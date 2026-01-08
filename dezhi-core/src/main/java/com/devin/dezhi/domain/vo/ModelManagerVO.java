package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.enums.ModelProviderEnum;
import com.devin.dezhi.enums.ModelType;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2026/01/08 11:07:24.
 *
 * <p>
 *  AI模型管理(ModelManager)VO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "AI模型管理VO")
public class ModelManagerVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 360307950274119572L;
    
    @Schema(description = "主键id")
    private BigInteger id;
    
    @Schema(description = "模型提供商")
    private ModelProviderEnum provider;
    
    @Schema(description = "模型名称")
    private String name;
    
    @Schema(description = "模型的base_url")
    private String baseUrl;
    
    @Schema(description = "模型的api_key")
    private String apiKey;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "更新时间")
    private Date updateTime;
    
    @Schema(description = "模型类型（CHAT, EMBEDDING）")
    private ModelType type;
    
}

