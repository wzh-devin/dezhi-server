package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2026/1/8 15:20.
 *
 * <p>
 *     模型状态枚举类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ModelStatusEnum {

    /**
     * 停用.
     */
    STOP,

    /**
     * 启用.
     */
    ACTIVATED
}
