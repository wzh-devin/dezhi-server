package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2025/12/26 22:07:50.
 *
 * <p>
 *  文章-文件关联表(ArticleFile)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_article_file")
public class ArticleFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 634199171503799901L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id; 

    /**
     * 文章id.
     */
    @TableField("article_id")
    private BigInteger articleId;      

    /**
     * 文件id.
     */
    @TableField("file_id")
    private BigInteger fileId;      

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
}
