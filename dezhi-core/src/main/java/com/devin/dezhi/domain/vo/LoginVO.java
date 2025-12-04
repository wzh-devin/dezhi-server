package com.devin.dezhi.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 2025/12/4 22:35.
 *
 * <p>
 *     登录参数
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "登录参数")
public class LoginVO {
    @Schema(description = "用户名")
    @NotBlank(message = "{required.parameter.error}")
    private String username;

    @Schema(description = "密码")
    @Size(min = 6, max = 16, message = "{param.size.error}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{password.error}")
    private String password;
}
