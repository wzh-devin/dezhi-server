package com.devin.dezhi.domain.vo;

import com.devin.dezhi.vo.CommonQueryVO;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

/**
 * 2025/12/05 19:20:07.
 *
 * <p>
 *  分类(Category)QueryVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQueryVO extends CommonQueryVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -30644249261286041L;
    
    /**
     * 关键词.
     */
    private String keyword;
    
}

