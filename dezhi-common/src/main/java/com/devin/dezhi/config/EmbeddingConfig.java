package com.devin.dezhi.config;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

/**
 * 2026/1/15 21:58.
 *
 * <p>
 *     embedding配置
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class EmbeddingConfig {

    @Value("${dezhi.embedding.base-url}")
    private String baseUrl;

    @Value("${dezhi.embedding.api-key}")
    private String apiKey;

    @Value("${dezhi.embedding.model}")
    private String model;

    /**
     * 创建 EmbeddingModel.
     *
     * @return EmbeddingModel
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(model)
                .timeout(Duration.ofSeconds(60))
                .build();
    }

}
