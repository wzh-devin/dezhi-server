package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.enums.ArticleStatusEnum;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 *  文章表(Article)VO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "文章表VO")
public class ArticleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -60155675954129154L;
    
    @Schema(description = "主键id")
    private BigInteger id;
    
    @Schema(description = "分类id")
    private BigInteger categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "标签列表")
    private List<String> tagList;
    
    @Schema(description = "文章标题")
    private String title;
    
    @Schema(description = "文章简介")
    private String summary;
    
    @Schema(description = "文章内容")
    private String content;
    
    @Schema(description = "文章内容markdown")
    private String contentMd;
    
    @Schema(description = "文章的uri地址")
    private String uri;
    
    @Schema(description = "文章状态（DRAFT,PUBLISHED,DELETED）")
    private ArticleStatusEnum status;
    
    @Schema(description = "是否置顶（0: 正常; 1: 置顶）")
    private Integer top;
    
    @Schema(description = "是否热门（0: 正常; 1: 热门）")
    private Integer hot;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "更新时间")
    private Date updateTime;
    
}

