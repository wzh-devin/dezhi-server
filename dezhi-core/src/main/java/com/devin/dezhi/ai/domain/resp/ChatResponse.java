package com.devin.dezhi.ai.domain.resp;

import lombok.Builder;
import lombok.Data;
import java.util.List;

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
    private String message;

    /**
     * 响应来源.
     */
    private List<Source> sourceList;

    /**
     * 检索时间.
     */
    private Long retrievalTimeMs;

    @Data
    @Builder
    public static class Source {
        /**
         * 文章id.
         */
        private String articleId;

        /**
         * 文章标题.
         */
        private String title;

        /**
         * 文章uri.
         */
        private String uri;

        /**
         * 文章片段.
         */
        private String snippet;
    }
}
