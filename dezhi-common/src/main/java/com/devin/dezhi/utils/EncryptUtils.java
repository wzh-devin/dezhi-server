package com.devin.dezhi.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;

/**
 * 2025/12/4 22:41.
 *
 * <p>
 *     加密工具类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class EncryptUtils {
    // ==================== MD5 加密（不可逆） ====================

    /**
     * MD5 加密（32位小写）.
     * 用途：文件校验、数据完整性验证
     *
     * @param text 明文
     * @return 密文
     */
    public static String md5(final String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        return SecureUtil.md5(text);
    }

    /**
     * MD5 加盐加密.
     *
     * @param text 明文
     * @param salt 盐值
     * @return 密文
     */
    public static String md5WithSalt(final String text, final String salt) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        return SecureUtil.md5(text + salt);
    }

    // ==================== SHA 加密（不可逆） ====================

    /**
     * SHA-1 加密.
     *
     * @param text 明文
     * @return 密文
     */
    public static String sha1(final String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        return SecureUtil.sha1(text);
    }

    /**
     * SHA-256 加密.
     * 用途：密码存储、数字签名
     *
     * @param text 明文
     * @return 密文
     */
    public static String sha256(final String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        return SecureUtil.sha256(text);
    }

    // ==================== BCrypt 加密（密码专用） ====================

    /**
     * BCrypt 加密.
     * 用途：用户密码存储
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String bcrypt(final String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 验证 BCrypt 密码.
     *
     * @param password 明文密码
     * @param hashed 加密后的密码
     * @return 是否匹配
     */
    public static boolean bcryptCheck(final String password, final String hashed) {
        if (StrUtil.isBlank(password) || StrUtil.isBlank(hashed)) {
            return false;
        }
        return BCrypt.checkpw(password, hashed);
    }
}
