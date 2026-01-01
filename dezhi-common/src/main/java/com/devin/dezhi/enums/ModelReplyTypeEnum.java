package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2026/1/1 16:07.
 *
 * <p>
 *     模型回复类型枚举
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ModelReplyTypeEnum {

    /**
     * 文本.
     */
    TEXT,

    /**
     * 完成.
     */
    DONE,

    /**
     * 错误.
     */
    ERROR;
}
