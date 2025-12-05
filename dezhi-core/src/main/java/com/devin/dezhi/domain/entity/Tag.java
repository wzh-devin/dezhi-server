package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.dao.TagDao;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2025/12/05 19:54:12.
 *
 * <p>
 *  标签(Tag)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_tag")
public class Tag implements Serializable {
    @Serial
    private static final long serialVersionUID = -69427664775413465L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id; 

    /**
     * 标签名称.
     */
    @TableField("name")
    private String name;      

    /**
     * 标签颜色.
     */
    @TableField("color")
    private String color;      

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
        TagDao tagDao = SpringContextHolder.getBean(TagDao.class);
        setId(IdGenerator.generateKey());
        init();
        tagDao.save(this);
    }

    /**
     * 更新.
     */
    public void update() {
        TagDao tagDao = SpringContextHolder.getBean(TagDao.class);
        setUpdateTime(new Date());
        tagDao.updateById(this);
    }
}
