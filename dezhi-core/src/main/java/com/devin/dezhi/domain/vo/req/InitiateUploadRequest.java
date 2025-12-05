package com.devin.dezhi.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 2025/12/6 00:14.
 *
 * <p>
 *     初始化上传
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "初始化上传")
public class InitiateUploadRequest {

    @Schema(description = "原始文件名")
    @NotBlank(message = "{required.parameter.error}")
    private String originalName;

    @Schema(description = "文件hash值")
    @NotBlank(message = "{required.parameter.error}")
    private String fileHash;

    @Schema(description = "文件大小")
    @NotBlank(message = "{required.parameter.error}")
    private Long fileSize;

    @Schema(description = "分片总数")
    @NotBlank(message = "{required.parameter.error}")
    private Integer totalChunks;
}
