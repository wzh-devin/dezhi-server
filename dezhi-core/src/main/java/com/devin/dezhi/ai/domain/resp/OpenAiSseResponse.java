package com.devin.dezhi.ai.domain.resp;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;

/**
 * 2025/12/31.
 *
 * <p>
 *     OpenAI标准SSE响应包装类
 *     自动序列化为"data: {json}"格式
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class OpenAiSseResponse {

    /**
     * SSE结束标记.
     */
    public static final String DONE = "[DONE]";

    /**
     * 数据内容.
     */
    private final String data;

    /**
     * 是否为结束标记.
     */
    private final boolean done;

    /**
     * 构造函数.
     *
     * @param chunk OpenAI格式的chunk
     */
    public OpenAiSseResponse(final OpenAiChatCompletionChunk chunk) {
        this.data = JSON.toJSONString(chunk);
        this.done = false;
    }

    /**
     * 私有构造函数用于创建结束响应.
     *
     * @param data 数据
     * @param done 是否结束
     */
    private OpenAiSseResponse(final String data, final boolean done) {
        this.data = data;
        this.done = done;
    }

    /**
     * 创建结束响应.
     *
     * @return 结束响应
     */
    public static OpenAiSseResponse done() {
        return new OpenAiSseResponse(DONE, true);
    }

    /**
     * 创建数据响应.
     *
     * @param chunk chunk数据
     * @return SSE响应
     */
    public static OpenAiSseResponse of(final OpenAiChatCompletionChunk chunk) {
        return new OpenAiSseResponse(chunk);
    }

    /**
     * 获取SSE格式的数据字符串.
     *
     * @return SSE格式字符串 "data: {json}" 或 "data: [DONE]"
     */
    @Override
    public String toString() {
        return data;
    }
}

