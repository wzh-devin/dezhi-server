package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.ai.service.VectorStoreService;
import com.devin.dezhi.constant.UrlRefixKey;
import com.devin.dezhi.dao.ArticleDao;
import com.devin.dezhi.dao.ArticleTagDao;
import com.devin.dezhi.enums.ArticleStatusEnum;
import com.devin.dezhi.handler.VectorTypeHandler;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
        VectorStoreService vectorStoreService = SpringContextHolder.getBean(VectorStoreService.class);

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
        vectorStoreService.updateVectorStore(this.id);
    }
}
