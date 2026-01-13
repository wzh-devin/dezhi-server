package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/05 19:20:07.
 *
 * <p>
 *  分类(Category)VO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "分类VO")
public class CategoryVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -14546672850446933L;
    
    @Schema(description = "主键id")
    private BigInteger id;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "更新时间")
    private Date updateTime;
    
}

