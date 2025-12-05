package com.devin.dezhi.domain.vo;

import com.devin.dezhi.enums.StatusFlagEnum;
import com.devin.dezhi.vo.CommonQueryVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 *  文件素材表(File)QueryVO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileQueryVO extends CommonQueryVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -54570160647279444L;

    /**
     * 存储类型.
     */
    @Schema(description = "存储类型")
    private String storageType;

    /**
     * 文件类型.
     */
    @Schema(description = "文件类型")
    private String type;

    /**
     * 删除状态.
     */
    @Schema(description = "删除状态")
    @NotNull(message = "{required.parameter.error}")
    private StatusFlagEnum deleted;
    
    /**
     * 文件名关键词.
     */
    @Schema(description = "文件名关键词")
    private String keyword;
}

