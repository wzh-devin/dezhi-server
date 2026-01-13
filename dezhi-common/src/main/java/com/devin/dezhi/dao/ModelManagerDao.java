package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.ModelManager;
import com.devin.dezhi.enums.ModelStatusEnum;
import com.devin.dezhi.enums.ModelType;
import com.devin.dezhi.mapper.ModelManagerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2026/01/08 11:07:24.
 *
 * <p>
 *  AI模型管理(ModelManager)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModelManagerDao extends ServiceImpl<ModelManagerMapper, ModelManager> {

    /**
     * 根据状态获取模型.
     * @param modelStatusEnum 模型状态枚举
     * @return 模型
     */
    public ModelManager getByStatus(final ModelStatusEnum modelStatusEnum) {
        return lambdaQuery()
                .eq(ModelManager::getType, ModelType.CHAT.name())
                .eq(ModelManager::getStatus, modelStatusEnum.name())
                .one();
    }
}
