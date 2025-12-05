package com.devin.dezhi.domain.vo;

import com.devin.dezhi.vo.CommonQueryVO;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

/**
 * 2025/12/05 19:54:12.
 *
 * <p>
 *  标签(Tag)QueryVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagQueryVO extends CommonQueryVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -67709033785419632L;
    
    /**
     * 关键词.
     */
    private String keyword;
    
}

