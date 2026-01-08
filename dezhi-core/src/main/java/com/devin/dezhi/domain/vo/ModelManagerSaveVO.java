package com.devin.dezhi.domain.vo;

import com.devin.dezhi.enums.ModelProviderEnum;
import com.devin.dezhi.enums.ModelType;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2026/01/08 11:07:24.
 *
 * <p>
 *  AI模型管理(ModelManager)SaveVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "AI模型管理SaveVO")
public class ModelManagerSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 291997446772632131L;
    
    @Schema(description = "模型提供商")
    @NotBlank(message = "{required.parameter.error}")
    private ModelProviderEnum provider;
    
    @Schema(description = "模型名称")
    @NotBlank(message = "{required.parameter.error}")
    private String name;
    
    @Schema(description = "模型的base_url")
    @NotBlank(message = "{required.parameter.error}")
    private String baseUrl;
    
    @Schema(description = "模型的api_key")
    @NotBlank(message = "{required.parameter.error}")
    private String apiKey;
    
    @Schema(description = "模型类型（CHAT, EMBEDDING）")
    @NotBlank(message = "{required.parameter.error}")
    private ModelType type;
    
}

