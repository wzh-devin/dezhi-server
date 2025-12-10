package com.devin.dezhi.domain.vo;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.enums.FileTypeEnum;
import com.devin.dezhi.enums.FileUploadStatusEnum;
import com.devin.dezhi.enums.StatusFlagEnum;
import com.devin.dezhi.enums.StorageTypeEnum;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 *  文件素材表(File)VO
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "文件素材表VO")
public class FileVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 805157857911347443L;
    
    @Schema(description = "主键id")
    private BigInteger id;
    
    @Schema(description = "文件原始名称")
    private String originalName;
    
    @Schema(description = "存储的文件名称")
    private String finalName;
    
    @Schema(description = "存储桶名称")
    private String bucketName;
    
    @Schema(description = "文件哈希值")
    private String hash;
    
    @Schema(description = "文件大小")
    private BigInteger size;
    
    @Schema(description = "文件MIME类型")
    private String mimeType;
    
    @Schema(description = "文件类型")
    private FileTypeEnum type;
    
    @Schema(description = "文件存储类型")
    private StorageTypeEnum storageType;
    
    @Schema(description = "文件url地址")
    private String url;
    
    @Schema(description = "是否被删除（0: 正常; 1: 已删除）")
    private StatusFlagEnum deleted;
    
    @Schema(description = "文件状态（UPLOADING, FINISHED, FAILED）")
    private FileUploadStatusEnum status;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "更新时间")
    private Date updateTime;
    
}

