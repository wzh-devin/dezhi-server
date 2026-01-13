package com.devin.dezhi.controller.ai;

import com.devin.dezhi.ai.agent.BaseAgent;
import com.devin.dezhi.domain.vo.req.ChatRequest;
import com.devin.dezhi.domain.vo.resp.ChatResponse;
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
@Tag(name = "aiChat")
@RestController
@RequestMapping("/admin/ai/chat")
@RequiredArgsConstructor
public class AIChatController {

    private final BaseAgent baseAgent;

    /**
     * 流式聊天.
     *
     * @param chatRequest 请求参数
     * @return SSE响应流
     */
    @Operation(summary = "流式聊天")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatResponse>> chatStream(
            @RequestBody final ChatRequest chatRequest
    ) {
        return baseAgent.chatStream(chatRequest)
                .map(
                        response -> ServerSentEvent.<ChatResponse>builder()
                                .data(response)
                                .build()
                );
    }
}
