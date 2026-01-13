package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import com.devin.dezhi.enums.ArticleStatusEnum;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 *  文章表(Article)UpdateVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "文章表UpdateVO")
public class ArticleUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 233289044303163129L;

    @Schema(description = "主键id")
    private BigInteger id; 
    
    @Schema(description = "分类id")
    @NotNull(message = "{required.parameter.error}")
    private BigInteger categoryId;

    @Schema(description = "标签id列表")
    private List<BigInteger> tagIdList;
    
    @Schema(description = "文章标题")
    @NotBlank(message = "{required.parameter.error}")
    private String title;
    
    @Schema(description = "文章简介")
    private String summary;
    
    @Schema(description = "文章内容")
    private String content;
    
    @Schema(description = "文章内容markdown")
    private String contentMd;
    
    @Schema(description = "文章状态（DRAFT,PUBLISHED,DELETED）")
    @NotBlank(message = "{required.parameter.error}")
    private ArticleStatusEnum status;
    
    @Schema(description = "是否置顶（0: 正常; 1: 置顶）")
    @NotNull(message = "{required.parameter.error}")
    private Integer top;
    
    @Schema(description = "是否热门（0: 正常; 1: 热门）")
    @NotNull(message = "{required.parameter.error}")
    private Integer hot;
    
}

