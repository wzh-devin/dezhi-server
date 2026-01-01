package com.devin.dezhi.ai.service.impl;

import com.devin.dezhi.ai.domain.req.ChatRequest;
import com.devin.dezhi.ai.domain.resp.ChatResponse;
import com.devin.dezhi.ai.retrieval.RAGRetrievalService;
import com.devin.dezhi.ai.service.RAGChatService;
import com.devin.dezhi.enums.ModelReplyTypeEnum;
import com.devin.dezhi.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final RAGRetrievalService ragRetrievalService;

    private final ChatModel chatModel;

    @Override
    public Flux<ChatResponse> chatStream(final ChatRequest chatRequest) {
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

        // 生成sessionId
        String sessionId = IdGenerator.generateUUID();
        return chatModel.stream(new Prompt(messages))
                .map(response -> {
                    String text = response.getResult().getOutput().getText();
                    return ChatResponse.builder()
                            .sessionId(sessionId)
                            .content(text)
                            .replyType(ModelReplyTypeEnum.TEXT)
                            .created(response.getMetadata().get("created"))
                            .build();
                }).concatWithValues(
                        ChatResponse.builder()
                                .sessionId(sessionId)
                                .content("")
                                .replyType(ModelReplyTypeEnum.DONE)
                                .build()
                );
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
