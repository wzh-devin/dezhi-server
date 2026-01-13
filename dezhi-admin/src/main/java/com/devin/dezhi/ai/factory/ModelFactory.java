package com.devin.dezhi.ai.factory;

import com.alibaba.fastjson2.JSON;
import com.devin.dezhi.ai.provider.ModelProvider;
import com.devin.dezhi.constant.I18nConstant;
import com.devin.dezhi.constant.RedisKey;
import com.devin.dezhi.dao.ModelManagerDao;
import com.devin.dezhi.domain.entity.ModelManager;
import com.devin.dezhi.enums.ModelProviderEnum;
import com.devin.dezhi.enums.ModelStatusEnum;
import com.devin.dezhi.exception.ModelException;
import com.devin.dezhi.utils.RedisUtils;
import com.devin.dezhi.utils.i18n.I18nUtils;
import com.devin.dezhi.utils.r.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模型工厂.
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class ModelFactory {
    private final ModelManagerDao modelManagerDao;

    private final Map<ModelProviderEnum, ModelProvider> providers;

    public ModelFactory(final ModelManagerDao modelManagerDao, final List<ModelProvider> providerList) {
        this.modelManagerDao = modelManagerDao;
        this.providers = providerList.stream()
                .collect(Collectors.toMap(ModelProvider::getProviderType, p -> p));
    }

    /**
     * 获取ChatModel.
     *
     * @return ChatModel
     */
    public ChatModel getChatModel() {
        ModelManager config = getModelManager();
        ModelProvider provider = providers.get(ModelProviderEnum.valueOf(config.getProvider()));
        return provider.createChatModel(config);
    }

    private ModelManager getModelManager() {
        String activeModel = RedisUtils.get(RedisKey.generateRedisKey(RedisKey.MODEL_KEY));
        ModelManager config = JSON.parseObject(activeModel, ModelManager.class);
        if (Objects.isNull(config)) {
            config = modelManagerDao.getByStatus(ModelStatusEnum.ACTIVATED);
            if (Objects.isNull(config)) {
                throw new ModelException(ResultEnum.MODEL_ERROR, I18nUtils.getMessage(I18nConstant.MODEL_NOT_EXIST));
            }
            RedisUtils.set(RedisKey.generateRedisKey(RedisKey.MODEL_KEY), JSON.toJSONString(config));
        }
        return config;
    }
}
