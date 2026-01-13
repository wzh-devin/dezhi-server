package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/05 19:20:07.
 *
 * <p>
 *  分类(Category)UpdateVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "分类UpdateVO")
public class CategoryUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 937099362539150619L;

    @Schema(description = "主键id")
    private BigInteger id; 
    
    @Schema(description = "分类名称")
    @NotBlank(message = "{required.parameter.error}")
    @Size(max = 100, message = "{max.parameter.error}")
    private String name;
    
}

