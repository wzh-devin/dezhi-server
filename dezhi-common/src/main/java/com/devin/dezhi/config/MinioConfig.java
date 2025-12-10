package com.devin.dezhi.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 2025/12/5 22:37.
 *
 * <p>
 * Minio 配置类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "dezhi.minio")
public class MinioConfig {
    /**
     * Minio 端点.
     */
    private String endpoint;

    /**
     * Minio 暴露地址.
     */
    private String exportUrl;

    /**
     * Minio 访问密钥.
     */
    private String accessKey;

    /**
     * Minio 密钥.
     */
    private String secretKey;

    /**
     * 存储桶名称.
     */
    private String bucketName;

    /**
     * 创建 MinioClient 对象.
     *
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        initBucket(minioClient);
        return minioClient;
    }

    /**
     * 创建 MinioAsyncClient 对象.
     *
     * @return MinioAsyncClient
     */
    @Bean
    public MinioAsyncClient minioAsyncClient() {
        return MinioAsyncClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 初始化存储桶.
     *
     * @param minioClient MinioClient
     */
    private void initBucket(final MinioClient minioClient) {
        log.info("======> 初始化 [{}] 存储桶", bucketName);
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }
            log.info("<====== [{}] 存储桶初始化完成", bucketName);
        } catch (Exception e) {
            log.error("{}存储桶初始化失败: {}", bucketName, e.getMessage());
            throw new RuntimeException("InitBucket Error: " + e.getMessage());
        }
    }
}
