package com.devin.dezhi.service.impl;

import com.devin.dezhi.dao.ArticleDao;
import com.devin.dezhi.domain.entity.Article;
import com.devin.dezhi.service.TokenTextSplitter;
import com.devin.dezhi.service.VectorStoreService;
import com.devin.dezhi.utils.IdGenerator;
import com.knuddels.jtokkit.api.ModelType;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

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

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    private final ArticleDao articleDao;

    private final ThreadPoolTaskExecutor embeddingStoreTaskExecutor;

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
        }, embeddingStoreTaskExecutor).join();
    }

    @Override
    public List<TextSegment> retrieve(
            final String message,
            final Integer topK
    ) {
        Embedding queryEmbedding = embeddingModel.embed(message).content();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(topK)
                        .minScore(0.7)
                        .build()
        );

        return searchResult.matches().stream()
                .map(EmbeddingMatch::embedded)
                .toList();
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
        Document originalDocument = Document.from(
                content,
                buildMetadata(article, 0)
        );
        // 切分文档
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .maxTokensPerSegment(1024)
                .minTokensPerSegment(100)
                .maxSegments(25)
                .tokenizer(new OpenAiTokenizer(ModelType.GPT_4O.getName()))
                .build();

        List<TextSegment> chunks = tokenTextSplitter.split(originalDocument);

        List<TextSegment> segmentList = IntStream.range(0, chunks.size())
                .mapToObj(index -> {
                    TextSegment textSegment = chunks.get(index);
                    return TextSegment.textSegment(
                            textSegment.text(),
                            buildMetadata(article, index)
                    );
                }).toList();

        // 并行新增向量
        int batchCount = (segmentList.size() + BATCH_SIZE - 1) / BATCH_SIZE;

        // 创建批处理任务
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < batchCount; i++) {
            int fromIndex = i * BATCH_SIZE;
            int toIndex = Math.min(fromIndex + BATCH_SIZE, segmentList.size());
            List<String> idList = new ArrayList<>();
            List<TextSegment> batch = segmentList.subList(fromIndex, toIndex)
                    .stream()
                    .peek(
                            textSegment -> idList.add(IdGenerator.generateUUID())
                    ).toList();

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                List<Embedding> embeddingList = embeddingModel.embedAll(batch).content();
                embeddingStore.addAll(idList, embeddingList, batch);
            }, embeddingStoreTaskExecutor);
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
        Filter filter = new IsEqualTo("articleId", articleId);

        // 异步删除向量
        CompletableFuture.runAsync(() -> {
            List<EmbeddingMatch<TextSegment>> matches;
            try {
                EmbeddingSearchResult<TextSegment> results = embeddingStore.search(
                        EmbeddingSearchRequest.builder()
                                .queryEmbedding(Embedding.from(new float[0]))
                                .filter(filter)
                                .maxResults(1000)
                                .minScore(0.0)
                                .build()
                );
                matches = results.matches();
            } catch (IllegalArgumentException e) {
                if (e.getMessage() != null && e.getMessage().contains("no graphql provider present")) {
                    return;
                }
                throw e;
            }

            if (!matches.isEmpty()) {
                List<String> embeddingIdList = matches.stream()
                        .map(EmbeddingMatch::embeddingId)
                        .toList();
                embeddingStore.removeAll(embeddingIdList);
            }
        }, embeddingStoreTaskExecutor);

    }

    private Metadata buildMetadata(final Article article, final Integer chunkIndex) {
        Metadata metadata = new Metadata();
        metadata.putAll(
                Map.of(
                        "articleId", article.getId().toString(),
                        "categoryId", Objects.isNull(article.getCategoryId()) ? "" : article.getCategoryId().toString(),
                        "chunkIndex", chunkIndex,
                        "title", article.getTitle(),
                        "summary", StringUtils.isBlank(article.getSummary()) ? "" : article.getSummary(),
                        "status", article.getStatus(),
                        "uri", StringUtils.isBlank(article.getUri()) ? "" : article.getUri(),
                        "createTime", article.getCreateTime().toString(),
                        "updateTime", article.getUpdateTime().toString()
                )
        );
        return metadata;
    }
}
