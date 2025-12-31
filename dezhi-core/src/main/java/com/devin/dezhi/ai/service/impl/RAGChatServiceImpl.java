package com.devin.dezhi.ai.service.impl;

import com.devin.dezhi.ai.domain.req.ChatRequest;
import com.devin.dezhi.ai.domain.resp.OpenAiChatCompletionChunk;
import com.devin.dezhi.ai.domain.resp.OpenAiSseResponse;
import com.devin.dezhi.ai.retrieval.RAGRetrievalService;
import com.devin.dezhi.ai.service.RAGChatService;
import com.devin.dezhi.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 2025/12/31 23:48.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RAGChatServiceImpl implements RAGChatService {

    private static final String SYSTEM_PROMPT = """
            你是一个专业的知识助手，基于提供的参考资料回答用户问题。
                  如果参考资料中没有相关信息，请诚实地说明你不知道。
                  回答时请：
                  1. 准确引用资料内容
                  2. 使用清晰的结构
                  3. 如有需要，提供相关建议
            """;

    private static final String CONTEXT_TEMPLATE = """
            以下是与问题相关的参考资料：
            
            %s
            
            请基于以上资料回答用户问题。如果资料中没有相关信息，请诚实说明。
            """;

    private static final String CHAT_COMPLETION_PREFIX = "chatcmpl-";

    private final RAGRetrievalService ragRetrievalService;

    private final OpenAiChatModel openAiChatModel;

    @Value("${spring.ai.openai.chat.options.model:gpt-4}")
    private String modelName;

    @Override
    public Flux<OpenAiSseResponse> chatStream(final ChatRequest chatRequest) {
        List<Document> retrievedDocuments = ragRetrievalService.retrieve(
                chatRequest.getMessage(),
                chatRequest.getTopK(),
                chatRequest.getFilters(),
                chatRequest.getRerank()
        );

        // 构建上下文
        String context = buildContext(retrievedDocuments);

        // 构建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(SYSTEM_PROMPT + context));
        messages.add(new UserMessage(chatRequest.getMessage()));

        // 生成唯一的响应ID
        String responseId = CHAT_COMPLETION_PREFIX + IdGenerator.generateUUID();
        AtomicBoolean isFirst = new AtomicBoolean(true);

        // 调用大模型输出并转换为OpenAI标准格式
        return openAiChatModel.stream(new Prompt(messages))
                .map(chatResponse -> convertToOpenAiFormat(chatResponse, responseId, isFirst))
                .concatWith(Flux.just(OpenAiSseResponse.done()));
    }

    /**
     * 将Spring AI的ChatResponse转换为OpenAI标准格式.
     *
     * @param chatResponse Spring AI响应
     * @param responseId   响应ID
     * @param isFirst      是否为第一个chunk
     * @return OpenAI标准格式响应
     */
    private OpenAiSseResponse convertToOpenAiFormat(
            final ChatResponse chatResponse,
            final String responseId,
            final AtomicBoolean isFirst
    ) {
        // 提取内容和finishReason
        String content = extractContent(chatResponse);
        String finishReason = extractFinishReason(chatResponse);

        OpenAiChatCompletionChunk chunk;

        if (isFirst.compareAndSet(true, false)) {
            // 第一个chunk，包含角色信息和内容
            chunk = OpenAiChatCompletionChunk.createFirstChunkWithContent(responseId, modelName, content);
        } else if (finishReason != null) {
            // 最后一个chunk，包含内容和finishReason
            chunk = OpenAiChatCompletionChunk.createFinalChunkWithContent(responseId, modelName, content, finishReason);
        } else {
            // 内容chunk
            chunk = OpenAiChatCompletionChunk.createContentChunk(responseId, modelName, content);
        }

        return OpenAiSseResponse.of(chunk);
    }

    /**
     * 从ChatResponse中提取内容.
     *
     * @param chatResponse Spring AI响应
     * @return 内容字符串
     */
    private String extractContent(final ChatResponse chatResponse) {
        if (chatResponse == null || chatResponse.getResults().isEmpty()) {
            return "";
        }
        var output = chatResponse.getResults().get(0).getOutput();
        return output.getText();
    }

    /**
     * 从ChatResponse中提取finish_reason.
     *
     * @param chatResponse Spring AI响应
     * @return finish_reason，如果未完成则返回null
     */
    private String extractFinishReason(final ChatResponse chatResponse) {
        if (chatResponse == null || chatResponse.getResults().isEmpty()) {
            return null;
        }
        var metadata = chatResponse.getResults().get(0).getMetadata();
        if (metadata.getFinishReason() != null) {
            return metadata.getFinishReason().toLowerCase();
        }
        return null;
    }

    private String buildContext(final List<Document> documents) {
        if (documents.isEmpty()) {
            return "";
        }

        StringBuilder contextBuilder = new StringBuilder();
        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            Map<String, Object> metadata = doc.getMetadata();

            contextBuilder.append(String.format(
                    "【文档 %d】\n标题：%s\n内容：%s\n来源：%s\n\n",
                    i + 1,
                    metadata.getOrDefault("title", "未知"),
                    doc.getText(),
                    metadata.getOrDefault("uri", "未知")
            ));
        }

        return String.format(CONTEXT_TEMPLATE, contextBuilder);
    }
}
