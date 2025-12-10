package com.devin.dezhi.enums;

/**
 * 2025/12/5 23:57.
 *
 * <p>
 *     文件上传状态枚举
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public enum FileUploadStatusEnum {
    /**
     * 上传中.
     */
    UPLOADING,

    /**
     * 上传完成.
     */
    COMPLETED,

    /**
     * 秒传.
     */
    INSTANT,

    /**
     * 上传失败.
     */
    FAILED
}
