package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/04 20:45:10.
 *
 * <p>
 *  用户表(User)UpdateVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "用户表UpdateVO")
public class UserUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -91880805871342580L;

    @Schema(description = "主键id")
    private BigInteger id;
    
    @Schema(description = "用户名")
    @NotBlank(message = "{required.parameter.error}")
    private String username;
    
    @Schema(description = "密码")
    @NotBlank(message = "{required.parameter.error}")
    @Size(min = 6, max = 16, message = "{param.size.error}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{password.error}")
    private String password;
    
    @Schema(description = "邮箱")
    @NotBlank(message = "{required.parameter.error}")
    @Email(message = "{email.error}")
    private String email;
    
}

