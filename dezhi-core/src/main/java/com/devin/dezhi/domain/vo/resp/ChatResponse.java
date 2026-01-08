package com.devin.dezhi.domain.vo.resp;

import com.devin.dezhi.enums.ModelReplyTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 2025/12/31 23:38.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Builder
public class ChatResponse {
    /**
     * 会话id.
     */
    private String sessionId;

    /**
     * 响应内容.
     */
    private String content;

    /**
     * 响应类型.
     */
    private ModelReplyTypeEnum replyType;

    /**
     * 创建时间.
     */
    private Long created;
}
