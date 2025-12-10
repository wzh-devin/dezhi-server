package com.devin.dezhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2025/12/9 00:37.
 *
 * <p>
 * 文件类型枚举
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    /**
     * 图片.
     */
    IMAGE,

    /**
     * PDF.
     */
    PDF,

    /**
     * 压缩包.
     */
    ZIP

}
