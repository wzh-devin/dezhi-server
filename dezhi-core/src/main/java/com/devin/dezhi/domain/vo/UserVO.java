package com.devin.dezhi.domain.vo;

import java.util.Date;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/04 20:45:10.
 *
 * <p>
 *  用户表(User)VO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "用户表VO")
public class UserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -95631309048123169L;
    
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "更新时间")
    private Date updateTime;
    
}

