package com.devin.dezhi.constant;

/**
 * 2025/12/4 22:00.
 *
 * <p>
 *     Redis常量配置
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RedisKey {

    /**
     * Redis基础Key.
     */
    private static final String BASE_KEY = "dezhi:";

    /**
     * 生成RedisKey.
     * @param key RedisKey
     * @param args 填写的参数
     * @return RedisKey
     */
    public static String generateRedisKey(final String key, final Object... args) {
        if (args.length == 0) {
            return BASE_KEY.concat(key).replace("%s", "");
        }
        return String.format(BASE_KEY.concat(key), args);
    }
}
