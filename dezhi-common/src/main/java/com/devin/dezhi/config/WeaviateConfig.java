package com.devin.dezhi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * 创建文本分词器.
     *
     * @return 文本分词器
     */
    @Bean
    public TextSplitter textSplitter() {
        return new TokenTextSplitter(
                500,
                50,
                100,
                20,
                true
        );
    }
}
