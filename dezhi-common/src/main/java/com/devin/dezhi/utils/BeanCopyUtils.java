package com.devin.dezhi.utils;

import com.devin.dezhi.annocation.EnumValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bean拷贝工具类（增强版）.
 *
 * <p>
 * 支持功能：
 * 1. 基础类型拷贝（使用CGLIB提升性能）
 * 2. 枚举 ↔ String 互转
 * 3. 枚举 ↔ 任意类型互转（基于@EnumValue注解）
 * - 只要枚举上标注了@EnumValue，无论对方是Entity、VO、DTO还是任何POJO，都会自动转换
 * - 枚举 -> 其他类型：提取@EnumValue标注的字段值
 * - 其他类型 -> 枚举：根据值匹配@EnumValue标注的字段
 * 4. List批量拷贝
 * 5. 循环引用防护
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 2.0.0
 * @since 1.0.0
 */
@Slf4j
public class BeanCopyUtils {

    // ==================== 缓存定义 ====================

    /**
     * BeanCopier缓存，key格式：sourceClass_targetClass.
     */
    private static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * 枚举name方法缓存.
     */
    private static final ConcurrentHashMap<Class<?>, Method> ENUM_NAME_METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * 枚举@EnumValue字段缓存.
     */
    private static final ConcurrentHashMap<Class<?>, Field> ENUM_VALUE_FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 枚举@EnumValue方法缓存.
     */
    private static final ConcurrentHashMap<Class<?>, Method> ENUM_VALUE_METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * 枚举实例缓存: key=枚举类_值, value=枚举实例.
     */
    private static final ConcurrentHashMap<String, Object> ENUM_INSTANCE_CACHE = new ConcurrentHashMap<>();

    /**
     * 枚举初始化标记缓存.
     */
    private static final ConcurrentHashMap<Class<?>, Boolean> ENUM_INITIALIZED_CACHE = new ConcurrentHashMap<>();

    /**
     * 最大递归深度，防止循环引用.
     */
    private static final int MAX_DEPTH = 10;

    // ==================== 公共API ====================

    /**
     * 拷贝单个对象.
     *
     * @param source      源对象
     * @param targetClass 目标类型
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return 目标对象
     */
    public static <S, T> T copy(final S source, final Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error("Bean拷贝失败: source={}, targetClass={}",
                    source.getClass().getName(), targetClass.getName(), e);
            throw new BeanCopyException("对象拷贝失败", e);
        }
    }

    /**
     * 拷贝List集合.
     *
     * @param sourceList  源集合
     * @param targetClass 目标类型
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return 目标集合
     */
    public static <S, T> List<T> copyList(final List<S> sourceList, final Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }

        if (sourceList.size() > 100000) {
            log.warn("集合大小超过10万，建议分批处理: size={}", sourceList.size());
        }

        try {
            return sourceList.stream()
                    .filter(Objects::nonNull)
                    .map(source -> copy(source, targetClass))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("List拷贝失败: targetClass={}", targetClass.getName(), e);
            throw new BeanCopyException("集合拷贝失败", e);
        }
    }

    /**
     * 拷贝对象属性.
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(final Object source, final Object target) {
        if (source == null || target == null) {
            return;
        }

        copyPropertiesWithDepth(source, target, 0);
    }

    /**
     * 清除缓存.
     */
    public static void clearCache() {
        BEAN_COPIER_CACHE.clear();
        ENUM_NAME_METHOD_CACHE.clear();
        ENUM_VALUE_FIELD_CACHE.clear();
        ENUM_VALUE_METHOD_CACHE.clear();
        ENUM_INSTANCE_CACHE.clear();
        ENUM_INITIALIZED_CACHE.clear();
        log.info("BeanCopyUtils缓存已清除");
    }

    /**
     * 获取缓存统计信息.
     *
     * @return 缓存统计
     */
    public static Map<String, Integer> getCacheStats() {
        return Map.of(
                "beanCopierCache", BEAN_COPIER_CACHE.size(),
                "enumNameMethodCache", ENUM_NAME_METHOD_CACHE.size(),
                "enumValueFieldCache", ENUM_VALUE_FIELD_CACHE.size(),
                "enumValueMethodCache", ENUM_VALUE_METHOD_CACHE.size(),
                "enumInstanceCache", ENUM_INSTANCE_CACHE.size(),
                "enumInitializedCache", ENUM_INITIALIZED_CACHE.size()
        );
    }

    // ==================== 核心拷贝逻辑 ====================

    /**
     * 带深度控制的拷贝方法.
     *
     * @param source 源对象
     * @param target 目标对象
     * @param depth  递归深度
     */
    private static void copyPropertiesWithDepth(final Object source, final Object target, final int depth) {
        if (depth > MAX_DEPTH) {
            log.warn("拷贝深度超过最大限制，可能存在循环引用: depth={}", depth);
            return;
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // 使用CGLIB BeanCopier进行快速拷贝
        String cacheKey = generateCacheKey(sourceClass, targetClass);
        BeanCopier copier = BEAN_COPIER_CACHE.computeIfAbsent(cacheKey, key ->
                BeanCopier.create(sourceClass, targetClass, true)
        );

        // 使用增强版转换器
        copier.copy(source, target, new EnhancedEnumConverter());

        // 处理特殊字段（枚举转换）
        handleSpecialFields(source, target, depth);
    }

    /**
     * 处理特殊字段（枚举转换）.
     *
     * @param source 源对象
     * @param target 目标对象
     * @param depth  递归深度
     */
    private static void handleSpecialFields(final Object source, final Object target, final int depth) {
        try {
            BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
            BeanWrapper targetBeanWrapper = new BeanWrapperImpl(target);

            PropertyDescriptor[] propertyDescriptors = sourceBeanWrapper.getPropertyDescriptors();

            for (PropertyDescriptor pd : propertyDescriptors) {
                String propertyName = pd.getName();

                if ("class".equals(propertyName)) {
                    continue;
                }

                try {
                    Object sourceValue = sourceBeanWrapper.getPropertyValue(propertyName);
                    if (sourceValue == null) {
                        continue;
                    }
                    if (!targetBeanWrapper.isWritableProperty(propertyName)) {
                        continue;
                    }
                    Class<?> targetPropertyType = targetBeanWrapper.getPropertyType(propertyName);
                    if (targetPropertyType == null) {
                        continue;
                    }

                    Class<?> sourcePropertyType = sourceValue.getClass();

                    // 类型相同，无需转换
                    if (sourcePropertyType.equals(targetPropertyType)) {
                        continue;
                    }

                    // 尝试枚举转换
                    Object convertedValue = tryConvertEnum(sourceValue, sourcePropertyType, targetPropertyType);
                    if (convertedValue != null) {
                        targetBeanWrapper.setPropertyValue(propertyName, convertedValue);
                    }

                } catch (Exception e) {
                    log.debug("属性拷贝失败，跳过: property={}, error={}",
                            propertyName, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("特殊字段处理失败", e);
        }
    }

    /**
     * 尝试枚举转换（统一入口）.
     *
     * @param sourceValue 源值
     * @param sourceType  源类型
     * @param targetType  目标类型
     * @return 转换后的值，如果无法转换返回null
     */
    // CHECKSTYLE:OFF
    private static Object tryConvertEnum(final Object sourceValue, final Class<?> sourceType, final Class<?> targetType) {
        if (sourceValue == null || sourceType == null || targetType == null) {
            return null;
        }
        if (sourceType.isEnum() && targetType == String.class) {
            return convertEnumToString(sourceValue);
        }
        if (sourceValue instanceof String && targetType.isEnum()) {
            return convertStringToEnum((String) sourceValue, targetType);
        }
        if (sourceType.isEnum() && !targetType.isEnum()) {
            Object enumValue = extractEnumValue(sourceValue);
            if (enumValue != null && isAssignable(enumValue.getClass(), targetType)) {
                return convertType(enumValue, targetType);
            }
        }
        if (!sourceType.isEnum() && targetType.isEnum()) {
            return findEnumByValue(sourceValue, targetType);
        }

        return null;
    }
    // CHECKSTYLE:ON

    // ==================== 枚举转换：基础方法 ====================

    /**
     * 枚举转String（使用name()）.
     *
     * @param enumValue 枚举值
     * @return 转换后的String
     */
    private static String convertEnumToString(final Object enumValue) {
        if (enumValue == null) {
            return null;
        }

        try {
            Method nameMethod = ENUM_NAME_METHOD_CACHE.computeIfAbsent(
                    enumValue.getClass(),
                    clazz -> {
                        try {
                            return clazz.getMethod("name");
                        } catch (NoSuchMethodException e) {
                            return null;
                        }
                    }
            );

            if (nameMethod != null) {
                return (String) nameMethod.invoke(enumValue);
            }

            return enumValue.toString();
        } catch (Exception e) {
            log.debug("枚举转String失败: {}", enumValue, e);
            return enumValue.toString();
        }
    }

    /**
     * String转枚举（使用valueOf）.
     *
     * @param value     待转换的值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return Object
     */
    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> Object convertStringToEnum(final String value, final Class<?> enumClass) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Enum.valueOf((Class<T>) enumClass, value.trim());
        } catch (Exception e) {
            log.debug("String转枚举失败: value={}, enumClass={}", value, enumClass.getName());
            return null;
        }
    }

    // ==================== 枚举转换：@EnumValue支持 ====================

    /**
     * 从枚举实例中提取值（基于@EnumValue标注）.
     *
     * @param enumInstance 枚举实例
     * @return 提取的值，如果未标注@EnumValue则返回 name()
     */
    private static Object extractEnumValue(final Object enumInstance) {
        if (enumInstance == null || !enumInstance.getClass().isEnum()) {
            return null;
        }

        try {
            Class<?> enumClass = enumInstance.getClass();

            // 优先使用@EnumValue标注的字段
            Field valueField = getEnumValueField(enumClass);
            if (valueField != null) {
                valueField.setAccessible(true);
                return valueField.get(enumInstance);
            }

            // 其次使用@EnumValue标注的方法
            Method valueMethod = getEnumValueMethod(enumClass);
            if (valueMethod != null) {
                valueMethod.setAccessible(true);
                return valueMethod.invoke(enumInstance);
            }

            // 降级：返回枚举的name
            log.debug("枚举{}未找到@EnumValue标注，使用name()降级", enumClass.getName());
            return ((Enum<?>) enumInstance).name();

        } catch (Exception e) {
            log.error("提取枚举值失败: enum={}", enumInstance, e);
            return null;
        }
    }

    /**
     * 根据值查找枚举实例（基于@EnumValue匹配）.
     *
     * @param value     要查找的值
     * @param enumClass 枚举类
     * @return 匹配的枚举实例，找不到返回null
     */
    private static Object findEnumByValue(final Object value, final Class<?> enumClass) {
        if (value == null || enumClass == null || !enumClass.isEnum()) {
            return null;
        }

        try {
            // 初始化枚举缓存
            initEnumCache(enumClass);

            // 从缓存中查找
            String cacheKey = generateEnumCacheKey(enumClass, value);
            Object cachedEnum = ENUM_INSTANCE_CACHE.get(cacheKey);

            if (cachedEnum != null) {
                return cachedEnum;
            }

            // 缓存未命中，遍历查找
            Object[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants == null) {
                return null;
            }

            for (Object enumConstant : enumConstants) {
                Object enumValue = extractEnumValue(enumConstant);
                if (isValueMatch(value, enumValue)) {
                    // 缓存结果
                    ENUM_INSTANCE_CACHE.put(cacheKey, enumConstant);
                    return enumConstant;
                }
            }

            log.debug("未找到匹配的枚举: value={}, enumClass={}", value, enumClass.getName());
            return null;

        } catch (Exception e) {
            log.error("查找枚举失败: value={}, enumClass={}", value, enumClass.getName(), e);
            return null;
        }
    }

    /**
     * 初始化枚举缓存.
     *
     * @param enumClass 枚举类
     */
    private static void initEnumCache(final Class<?> enumClass) {
        if (ENUM_INITIALIZED_CACHE.containsKey(enumClass)) {
            return;
        }
        synchronized (enumClass) {
            if (ENUM_INITIALIZED_CACHE.containsKey(enumClass)) {
                return;
            }

            try {
                Object[] enumConstants = enumClass.getEnumConstants();
                if (enumConstants == null || enumConstants.length == 0) {
                    return;
                }

                // 预热缓存：遍历所有枚举实例，建立值到枚举的映射
                for (Object enumConstant : enumConstants) {
                    Object value = extractEnumValue(enumConstant);
                    if (value != null) {
                        String cacheKey = generateEnumCacheKey(enumClass, value);
                        ENUM_INSTANCE_CACHE.put(cacheKey, enumConstant);
                    }
                }

                ENUM_INITIALIZED_CACHE.put(enumClass, Boolean.TRUE);
                log.debug("枚举缓存初始化成功: enumClass={}, count={}",
                        enumClass.getName(), enumConstants.length);

            } catch (Exception e) {
                log.error("枚举缓存初始化失败: enumClass={}", enumClass.getName(), e);
            }
        }
    }

    /**
     * 获取枚举的@EnumValue标注字段.
     *
     * @param enumClass 枚举类
     * @return 字段
     */
    private static Field getEnumValueField(final Class<?> enumClass) {
        return ENUM_VALUE_FIELD_CACHE.computeIfAbsent(enumClass, clazz -> {
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(EnumValue.class)) {
                        field.setAccessible(true);
                        log.debug("找到@EnumValue字段: class={}, field={}",
                                clazz.getName(), field.getName());
                        return field;
                    }
                }
                return null;
            } catch (Exception e) {
                log.error("获取@EnumValue字段失败: class={}", clazz.getName(), e);
                return null;
            }
        });
    }

    /**
     * 获取枚举的@EnumValue标注方法.
     *
     * @param enumClass 枚举类
     * @return 方法
     */
    private static Method getEnumValueMethod(final Class<?> enumClass) {
        return ENUM_VALUE_METHOD_CACHE.computeIfAbsent(enumClass, clazz -> {
            try {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(EnumValue.class)) {
                        method.setAccessible(true);
                        log.debug("找到@EnumValue方法: class={}, method={}",
                                clazz.getName(), method.getName());
                        return method;
                    }
                }
                return null;
            } catch (Exception e) {
                log.error("获取@EnumValue方法失败: class={}", clazz.getName(), e);
                return null;
            }
        });
    }

    // ==================== 工具方法 ====================

    /**
     * 判断两个值是否匹配（支持类型转换）.
     *
     * @param value1 值1
     * @param value2 值2
     * @return 是否匹配
     */
    private static boolean isValueMatch(final Object value1, final Object value2) {
        if (value1 == null || value2 == null) {
            return false;
        }

        if (value1.equals(value2)) {
            return true;
        }

        String str1 = String.valueOf(value1).trim();
        String str2 = String.valueOf(value2).trim();

        if (str1.equalsIgnoreCase(str2)) {
            return true;
        }

        // 数字类型转换比较
        if (isNumeric(value1) && isNumeric(value2)) {
            try {
                return Double.parseDouble(str1) == Double.parseDouble(str2);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * 判断对象是否为数字类型.
     *
     * @param value 对象
     * @return 是否为数字类型
     */
    private static boolean isNumeric(final Object value) {
        return value instanceof Number
                || value instanceof String && ((String) value).matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 判断源类型是否可以赋值给目标类型.
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 是否可以赋值
     */
    // CHECKSTYLE:OFF
    private static boolean isAssignable(final Class<?> sourceType, final Class<?> targetType) {
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }

        // 处理基本类型和包装类型
        if (targetType.isPrimitive()) {
            if (targetType == int.class && sourceType == Integer.class) {
                return true;
            }
            if (targetType == long.class && sourceType == Long.class) {
                return true;
            }
            if (targetType == double.class && sourceType == Double.class) {
                return true;
            }
            if (targetType == float.class && sourceType == Float.class) {
                return true;
            }
            if (targetType == boolean.class && sourceType == Boolean.class) {
                return true;
            }
            if (targetType == byte.class && sourceType == Byte.class) {
                return true;
            }
            if (targetType == short.class && sourceType == Short.class) {
                return true;
            }
            return targetType == char.class && sourceType == Character.class;
        }

        return false;
    }
    // CHECKSTYLE:ON

    /**
     * 类型转换.
     *
     * @param value      源值
     * @param targetType 目标类型
     * @return 转换后的值
     */
    //CHECKSTYLE:OFF
    private static Object convertType(final Object value, final Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isInstance(value)) {
            return value;
        }

        try {
            if (value instanceof String strValue) {
                if (targetType == Integer.class || targetType == int.class) {
                    return Integer.valueOf(strValue);
                }
                if (targetType == Long.class || targetType == long.class) {
                    return Long.valueOf(strValue);
                }
                if (targetType == Double.class || targetType == double.class) {
                    return Double.valueOf(strValue);
                }
                if (targetType == Float.class || targetType == float.class) {
                    return Float.valueOf(strValue);
                }
                if (targetType == Boolean.class || targetType == boolean.class) {
                    return Boolean.valueOf(strValue);
                }
            }

            if (value instanceof Number numValue) {
                if (targetType == Integer.class || targetType == int.class) {
                    return numValue.intValue();
                }
                if (targetType == Long.class || targetType == long.class) {
                    return numValue.longValue();
                }
                if (targetType == Double.class || targetType == double.class) {
                    return numValue.doubleValue();
                }
                if (targetType == Float.class || targetType == float.class) {
                    return numValue.floatValue();
                }
                if (targetType == String.class) {
                    return numValue.toString();
                }
            }

            return value;
        } catch (Exception e) {
            log.debug("类型转换失败: value={}, targetType={}", value, targetType.getName());
            return value;
        }
    }
    //CHECKSTYLE:ON

    /**
     * 生成BeanCopier缓存key.
     *
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @return 缓存key
     */
    private static String generateCacheKey(final Class<?> sourceClass, final Class<?> targetClass) {
        return sourceClass.getName() + "_" + targetClass.getName();
    }

    /**
     * 生成枚举缓存key.
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @return 缓存key
     */
    private static String generateEnumCacheKey(final Class<?> enumClass, final Object value) {
        return enumClass.getName() + "_" + value;
    }

    /**
     * 判断是否为简单值类型.
     */
    // CHECKSTYLE:OFF
    @SuppressWarnings("unused")
    private static boolean isSimpleValueType(final Class<?> type) {
        return type.isPrimitive() || type.isEnum()
                || CharSequence.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type)
                || Date.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class;
    }
    // CHECKSTYLE:ON

    // ==================== CGLIB转换器 ====================

    /**
     * 增强版CGLIB枚举转换器.
     */
    private static final class EnhancedEnumConverter implements Converter {
        @Override
        public Object convert(final Object value, final Class target, final Object context) {
            if (value == null) {
                return null;
            }

            try {
                // 使用统一的枚举转换方法
                Object convertedValue = tryConvertEnum(value, value.getClass(), target);
                if (convertedValue != null) {
                    return convertedValue;
                }
            } catch (Exception e) {
                log.debug("枚举转换失败: value={}, target={}", value, target.getName(), e);
            }

            // 其他类型直接返回
            return value;
        }
    }

    // ==================== 自定义异常 ====================

    /**
     * Bean拷贝异常.
     */
    public static class BeanCopyException extends RuntimeException {
        public BeanCopyException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}