package com.devin.dezhi.service.impl;

import com.devin.dezhi.config.MinioConfig;
import com.devin.dezhi.constant.FileConstant;
import com.devin.dezhi.exception.BusinessException;
import com.devin.dezhi.service.MinioService;
import io.minio.AbortMultipartUploadResponse;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadPartResponse;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * 2025/12/6 01:30.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioAsyncClient minioAsyncClient;

    private final MinioConfig minioConfig;

    @Override
    public String initiateMultipartUpload(
            final String objectName
    ) {
        try {
            CompletableFuture<CreateMultipartUploadResponse> future = minioAsyncClient.createMultipartUploadAsync(
                    minioConfig.getBucketName(),
                    null,
                    objectName,
                    null,
                    null
            );

            CreateMultipartUploadResponse response = future.get();
            return response.result().uploadId();
        } catch (Exception e) {
            log.error("initiateMultipartUpload error: {}", e.getMessage());
            throw new BusinessException("initiateMultipartUpload error");
        }
    }

    @Override
    public String uploadChunk(
            final String finalName,
            final String minioUploadId,
            final Integer chunkIndex,
            final InputStream inputStream,
            final long size
    ) {
        try {
            CompletableFuture<UploadPartResponse> future = minioAsyncClient.uploadPartAsync(
                    minioConfig.getBucketName(),
                    null,
                    finalName,
                    inputStream,
                    size,
                    minioUploadId,
                    chunkIndex,
                    null,
                    null
            );

            UploadPartResponse response = future.get();
            return response.etag();
        } catch (Exception e) {
            log.error("uploadChunk error: {}", e.getMessage());
            throw new BusinessException("uploadChunk error");
        }
    }

    @Override
    public void completeMultipartUpload(
            final String finalName,
            final String minioUploadId,
            final Part[] parts
    ) {
        try {
            CompletableFuture<ObjectWriteResponse> future = minioAsyncClient.completeMultipartUploadAsync(
                    minioConfig.getBucketName(),
                    null,
                    finalName,
                    minioUploadId,
                    parts,
                    null,
                    null
            );
            ObjectWriteResponse response = future.get();
        } catch (Exception e) {
            log.error("completeMultipartUpload error: {}", e.getMessage());
            throw new BusinessException("completeMultipartUpload error");
        }
    }

    @Override
    public ListPartsResponse listParts(
            final String finalName,
            final String minioUploadId
    ) {
        try {
            CompletableFuture<ListPartsResponse> future =
                    minioAsyncClient.listPartsAsync(
                            minioConfig.getBucketName(),
                            null,
                            finalName,
                            FileConstant.MAX_CHUNK_SIZE,
                            0,
                            minioUploadId,
                            null,
                            null
                    );

            return future.get();
        } catch (Exception e) {
            log.error("listParts error: {}", e.getMessage());
            throw new BusinessException("listParts error");
        }
    }

    @Override
    public void cancelMultipartUpload(final String finalName, final String minioUploadId) {
        try {
            CompletableFuture<AbortMultipartUploadResponse> future = minioAsyncClient.abortMultipartUploadAsync(
                    minioConfig.getBucketName(),
                    null,
                    finalName,
                    minioUploadId,
                    null,
                    null
            );
            future.get();
        } catch (Exception e) {
            log.error("cancelMultipartUpload error: {}", e.getMessage());
            throw new BusinessException("cancelMultipartUpload error");
        }
    }

    @Override
    public String getPublicUrl(final String finalName) {
        String url = minioConfig.getExportUrl();
        if (!StringUtils.hasLength(url)) {
            url = minioConfig.getEndpoint();
        }
        return url;
    }

    @Override
    public MinioConfig getMinioConfig() {
        return minioConfig;
    }
}
