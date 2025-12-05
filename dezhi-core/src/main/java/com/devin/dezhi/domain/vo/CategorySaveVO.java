package com.devin.dezhi.domain.vo;

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
 *  分类(Category)SaveVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "分类SaveVO")
public class CategorySaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -87178773346382375L;
    
    @Schema(description = "分类名称")
    @NotBlank(message = "{required.parameter.error}")
    @Size(max = 100, message = "{parameter.size.error}")
    private String name;
    
}

