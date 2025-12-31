package com.devin.dezhi.ai.retrieval.impl;

import com.devin.dezhi.ai.retrieval.RAGRetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2025/12/31 23:25.
 *
 * <p>
 *     RAG检索服务实现类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RAGRetrievalServiceImpl implements RAGRetrievalService {

    private final VectorStore vectorStore;

    @Override
    public List<Document> retrieve(
            final String message,
            final Integer topK,
            final Map<String, Object> filters,
            final boolean rerank
    ) {
        SearchRequest.Builder requestBuilder = SearchRequest.builder()
                .query(message)
                .topK(topK)
                .similarityThreshold(0.7);

        // 构建过滤条件
        if (filters != null && !filters.isEmpty()) {
            Filter.Expression filterExpression = buildFilterExpression(filters);
            if (filterExpression != null) {
                requestBuilder.filterExpression(filterExpression);
            }
        }

        return vectorStore.similaritySearch(requestBuilder.build());
    }

    private Filter.Expression buildFilterExpression(final Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return null;
        }

        List<Filter.Expression> expressions = new ArrayList<>();
        FilterExpressionBuilder builder = new FilterExpressionBuilder();

        if (filters.containsKey("categoryId")) {
            expressions.add(builder.eq("categoryId", String.valueOf(filters.get("categoryId"))).build());
        }

        if (filters.containsKey("status")) {
            expressions.add(builder.eq("status", String.valueOf(filters.get("status"))).build());
        }

        if (expressions.isEmpty()) {
            return null;
        }

        if (expressions.size() == 1) {
            return expressions.get(0);
        }

        // 使用 Filter.Group 和 Filter.Expression 直接构造 AND
        Filter.Expression result = expressions.get(0);
        for (int i = 1; i < expressions.size(); i++) {
            result = new Filter.Expression(
                    Filter.ExpressionType.AND,
                    new Filter.Group(result),
                    new Filter.Group(expressions.get(i))
            );
        }
        return result;
    }
}
