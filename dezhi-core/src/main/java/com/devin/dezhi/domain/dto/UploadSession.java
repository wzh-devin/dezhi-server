package com.devin.dezhi.domain.dto;

import com.devin.dezhi.enums.FileUploadStatusEnum;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 2025/12/6 01:12.
 *
 * <p>
 *     文件上传会话.
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class UploadSession {

    /**
     * 会话id.
     */
    private String uploadId;

    /**
     * Minio上传的会话id.
     */
    private String minioUploadId;

    /**
     * 文件存储桶名称.
     */
    private String bucketName;

    /**
     * 文件原始名称.
     */
    private String originalName;

    /**
     * 文件最终名称.
     */
    private String finalName;

    /**
     * 文件hash.
     */
    private String fileHash;

    /**
     * 文件总大小.
     */
    private Long totalSize;

    /**
     * 文件总分片数.
     */
    private Integer totalChunks;

    /**
     * 已经上传的分片.
     */
    private List<Integer> competedChunks = new ArrayList<>();

    /**
     * 文件上传状态.
     */
    private FileUploadStatusEnum status;

    /**
     * 会话创建时间.
     */
    private Long createTime;
}
