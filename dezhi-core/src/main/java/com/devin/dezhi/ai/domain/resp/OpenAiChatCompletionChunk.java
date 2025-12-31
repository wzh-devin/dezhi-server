package com.devin.dezhi.ai.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 2025/12/31.
 *
 * <p>
 * OpenAI标准Chat Completion Chunk响应格式
 * 用于SSE流式响应，符合OpenAI API规范
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @see <a href="https://platform.openai.com/docs/api-reference/chat/streaming">OpenAI Streaming</a>
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenAiChatCompletionChunk {

    /**
     * 响应唯一标识符.
     */
    private String id;

    /**
     * 对象类型，固定为"chat.completion.chunk".
     */
    private String object;

    /**
     * 响应创建时间戳.
     */
    private Long created;

    /**
     * 模型名称.
     */
    private String model;

    /**
     * 系统指纹.
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * 选项列表.
     */
    private List<Choice> choices;

    /**
     * 使用量统计（仅在最后一个chunk中返回）.
     */
    private Usage usage;

    /**
     * 创建第一个chunk（包含角色信息）.
     *
     * @param id    响应ID
     * @param model 模型名称
     * @return 第一个chunk
     */
    public static OpenAiChatCompletionChunk createFirstChunk(final String id, final String model) {
        return OpenAiChatCompletionChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(List.of(
                        Choice.builder()
                                .index(0)
                                .delta(Delta.builder().role("assistant").build())
                                .finishReason(null)
                                .build()
                ))
                .build();
    }

    /**
     * 创建第一个chunk（包含角色信息和内容）.
     *
     * @param id      响应ID
     * @param model   模型名称
     * @param content 内容片段
     * @return 第一个chunk
     */
    public static OpenAiChatCompletionChunk createFirstChunkWithContent(
            final String id,
            final String model,
            final String content
    ) {
        return OpenAiChatCompletionChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(List.of(
                        Choice.builder()
                                .index(0)
                                .delta(Delta.builder().role("assistant").content(content).build())
                                .finishReason(null)
                                .build()
                ))
                .build();
    }

    /**
     * 创建内容chunk.
     *
     * @param id      响应ID
     * @param model   模型名称
     * @param content 内容片段
     * @return 内容chunk
     */
    public static OpenAiChatCompletionChunk createContentChunk(final String id, final String model, final String content) {
        return OpenAiChatCompletionChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(List.of(
                        Choice.builder()
                                .index(0)
                                .delta(Delta.builder().content(content).build())
                                .finishReason(null)
                                .build()
                ))
                .build();
    }

    /**
     * 创建结束chunk.
     *
     * @param id           响应ID
     * @param model        模型名称
     * @param finishReason 结束原因
     * @return 结束chunk
     */
    public static OpenAiChatCompletionChunk createFinalChunk(final String id, final String model, final String finishReason) {
        return OpenAiChatCompletionChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(List.of(
                        Choice.builder()
                                .index(0)
                                .delta(Delta.builder().build())
                                .finishReason(finishReason != null ? finishReason : "stop")
                                .build()
                ))
                .build();
    }

    /**
     * 创建结束chunk（包含内容）.
     *
     * @param id           响应ID
     * @param model        模型名称
     * @param content      内容片段
     * @param finishReason 结束原因
     * @return 结束chunk
     */
    public static OpenAiChatCompletionChunk createFinalChunkWithContent(
            final String id,
            final String model,
            final String content,
            final String finishReason
    ) {
        return OpenAiChatCompletionChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(List.of(
                        Choice.builder()
                                .index(0)
                                .delta(Delta.builder().content(content).build())
                                .finishReason(finishReason != null ? finishReason : "stop")
                                .build()
                ))
                .build();
    }

    /**
     * 选项.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Choice {

        /**
         * 选项索引.
         */
        private Integer index;

        /**
         * 增量内容.
         */
        private Delta delta;

        /**
         * 日志概率.
         */
        private Object logprobs;

        /**
         * 完成原因.
         * null: 生成中
         * stop: 正常结束
         * length: 达到最大长度
         * content_filter: 内容过滤
         */
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    /**
     * 增量内容.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Delta {

        /**
         * 角色（仅在第一个chunk中返回）.
         */
        private String role;

        /**
         * 内容片段.
         */
        private String content;

        /**
         * 工具调用.
         */
        @JsonProperty("tool_calls")
        private List<Object> toolCalls;
    }

    /**
     * 使用量统计.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Usage {

        /**
         * 输入token数量.
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        /**
         * 输出token数量.
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        /**
         * 总token数量.
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}

