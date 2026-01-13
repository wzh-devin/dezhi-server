package com.devin.dezhi.domain.vo;

import com.devin.dezhi.enums.ArticleStatusEnum;
import com.devin.dezhi.vo.CommonQueryVO;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 *  文章表(Article)QueryVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryVO extends CommonQueryVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -22890353347673358L;

    /**
     * 状态.
     */
    private ArticleStatusEnum status;

    /**
     * 分类ID.
     */
    private BigInteger categoryId;

    /**
     * 标签ID.
     */
    private BigInteger tagId;
    
    /**
     * 关键词.
     */
    private String keyword;
    
}

