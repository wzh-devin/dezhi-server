package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2025/12/6 01:06.
 *
 * <p>
 *     状态标志枚举类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum StatusFlagEnum {

    DELETED(0, "正常"),
    NORMAL(1, "已删除");

    private final Integer status;

    private final String description;
}
