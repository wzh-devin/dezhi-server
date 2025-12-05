package com.devin.dezhi.service;

import com.devin.dezhi.config.MinioConfig;
import io.minio.ListPartsResponse;
import io.minio.messages.Part;
import java.io.InputStream;

/**
 * 2025/12/6 01:30.
 *
 * <p>
 * MinioService
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface MinioService {

    /**
     * 初始化分片上传会话.
     *
     * @param objectName 文件名
     * @return Minio会话id
     */
    String initiateMultipartUpload(String objectName);

    /**
     * 上传分片.
     *
     * @param finalName     最终文件名
     * @param minioUploadId Minio会话id
     * @param chunkIndex    分片索引
     * @param inputStream   输入流
     * @param size          文件大小
     * @return Etag
     */
    String uploadChunk(String finalName, String minioUploadId, Integer chunkIndex, InputStream inputStream, long size);

    /**
     * 完成分片上传.
     *
     * @param finalName     最终文件名
     * @param minioUploadId Minio会话id
     * @param parts         分片列表
     */
    void completeMultipartUpload(String finalName, String minioUploadId, Part[] parts);

    /**
     * 获取分片列表.
     *
     * @param finalName     文件名
     * @param minioUploadId Minio会话id
     * @return 分片列表
     */
    ListPartsResponse listParts(String finalName, String minioUploadId);

    /**
     * 取消分片上传.
     *
     * @param finalName     文件名
     * @param minioUploadId Minio会话id
     */
    void cancelMultipartUpload(String finalName, String minioUploadId);

    /**
     * 获取文件公开访问地址.
     *
     * @param finalName 文件名
     * @return 文件公开访问地址
     */
    String getPublicUrl(String finalName);

    /**
     * 获取Minio配置.
     * @return Minio配置
     */
    MinioConfig getMinioConfig();
}
