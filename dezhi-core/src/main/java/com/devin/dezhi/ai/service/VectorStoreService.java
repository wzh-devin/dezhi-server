package com.devin.dezhi.ai.service;

import org.springframework.ai.document.Document;
import java.math.BigInteger;
import java.util.List;

/**
 * 2026/1/7 18:46.
 *
 * <p>
 *     向量存储服务
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface VectorStoreService {
    /**
     * 更新向量库.
     * @param articleId 文章id.
     */
    void updateVectorStore(BigInteger articleId);

    /**
     * 检索向量库.
     * @param message 检索消息.
     * @param topK 检索数量.
     * @return 检索结果.
     */
    List<Document> retrieve(String message, Integer topK);
}
