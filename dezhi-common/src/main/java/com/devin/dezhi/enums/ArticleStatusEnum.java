package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2025/12/27 02:02.
 *
 * <p>
 *     文章状态枚举
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {
    /**
     * 正常.
     */
    NORMAL,
    /**
     * 草稿.
     */
    DRAFT,

    /**
     * 已发布.
     */
    PUBLISHED,

    /**
     * 已删除.
     */
    DELETED
}
