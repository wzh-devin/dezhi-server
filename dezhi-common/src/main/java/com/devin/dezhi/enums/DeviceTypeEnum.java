package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2025/12/26 19:00.
 *
 * <p>
 *     设备类型枚举
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {
    /**
     * PC端设备.
     */
    PC,

    /**
     * 移动设备.
     */
    PHONE
}
