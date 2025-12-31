package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import com.devin.dezhi.constant.UrlRefixKey;
import com.devin.dezhi.dao.ArticleDao;
import com.devin.dezhi.dao.ArticleTagDao;
import com.devin.dezhi.enums.ArticleStatusEnum;
import com.devin.dezhi.handler.VectorTypeHandler;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.util.CollectionUtils;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 *  文章表(Article)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_article")
public class Article implements Serializable {
    @Serial
    private static final long serialVersionUID = -68602038438930633L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id; 

    /**
     * 分类id.
     */
    @TableField("category_id")
    private BigInteger categoryId;      

    /**
     * 文章标题.
     */
    @TableField("title")
    private String title;      

    /**
     * 文章简介.
     */
    @TableField("summary")
    private String summary;      

    /**
     * 文章内容.
     */
    @TableField("content")
    private String content;      

    /**
     * 文章内容markdown.
     */
    @TableField("content_md")
    private String contentMd;      

    /**
     * 文章的uri地址.
     */
    @TableField("uri")
    private String uri;      

    /**
     * 文章状态（DRAFT,PUBLISHED,DELETED）.
     */
    @TableField("status")
    private String status;      

    /**
     * 是否置顶（0: 正常; 1: 置顶）.
     */
    @TableField("is_top")
    private Integer top;

    /**
     * 是否热门（0: 正常; 1: 热门）.
     */
    @TableField("is_hot")
    private Integer hot;

    /**
     * 创建时间.
     */
    @TableField("create_time")
    private Date createTime;      

    /**
     * 更新时间.
     */
    @TableField("update_time")
    private Date updateTime;      

    /**
     * 文章简介向量.
     */
    @TableField(value = "summary_embedding", typeHandler = VectorTypeHandler.class)
    private String summaryEmbedding;

    /**
     * 标签列表.
     */
    @TableField(exist = false)
    private List<BigInteger> tagIdList;

    /**
     * 初始化.
     */
    public void init() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    /**
     * 保存.
     * @return Article
     */
    public Article save() {
        ArticleDao articleDao = SpringContextHolder.getBean(ArticleDao.class);
        setId(IdGenerator.generateKey());
        setTitle("未命名文章");
        setUri(UrlRefixKey.ARTICLE_URL_PREFIX + IdGenerator.generateUUID());
        setStatus(ArticleStatusEnum.DRAFT.name());
        setHot(0);
        setTop(0);
        init();
        articleDao.save(this);
        return this;
    }

    /**
     * 更新.
     */
    public void update() {
        ArticleDao articleDao = SpringContextHolder.getBean(ArticleDao.class);
        ArticleTagDao articleTagDao = SpringContextHolder.getBean(ArticleTagDao.class);
        setUpdateTime(new Date());
        articleDao.updateById(this);
        // 删除标签关联
        articleTagDao.lambdaUpdate()
                .eq(ArticleTag::getArticleId, this.id)
                .remove();
        if (!CollectionUtils.isEmpty(tagIdList)) {
            // 新增标签关联
            List<ArticleTag> articleTagList = tagIdList.stream().map(tagId -> {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setId(IdGenerator.generateKey());
                articleTag.setArticleId(this.id);
                articleTag.setTagId(tagId);
                articleTag.init();
                return articleTag;
            }).toList();
            articleTagDao.saveBatch(articleTagList);
        }
        // 更新向量库
        updateVectorStore();
    }

    /**
     * 更新向量库.
     *
     */
    public void updateVectorStore() {
        VectorStore vectorStore = SpringContextHolder.getBean(VectorStore.class);
        TextSplitter textSplitter = SpringContextHolder.getBean(TextSplitter.class);
        ArticleDao articleDao = SpringContextHolder.getBean(ArticleDao.class);

        // 获取更新后的文章信息
        Article article = articleDao.getById(this.id);

        // 删除旧文本
        deleteVectorStore();

        // 新增文本
        String content = Objects.nonNull(article.contentMd)
                ? article.contentMd
                : article.content;
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
            Document document = chunks.get(i);
            Map<String, Object> metadata = buildMetadata(article, i);
            Document docs = Document.builder()
                    .id(IdGenerator.generateUUID())
                    .metadata(metadata)
                    .text(document.getText())
                    .build();
            documentMetadataList.add(docs);
        }
        // 增加到向量库中
        vectorStore.add(documentMetadataList);
    }

    /**
     * 删除文章.
     *
     */
    public void deleteVectorStore() {
        VectorStore vectorStore = SpringContextHolder.getBean(VectorStore.class);

        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        var filterExpression = builder.eq("articleId", id.toString()).build();

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
