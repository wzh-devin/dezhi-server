package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.dao.UserDao;
import com.devin.dezhi.utils.EncryptUtils;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2025/12/04 20:45:05.
 *
 * <p>
 *  用户表(User)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -16546835987314626L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id; 

    /**
     * 用户名.
     */
    @TableField("username")
    private String username;      

    /**
     * 密码.
     */
    @TableField("password")
    private String password;      

    /**
     * 邮箱.
     */
    @TableField("email")
    private String email;      

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
     * 更新.
     */
    public void update() {
        UserDao userDao = SpringContextHolder.getBean(UserDao.class);
        setPassword(EncryptUtils.bcrypt(this.password));
        setUpdateTime(new Date());
        userDao.updateById(this);
    }

    /**
     * 保存.
     */
    public void save() {
        UserDao userDao = SpringContextHolder.getBean(UserDao.class);
        setId(IdGenerator.generateKey());
        init();
        userDao.save(this);
    }
}
