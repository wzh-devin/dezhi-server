package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.dao.SessionDao;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2026/01/08 17:59:42.
 *
 * <p>
 *  用户会话表(Session)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_session")
public class Session implements Serializable {
    @Serial
    private static final long serialVersionUID = -46087453900038013L;
    
    /**
     * 主键id.
     */
    @TableId
    private BigInteger id;

    /**
     * 会话id.
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 会话标题.
     */
    @TableField("title")
    private String title;      

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
        SessionDao sessionDao = SpringContextHolder.getBean(SessionDao.class);
        setId(IdGenerator.generateKey());
        setSessionId(IdGenerator.generateUUID());
        init();
        sessionDao.save(this);
    }
}
