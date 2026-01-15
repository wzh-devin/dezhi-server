package com.devin.dezhi.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 2025/12/31 16:41.
 *
 * <p>
 * Weaviate 配置
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class WeaviateConfig {

    @Value("${dezhi.weaviate.host}")
    private String host;

    @Value("${dezhi.weaviate.scheme}")
    private String scheme;

    @Value("${dezhi.weaviate.api-key}")
    private String apiKey;

    @Value("${dezhi.weaviate.object-class}")
    private String objectClass;

    /**
     * Weaviate 客户端.
     * @return WeaviateClient
     */
    @Bean
    public WeaviateClient weaviateClient() {
        Config config = new Config(scheme, host, Map.of(
                "Authorization", "Bearer " + apiKey
        ));

        return new WeaviateClient(config);
    }

    /**
     * 创建 EmbeddingStore.
     *
     * @return EmbeddingStore
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return WeaviateEmbeddingStore.builder()
                .scheme(scheme)
                .host(host)
                .apiKey(apiKey)
                .objectClass(objectClass)
                .metadataKeys(metadataKeys())
                .build();
    }

    /**
     * 定义元数据字段.
     *
     * @return 元数据字段
     */
    private Collection<String> metadataKeys() {
        return List.of(
                "articleId",
                "categoryId",
                "chunkIndex",
                "status",
                "title",
                "summary",
                "uri",
                "createTime",
                "updateTime"
        );
    }
}
