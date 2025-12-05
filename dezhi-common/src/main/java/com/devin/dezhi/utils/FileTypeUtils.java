package com.devin.dezhi.utils;

import com.devin.dezhi.constant.FileConstant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 2025/12/6 00:28.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileTypeUtils {

    private static final Map<String, String> EXTENSION_TO_MIME = new HashMap<>();

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"
    );

    static {
        EXTENSION_TO_MIME.put("jpg", "image/jpeg");
        EXTENSION_TO_MIME.put("jpeg", "image/jpeg");
        EXTENSION_TO_MIME.put("png", "image/png");
        EXTENSION_TO_MIME.put("gif", "image/gif");
        EXTENSION_TO_MIME.put("bmp", "image/bmp");
        EXTENSION_TO_MIME.put("webp", "image/webp");
        EXTENSION_TO_MIME.put("svg", "image/svg+xml");
    }

    /**
     * 根据文件扩展名获取MIME类型.
     *
     * @param extension 扩展名
     * @return MIME类型
     */
    public static String getMimeType(final String extension) {
        if (Objects.isNull(extension) || extension.isEmpty()) {
            return "application/octet-stream";
        }
        return EXTENSION_TO_MIME.getOrDefault(
                extension.toLowerCase(),
                "application/octet-stream"
        );
    }

    /**
     * 从文件名提取扩展名.
     *
     * @param filename 文件名
     * @return 扩展名
     */
    public static String getExtension(final String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 验证是否为允许的图片类型.
     *
     * @param extension 扩展名
     * @return 是否为允许的图片类型
     */
    public static boolean isAllowedImageType(final String extension) {
        return extension != null && IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * 验证文件大小是否合法（最大100MB）.
     *
     * @param size 文件大小
     * @return 是否合法
     */
    public static boolean isValidFileSize(final Long size) {
        return size != null && size > 0 && size <= FileConstant.MAX_FILE_SIZE;
    }

    /**
     * 获取最大允许的文件大小.
     *
     * @param unit 单位,支持: B(字节), KB(千字节), MB(兆字节), GB(吉字节)
     * @return 指定单位下的最大允许文件大小
     */
    public static Long getMaxSize(final FileUnit unit) {
        return switch (unit.name()) {
            case "KB", "K" -> FileConstant.MAX_FILE_SIZE / 1024;
            case "MB", "M" -> FileConstant.MAX_FILE_SIZE / (1024 * 1024);
            case "GB", "G" -> FileConstant.MAX_FILE_SIZE / (1024 * 1024 * 1024);
            default -> FileConstant.MAX_FILE_SIZE;
        };
    }

    /**
     * 文件大小单位枚举.
     */
    public enum FileUnit {
        B, KB, MB, GB
    }
}
