package com.devin.dezhi.domain.entity;

import java.math.BigInteger;
import java.util.Date;
import com.devin.dezhi.dao.ModelManagerDao;
import com.devin.dezhi.domain.vo.ModelManagerVO;
import com.devin.dezhi.enums.ModelStatusEnum;
import com.devin.dezhi.enums.ModelType;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.SpringContextHolder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 2026/01/08 11:07:24.
 *
 * <p>
 * AI模型管理(ModelManager)Entity层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@TableName(value = "dz_model_manager")
public class ModelManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 756870481775355730L;

    /**
     * 主键id.
     */
    @TableId
    private BigInteger id;

    /**
     * 模型提供商.
     */
    @TableField("provider")
    private String provider;

    /**
     * 模型名称.
     */
    @TableField("name")
    private String name;

    /**
     * 模型的base_url.
     */
    @TableField("base_url")
    private String baseUrl;

    /**
     * 模型的api_key.
     */
    @TableField("api_key")
    private String apiKey;

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
     * 模型类型（CHAT, EMBEDDING）.
     */
    @TableField("type")
    private String type;

    /**
     * 模型状态（STOP, ACTIVATED）.
     */
    @TableField("status")
    private String status;

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
        ModelManagerDao modelManagerDao = SpringContextHolder.getBean(ModelManagerDao.class);
        setId(IdGenerator.generateKey());
        init();
        modelManagerDao.save(this);
    }

    /**
     * 更新.
     */
    public void update() {
        ModelManagerDao modelManagerDao = SpringContextHolder.getBean(ModelManagerDao.class);

        // 停止其他模型，只保留一个在使用的模型即可
        modelManagerDao
                .lambdaUpdate()
                .eq(ModelManager::getType, ModelType.CHAT.name())
                .set(ModelManager::getStatus, ModelStatusEnum.STOP.name())
                .update();

        setUpdateTime(new Date());
        setStatus(ModelStatusEnum.ACTIVATED.name());
        modelManagerDao.updateById(this);
    }

    /**
     * 列表.
     *
     * @return 列表
     */
    public static List<ModelManagerVO> list() {
        ModelManagerDao modelManagerDao = SpringContextHolder.getBean(ModelManagerDao.class);
        List<ModelManager> modelManagerList = modelManagerDao.list();
        return BeanCopyUtils.copyList(modelManagerList, ModelManagerVO.class);
    }
}
