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
 * 2026/01/08 17:59:42.
 *
 * <p>
 *  对话消息表(Message)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_message")
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 308975898235951586L;
    
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
     * 消息角色（USER, SYSTEM, ASSISTANT, TOOL）.
     */
    @TableField("role")
    private String role;      

    /**
     * 消息内容.
     */
    @TableField("content")
    private String content;      

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
