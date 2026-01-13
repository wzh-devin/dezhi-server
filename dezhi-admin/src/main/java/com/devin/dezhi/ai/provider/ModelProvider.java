package com.devin.dezhi.ai.provider;

import com.devin.dezhi.domain.entity.ModelManager;
import com.devin.dezhi.enums.ModelProviderEnum;
import org.springframework.ai.chat.model.ChatModel;

/**
 * 模型提供者接口.
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ModelProvider {

    /**
     * 创建ChatModel.
     *
     * @param config 模型配置
     * @return ChatModel
     */
    ChatModel createChatModel(ModelManager config);

    /**
     * 获取提供者类型.
     *
     * @return ModelProviderEnum
     */
    ModelProviderEnum getProviderType();
}
