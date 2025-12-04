package com.devin.dezhi.utils;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2025/12/5 02:23.
 *
 * <p>
 *     类型转换工具类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
// CHECKSTYLE:OFF
@Slf4j
public class ConvertUtils {

    /**
     * 日期格式化器缓存.
     */
    private static final ConcurrentHashMap<String, SimpleDateFormat> DATE_FORMAT_CACHE = new ConcurrentHashMap<>();

    /**
     * 常用日期格式.
     */
    private static final String[] DATE_PATTERNS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyyMMddHHmmss",
            "yyyyMMdd"
    };

    /**
     * 默认日期格式.
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // ==================== Object转换 ====================

    /**
     * 转换为String类型.
     *
     * @param value 待转换的值
     * @return String类型的值，如果为null则返回null
     */
    public static String toString(final Object value) {
        return toString(value, null);
    }

    /**
     * 转换为String类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return String类型的值，如果为null或转换失败则返回默认值
     */
    public static String toString(final Object value, final String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            log.debug("转换为String失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为Integer类型.
     *
     * @param value 待转换的值
     * @return Integer类型的值，如果为null或转换失败则返回null
     */
    public static Integer toInteger(final Object value) {
        return toInteger(value, null);
    }

    /**
     * 转换为Integer类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return Integer类型的值，如果为null或转换失败则返回默认值
     */
    public static Integer toInteger(final Object value, final Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Integer) {
                return (Integer) value;
            }
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }
                // 处理小数点字符串
                if (str.contains(".")) {
                    return new BigDecimal(str).intValue();
                }
                return Integer.valueOf(str);
            }
            if (value instanceof Boolean) {
                return ((Boolean) value) ? 1 : 0;
            }
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            log.debug("转换为Integer失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为Long类型.
     *
     * @param value 待转换的值
     * @return Long类型的值，如果为null或转换失败则返回null
     */
    public static Long toLong(final Object value) {
        return toLong(value, null);
    }

    /**
     * 转换为Long类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return Long类型的值，如果为null或转换失败则返回默认值
     */
    public static Long toLong(final Object value, final Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Long) {
                return (Long) value;
            }
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }
                // 处理小数点字符串
                if (str.contains(".")) {
                    return new BigDecimal(str).longValue();
                }
                return Long.valueOf(str);
            }
            if (value instanceof Boolean) {
                return ((Boolean) value) ? 1L : 0L;
            }
            if (value instanceof Date) {
                return ((Date) value).getTime();
            }
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            log.debug("转换为Long失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为Double类型.
     *
     * @param value 待转换的值
     * @return Double类型的值，如果为null或转换失败则返回null
     */
    public static Double toDouble(final Object value) {
        return toDouble(value, null);
    }

    /**
     * 转换为Double类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return Double类型的值，如果为null或转换失败则返回默认值
     */
    public static Double toDouble(final Object value, final Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Double) {
                return (Double) value;
            }
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }
                return Double.valueOf(str);
            }
            if (value instanceof Boolean) {
                return ((Boolean) value) ? 1.0 : 0.0;
            }
            return Double.valueOf(value.toString());
        } catch (Exception e) {
            log.debug("转换为Double失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为Float类型.
     *
     * @param value 待转换的值
     * @return Float类型的值，如果为null或转换失败则返回null
     */
    public static Float toFloat(final Object value) {
        return toFloat(value, null);
    }

    /**
     * 转换为Float类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return Float类型的值，如果为null或转换失败则返回默认值
     */
    public static Float toFloat(final Object value, final Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Float) {
                return (Float) value;
            }
            if (value instanceof Number) {
                return ((Number) value).floatValue();
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }
                return Float.valueOf(str);
            }
            if (value instanceof Boolean) {
                return ((Boolean) value) ? 1.0f : 0.0f;
            }
            return Float.valueOf(value.toString());
        } catch (Exception e) {
            log.debug("转换为Float失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为Boolean类型.
     *
     * @param value 待转换的值
     * @return Boolean类型的值，如果为null或转换失败则返回null
     */
    public static Boolean toBoolean(final Object value) {
        return toBoolean(value, null);
    }

    /**
     * 转换为Boolean类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return Boolean类型的值，如果为null或转换失败则返回默认值
     */
    public static Boolean toBoolean(final Object value, final Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            }
            if (value instanceof String) {
                String str = ((String) value).trim().toLowerCase();
                switch (str) {
                    case "" -> {
                        return defaultValue;
                    }

                    // true, yes, y, 1, on -> true
                    // false, no, n, 0, off -> false
                    case "true", "yes", "y", "1", "on" -> {
                        return true;
                    }
                    case "false", "no", "n", "0", "off" -> {
                        return false;
                    }
                }
            }
            return Boolean.valueOf(value.toString());
        } catch (Exception e) {
            log.debug("转换为Boolean失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为BigInteger类型.
     *
     * @param value 待转换的值
     * @return BigInteger类型的值，如果为null或转换失败则返回null
     */
    public static BigInteger toBigInteger(final Object value) {
        return toBigInteger(value, null);
    }

    /**
     * 转换为BigInteger类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return BigInteger类型的值，如果为null或转换失败则返回默认值
     */
    public static BigInteger toBigInteger(final Object value, final BigInteger defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof BigInteger) {
                return (BigInteger) value;
            }
            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).toBigInteger();
            }
            if (value instanceof Long) {
                return BigInteger.valueOf((Long) value);
            }
            if (value instanceof Integer) {
                return BigInteger.valueOf(((Integer) value).longValue());
            }
            if (value instanceof Number) {
                return BigInteger.valueOf(((Number) value).longValue());
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }
                // 处理小数点字符串
                if (str.contains(".")) {
                    return new BigDecimal(str).toBigInteger();
                }
                return new BigInteger(str);
            }
            return new BigInteger(value.toString());
        } catch (Exception e) {
            log.debug("转换为BigInteger失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为BigDecimal类型.
     *
     * @param value 待转换的值
     * @return BigDecimal类型的值，如果为null或转换失败则返回null
     */
    public static BigDecimal toBigDecimal(final Object value) {
        return toBigDecimal(value, null);
    }

    /**
     * 转换为BigDecimal类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return BigDecimal类型的值，如果为null或转换失败则返回默认值
     */
    public static BigDecimal toBigDecimal(final Object value, final BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }
            if (value instanceof BigInteger) {
                return new BigDecimal((BigInteger) value);
            }
            if (value instanceof Number) {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }
                return new BigDecimal(str);
            }
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            log.debug("转换为BigDecimal失败: value={}", value, e);
            return defaultValue;
        }
    }

    // ==================== 日期时间转换 ====================

    /**
     * 转换为Date类型.
     *
     * @param value 待转换的值
     * @return Date类型的值，如果为null或转换失败则返回null
     */
    public static Date toDate(final Object value) {
        return toDate(value, null);
    }

    /**
     * 转换为Date类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return Date类型的值，如果为null或转换失败则返回默认值
     */
    public static Date toDate(final Object value, final Date defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Date) {
                return (Date) value;
            }
            if (value instanceof Long) {
                return new Date((Long) value);
            }
            if (value instanceof LocalDateTime) {
                return Date.from(((LocalDateTime) value).atZone(java.time.ZoneId.systemDefault()).toInstant());
            }
            if (value instanceof LocalDate) {
                return Date.from(((LocalDate) value).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }

                // 尝试解析为时间戳
                if (str.matches("\\d+")) {
                    long timestamp = Long.parseLong(str);
                    // 判断是秒还是毫秒（13位是毫秒，10位是秒）
                    if (str.length() == 10) {
                        timestamp *= 1000;
                    }
                    return new Date(timestamp);
                }

                // 尝试各种日期格式
                for (String pattern : DATE_PATTERNS) {
                    try {
                        SimpleDateFormat sdf = getDateFormat(pattern);
                        return sdf.parse(str);
                    } catch (ParseException ignored) {
                        // 继续尝试下一个格式
                        log.error("尝试解析日期格式失败: pattern={}, value={}", pattern, str);
                    }
                }
            }
            return defaultValue;
        } catch (Exception e) {
            log.debug("转换为Date失败: value={}", value, e);
            return defaultValue;
        }
    }

    /**
     * 转换为LocalDateTime类型.
     *
     * @param value 待转换的值
     * @return LocalDateTime类型的值，如果为null或转换失败则返回null
     */
    public static LocalDateTime toLocalDateTime(final Object value) {
        return toLocalDateTime(value, null);
    }

    /**
     * 转换为LocalDateTime类型（带默认值）.
     *
     * @param value        待转换的值
     * @param defaultValue 默认值
     * @return LocalDateTime类型的值，如果为null或转换失败则返回默认值
     */
    public static LocalDateTime toLocalDateTime(final Object value, final LocalDateTime defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof LocalDateTime) {
                return (LocalDateTime) value;
            }
            if (value instanceof Date) {
                return LocalDateTime.ofInstant(((Date) value).toInstant(), java.time.ZoneId.systemDefault());
            }
            if (value instanceof Long) {
                return LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli((Long) value),
                        java.time.ZoneId.systemDefault());
            }
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return defaultValue;
                }

                // 尝试解析为时间戳
                if (str.matches("\\d+")) {
                    long timestamp = Long.parseLong(str);
                    if (str.length() == 10) {
                        timestamp *= 1000;
                    }
                    return LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(timestamp),
                            java.time.ZoneId.systemDefault());
                }

                // 尝试标准格式
                try {
                    return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (Exception ignored) {
                    // 尝试自定义格式
                    for (String pattern : DATE_PATTERNS) {
                        try {
                            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
                        } catch (Exception e) {
                            // 继续尝试下一个格式
                        }
                    }
                }
            }
            return defaultValue;
        } catch (Exception e) {
            log.debug("转换为LocalDateTime失败: value={}", value, e);
            return defaultValue;
        }
    }

    // ==================== 集合转换 ====================

    /**
     * 转换为List类型.
     *
     * @param value       待转换的值
     * @param elementType 元素类型
     * @param <T>         元素泛型
     * @return List类型的值，如果为null或转换失败则返回空List
     */
    public static <T> List<T> toList(final Object value, final Class<T> elementType) {
        if (value == null) {
            return new ArrayList<>();
        }

        try {
            if (value instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) value;
                return convertList(list, elementType);
            }
            if (value instanceof Collection) {
                return convertList(new ArrayList<>((Collection<?>) value), elementType);
            }
            if (value.getClass().isArray()) {
                Object[] array = (Object[]) value;
                return convertList(Arrays.asList(array), elementType);
            }
            // 单个元素转List
            List<T> result = new ArrayList<>();
            T converted = convert(value, elementType);
            if (converted != null) {
                result.add(converted);
            }
            return result;
        } catch (Exception e) {
            log.debug("转换为List失败: value={}, elementType={}", value, elementType, e);
            return new ArrayList<>();
        }
    }

    /**
     * 转换List中的元素类型.
     *
     * @param list       源List
     * @param targetType 目标类型
     * @param <T>        目标泛型
     * @return 转换后的List
     */
    private static <T> List<T> convertList(final List<?> list, final Class<T> targetType) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>(list.size());
        for (Object item : list) {
            T converted = convert(item, targetType);
            if (converted != null) {
                result.add(converted);
            }
        }
        return result;
    }

    // ==================== 通用转换 ====================

    /**
     * 通用类型转换方法.
     *
     * @param value      待转换的值
     * @param targetType 目标类型
     * @param <T>        目标泛型
     * @return 转换后的值，如果为null或转换失败则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(final Object value, final Class<T> targetType) {
        if (value == null || targetType == null) {
            return null;
        }

        // 类型相同，直接返回
        if (targetType.isInstance(value)) {
            return (T) value;
        }

        try {
            // String
            if (targetType == String.class) {
                return (T) toString(value);
            }
            // Integer
            if (targetType == Integer.class || targetType == int.class) {
                return (T) toInteger(value);
            }
            // Long
            if (targetType == Long.class || targetType == long.class) {
                return (T) toLong(value);
            }
            // Double
            if (targetType == Double.class || targetType == double.class) {
                return (T) toDouble(value);
            }
            // Float
            if (targetType == Float.class || targetType == float.class) {
                return (T) toFloat(value);
            }
            // Boolean
            if (targetType == Boolean.class || targetType == boolean.class) {
                return (T) toBoolean(value);
            }
            // BigInteger
            if (targetType == BigInteger.class) {
                return (T) toBigInteger(value);
            }
            // BigDecimal
            if (targetType == BigDecimal.class) {
                return (T) toBigDecimal(value);
            }
            // Date
            if (targetType == Date.class) {
                return (T) toDate(value);
            }
            // LocalDateTime
            if (targetType == LocalDateTime.class) {
                return (T) toLocalDateTime(value);
            }

            log.warn("不支持的目标类型: {}", targetType);
            return null;
        } catch (Exception e) {
            log.debug("类型转换失败: value={}, targetType={}", value, targetType, e);
            return null;
        }
    }

    /**
     * 通用类型转换方法（带默认值）.
     *
     * @param value        待转换的值
     * @param targetType   目标类型
     * @param defaultValue 默认值
     * @param <T>          目标泛型
     * @return 转换后的值，如果为null或转换失败则返回默认值
     */
    public static <T> T convert(final Object value, final Class<T> targetType, final T defaultValue) {
        T result = convert(value, targetType);
        return result != null ? result : defaultValue;
    }

    // ==================== 工具方法 ====================

    /**
     * 获取日期格式化器（带缓存）.
     *
     * @param pattern 日期格式
     * @return SimpleDateFormat实例
     */
    private static SimpleDateFormat getDateFormat(final String pattern) {
        return DATE_FORMAT_CACHE.computeIfAbsent(pattern, SimpleDateFormat::new);
    }

    /**
     * 判断字符串是否为数字.
     *
     * @param str 字符串
     * @return 是否为数字
     */
    public static boolean isNumeric(final String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 清除缓存.
     */
    public static void clearCache() {
        DATE_FORMAT_CACHE.clear();
        log.info("ConvertUtils缓存已清除");
    }
}
// CHECKSTYLE:ON
