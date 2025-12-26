package com.devin.dezhi.utils.r;

import com.devin.dezhi.constant.I18nConstant;
import com.devin.dezhi.utils.i18n.I18nUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2025/6/13 19:39.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    /* 成功状态码 */
    SUCCESS(0, I18nConstant.SUCCESS),
    /* 未知错误 */
    ERROR(-1, I18nConstant.ERROR),
    /* 客户端错误: 1001-1999 */
    /* 业务错误: 4001-4999 */
    BUSINESS_ERROR(4000, I18nConstant.BUSINESS_ERROR),
    LOGIN_ERROR(4001, I18nConstant.USER_NOT_LOGIN),
    DUPLICATE_KEY(4002, I18nConstant.DUPLICATE_KEY),
    PARAM_IS_INVALID(4003, I18nConstant.PARAM_IS_INVALID),
    FILE_ERROR(4004, I18nConstant.FILE_ERROR),
    SESSION_NOT_FOUND(4005, I18nConstant.SESSION_NOT_FOUND),
    UPLOAD_ERROR(4006, I18nConstant.UPLOAD_ERROR), UPLOAD_NOT_FINISHED(4007, I18nConstant.UPLOAD_NOT_FINISHED);

    private final Integer code;

    private final String messageKey;

    /**
     * 获取国际化消息.
     * @return 消息内容
     */
    public String getMessage() {
        return I18nUtils.getMessage(messageKey);
    }
}
