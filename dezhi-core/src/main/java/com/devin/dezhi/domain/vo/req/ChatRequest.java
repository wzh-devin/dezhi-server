package com.devin.dezhi.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
     * 流式返回.
     */
    @Schema(description = "流式返回")
    private Boolean stream = Boolean.TRUE;
}
