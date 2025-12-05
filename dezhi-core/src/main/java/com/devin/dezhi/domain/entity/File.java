package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.dao.FileDao;
import com.devin.dezhi.enums.StatusFlagEnum;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 *  文件素材表(File)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_file")
public class File implements Serializable {
    @Serial
    private static final long serialVersionUID = -29249592046540533L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id; 

    /**
     * 文件原始名称.
     */
    @TableField("original_name")
    private String originalName;      

    /**
     * 存储的文件名称.
     */
    @TableField("final_name")
    private String finalName;      

    /**
     * 存储桶名称.
     */
    @TableField("bucket_name")
    private String bucketName;      

    /**
     * 文件哈希值.
     */
    @TableField("hash")
    private String hash;      

    /**
     * 文件大小.
     */
    @TableField("size")
    private BigInteger size;      

    /**
     * 文件MIME类型.
     */
    @TableField("type")
    private String type;      

    /**
     * 文件扩展名.
     */
    @TableField("extension")
    private String extension;      

    /**
     * 文件存储类型.
     */
    @TableField("storage_type")
    private String storageType;      

    /**
     * 文件uri地址.
     */
    @TableField("uri")
    private String uri;      

    /**
     * 是否被删除（0: 正常; 1: 已删除）.
     */
    @TableField("is_deleted")
    private Integer deleted;

    /**
     * 文件状态（UPLOADING, FINISHED, FAILED）.
     */
    @TableField("status")
    private String status;      

    /**
     * 创建时间.
     */
    @TableField("create_time")
    private Date createTime;      

    /**
     * 更新时间.
     */
    @TableField("update_time")
    private Date updateTime;      

    /**
     * 初始化.
     */
    public void init() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    /**
     * 保存.
     */
    public void save() {
        FileDao fileDao = SpringContextHolder.getBean(FileDao.class);
        setId(IdGenerator.generateKey());
        init();
        setDeleted(StatusFlagEnum.NORMAL.getStatus());
        fileDao.save(this);
    }

    /**
     * 更新.
     */
    public void update() {
        FileDao fileDao = SpringContextHolder.getBean(FileDao.class);
        setUpdateTime(new Date());
        fileDao.updateById(this);
    }
}
