package com.devin.dezhi.ai.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Map;

/**
 * 2025/12/31 23:41.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Schema(description = "聊天请求")
public class ChatRequest {
    /**
     * 会话id.
     */
    @Schema(description = "会话id")
    private String sessionId;

    /**
     * 消息.
     */
    @Schema(description = "消息")
    private String message;

    /**
     * 搜索数量.
     */
    @Schema(description = "搜索数量")
    private Integer topK;

    /**
     * 过滤条件.
     */
    @Schema(description = "过滤条件")
    private Map<String, Object> filters;

    /**
     * 重新排序.
     */
    @Schema(description = "重新排序")
    private Boolean rerank = Boolean.FALSE;

    /**
     * 流式返回.
     */
    @Schema(description = "流式返回")
    private Boolean stream = Boolean.TRUE;
}
