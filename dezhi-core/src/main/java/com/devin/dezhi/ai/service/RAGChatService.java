package com.devin.dezhi.ai.service;

import com.devin.dezhi.ai.domain.req.ChatRequest;
import com.devin.dezhi.ai.domain.resp.OpenAiSseResponse;
import reactor.core.publisher.Flux;

/**
 * 2025/12/31 23:47.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RAGChatService {
    /**
     * 流式聊天，返回OpenAI标准SSE格式.
     *
     * @param chatRequest 请求参数
     * @return OpenAI标准格式的SSE响应流
     */
    Flux<OpenAiSseResponse> chatStream(ChatRequest chatRequest);
}
