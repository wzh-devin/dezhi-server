package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2026/1/8 11:08.
 *
 * <p>
 *     模型类型
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ModelType {

    /**
     * 聊天模型.
     */
    CHAT,

    /**
     * 向量模型.
     */
    EMBEDDING
}
