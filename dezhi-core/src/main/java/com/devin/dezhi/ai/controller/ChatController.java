package com.devin.dezhi.ai.controller;

import com.devin.dezhi.ai.domain.req.ChatRequest;
import com.devin.dezhi.ai.service.RAGChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 2025/12/31 23:34.
 *
 * <p>
 * AI Chat
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Tag(name = "ChatController")
@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final RAGChatService ragChatService;

    /**
     * 流式聊天，返回OpenAI标准SSE格式.
     *
     * @param chatRequest 请求参数
     * @return OpenAI标准格式的SSE响应流
     */
    @Operation(summary = "流式聊天")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestBody final ChatRequest chatRequest
    ) {
        return ragChatService.chatStream(chatRequest)
                .map(
                        response -> ServerSentEvent.<String>builder()
                                .data(response.getData())
                                .build()
                );
    }
}
