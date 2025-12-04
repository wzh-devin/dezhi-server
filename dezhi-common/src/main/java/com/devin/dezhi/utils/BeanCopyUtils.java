package com.devin.dezhi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 2025/12/5 02:00.
 *
 * <p>
 *     Bean拷贝工具类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class BeanCopyUtils {

    /**
     * BeanCopier缓存，key格式：sourceClass_targetClass.
     */
    private static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * 枚举方法缓存，避免重复反射.
     */
    private static final ConcurrentHashMap<Class<?>, Method> ENUM_NAME_METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * 最大递归深度，防止循环引用.
     */
    private static final int MAX_DEPTH = 10;

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
     * 核心拷贝方法：使用CGLIB提升性能，并处理枚举转换.
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
     * 带深度控制的拷贝方法，防止循环引用.
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

        String cacheKey = generateCacheKey(sourceClass, targetClass);
        BeanCopier copier = BEAN_COPIER_CACHE.computeIfAbsent(cacheKey, key ->
                BeanCopier.create(sourceClass, targetClass, true)
        );

        copier.copy(source, target, new EnumConverter());

        handleSpecialFields(source, target, depth);
    }

    /**
     * 处理特殊字段（枚举、嵌套对象等）.
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
                    if (sourceValue.getClass().isEnum() && targetPropertyType == String.class) {
                        String enumValue = convertEnumToString(sourceValue);
                        targetBeanWrapper.setPropertyValue(propertyName, enumValue);
                    } else if (sourceValue instanceof String && targetPropertyType.isEnum()) {
                        Object enumValue = convertStringToEnum((String) sourceValue, targetPropertyType);
                        if (enumValue != null) {
                            targetBeanWrapper.setPropertyValue(propertyName, enumValue);
                        }
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
     * 枚举转String.
     *
     * @param enumValue 枚举对象
     * @return String
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
            log.debug("枚举转换失败: {}", enumValue, e);
            return enumValue.toString();
        }
    }

    /**
     * String转枚举.
     *
     * @param value     值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 枚举对象
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

    /**
     * 生成缓存key.
     *
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @return 返回生成的缓存key
     */
    private static String generateCacheKey(final Class<?> sourceClass, final Class<?> targetClass) {
        return sourceClass.getName() + "_" + targetClass.getName();
    }

    /**
     * 判断是否为简单值类型.
     *
     * @param type 类型
     * @return boolean
     */
    // CHECKSTYLE:OFF
    private static boolean isSimpleValueType(final Class<?> type) {
        return type.isPrimitive() || type.isEnum()
                || CharSequence.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type)
                || Date.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class;
    }
    // CHECKSTYLE:ON

    /**
     * 清除缓存.
     */
    public static void clearCache() {
        BEAN_COPIER_CACHE.clear();
        ENUM_NAME_METHOD_CACHE.clear();
        log.info("BeanCopyUtils缓存已清除");
    }

    /**
     * CGLIB枚举转换器.
     */
    private static final class EnumConverter implements Converter {
        @Override
        public Object convert(final Object value, final Class target, final Object context) {
            if (value == null) {
                return null;
            }

            // 枚举 -> String
            if (value.getClass().isEnum() && target == String.class) {
                return convertEnumToString(value);
            }

            // String -> 枚举
            if (value instanceof String && target.isEnum()) {
                return convertStringToEnum((String) value, target);
            }

            // 其他类型直接返回
            return value;
        }
    }

    /**
     * 自定义异常.
     */
    public static class BeanCopyException extends RuntimeException {
        public BeanCopyException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
