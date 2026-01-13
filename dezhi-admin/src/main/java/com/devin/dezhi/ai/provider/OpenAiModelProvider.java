package com.devin.dezhi.ai.provider;

import com.devin.dezhi.domain.entity.ModelManager;
import com.devin.dezhi.enums.ModelProviderEnum;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;

/**
 * OpenAI模型提供者.
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class OpenAiModelProvider implements ModelProvider {

    @Override
    public ChatModel createChatModel(final ModelManager config) {
        OpenAiApi api = OpenAiApi.builder()
                .baseUrl(config.getBaseUrl())
                .apiKey(config.getApiKey())
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(api)
                .defaultOptions(
                        OpenAiChatOptions.builder()
                                .model(config.getName())
                                .temperature(0.7)
                                .build()
                )
                .build();
    }

    @Override
    public ModelProviderEnum getProviderType() {
        return ModelProviderEnum.OPENAI;
    }
}
