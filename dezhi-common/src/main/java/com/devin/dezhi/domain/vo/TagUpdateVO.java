package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/05 19:54:12.
 *
 * <p>
 *  标签(Tag)UpdateVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "标签UpdateVO")
public class TagUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -11179950413837627L;

    @Schema(description = "主键id")
    private BigInteger id; 
    
    @Schema(description = "标签名称")
    @NotBlank(message = "{required.parameter.error}")
    @Size(max = 100, message = "{max.parameter.error}")
    private String name;
    
    @Schema(description = "标签颜色")
    @NotBlank(message = "{required.parameter.error}")
    @Size(max = 255, message = "{max.parameter.error}")
    private String color;
    
}

