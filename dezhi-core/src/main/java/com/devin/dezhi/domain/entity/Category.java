package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.dao.CategoryDao;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2025/12/05 19:20:06.
 *
 * <p>
 *  分类(Category)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_category")
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = -29095082710657814L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id; 

    /**
     * 分类名称.
     */
    @TableField("name")
    private String name;      

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
        CategoryDao categoryDao = SpringContextHolder.getBean(CategoryDao.class);
        setId(IdGenerator.generateKey());
        init();
        categoryDao.save(this);
    }

    /**
     * 更新.
     */
    public void update() {
        CategoryDao categoryDao = SpringContextHolder.getBean(CategoryDao.class);
        setUpdateTime(new Date());
        categoryDao.updateById(this);
    }
}
