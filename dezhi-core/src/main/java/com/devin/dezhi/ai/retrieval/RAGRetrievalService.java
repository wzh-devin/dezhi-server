package com.devin.dezhi.ai.retrieval;

import org.springframework.ai.document.Document;
import java.util.List;
import java.util.Map;

/**
 * 2025/12/31 23:24.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RAGRetrievalService {

    /**
     * 检索.
     * @param message 检索内容
     * @param topK 检索数量
     * @param filters 过滤条件
     * @param rerank 是否重新排序
     * @return 检索结果
     */
    List<Document> retrieve(String message, Integer topK, Map<String, Object> filters, boolean rerank);
}
