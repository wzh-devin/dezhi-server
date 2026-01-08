package com.devin.dezhi.ai.service.impl;

import com.devin.dezhi.ai.service.VectorStoreService;
import com.devin.dezhi.dao.ArticleDao;
import com.devin.dezhi.domain.entity.Article;
import com.devin.dezhi.utils.IdGenerator;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 2026/1/7 18:46.
 *
 * <p>
 * 向量存储服务实现类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VectorStoreServiceImpl implements VectorStoreService {

    /**
     * 每批次添加的文档数量.
     */
    private static final int BATCH_SIZE = 8;

    private final VectorStore vectorStore;

    private final TextSplitter textSplitter;

    private final ArticleDao articleDao;

    private final ThreadPoolTaskExecutor vectorStoreTaskExecutor;

    @Override
    public void updateVectorStore(final BigInteger articleId) {
        // 获取更新之后的文章信息
        Article article = articleDao.getById(articleId);
        if (Objects.isNull(article)) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            // 删除文章向量
            deleteVectorStore(articleId);
            // 保存文章向量
            saveVectorStore(article);
        }, vectorStoreTaskExecutor).join();
    }

    @Override
    public List<Document> retrieve(
            final String message,
            final Integer topK
    ) {
        SearchRequest.Builder requestBuilder = SearchRequest.builder()
                .query(message)
                .topK(topK)
                .similarityThreshold(0.7);

        return vectorStore.similaritySearch(requestBuilder.build());
    }

    /**
     * 保存文章向量.
     *
     * @param article 文章
     */
    public void saveVectorStore(final Article article) {
        // 新增文章向量
        String content = Objects.nonNull(article.getContentMd())
                ? article.getContentMd()
                : article.getContent();
        if (Objects.isNull(content) || content.trim().isEmpty()) {
            return;
        }

        // 创建文档
        Document originalDocument = Document.builder()
                .metadata(buildMetadata(article, 0))
                .text(content)
                .build();
        // 切分文档
        List<Document> chunks = textSplitter.split(originalDocument);
        // 设置元数据
        ArrayList<Document> documentMetadataList = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            Document chunk = chunks.get(i);
            Document doc = Document.builder()
                    .id(IdGenerator.generateUUID())
                    .metadata(buildMetadata(article, i))
                    .text(chunk.getText())
                    .build();
            documentMetadataList.add(doc);
        }

        // 并行新增向量
        int batchCount = (documentMetadataList.size() + BATCH_SIZE - 1) / BATCH_SIZE;

        // 创建批处理任务
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < batchCount; i++) {
            int fromIndex = i * BATCH_SIZE;
            int toIndex = Math.min(fromIndex + BATCH_SIZE, documentMetadataList.size());
            List<Document> batch = documentMetadataList.subList(fromIndex, toIndex);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                vectorStore.add(batch);
            }, vectorStoreTaskExecutor);

            tasks.add(future);
        }

        // 等待所有任务完成
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 删除文章向量.
     *
     * @param articleId 文章ID
     */
    public void deleteVectorStore(final BigInteger articleId) {
        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        var filterExpression = builder.eq("articleId", articleId.toString()).build();

        // 异步删除向量
        CompletableFuture.runAsync(() -> {
            List<Document> results;
            try {
                results = vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query("*")
                                .topK(1000)
                                .filterExpression(filterExpression)
                                .build()
                );
            } catch (IllegalArgumentException e) {
                if (e.getMessage() != null && e.getMessage().contains("no graphql provider present")) {
                    return;
                }
                throw e;
            }

            if (!results.isEmpty()) {
                List<String> documentIdList = results.stream()
                        .map(Document::getId)
                        .toList();
                vectorStore.delete(documentIdList);
            }
        });

    }

    private Map<String, Object> buildMetadata(final Article article, final Integer chunkIndex) {
        return Map.of(
                "articleId", article.getId().toString(),
                "categoryId", Objects.isNull(article.getCategoryId()) ? "" : article.getCategoryId().toString(),
                "chunkIndex", chunkIndex,
                "title", article.getTitle(),
                "summary", StringUtils.isBlank(article.getSummary()) ? "" : article.getSummary(),
                "status", article.getStatus(),
                "uri", StringUtils.isBlank(article.getUri()) ? "" : article.getUri(),
                "createTime", article.getCreateTime(),
                "updateTime", article.getUpdateTime()
        );
    }
}
