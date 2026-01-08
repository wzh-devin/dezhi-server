package com.devin.dezhi.exception;

import com.devin.dezhi.utils.r.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 2026/1/8 14:14.
 *
 * <p>
 *     模型异常
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelException extends RuntimeException {

    private final Integer errCode;

    private final String errMsg;

    /**
     * 构造函数.
     *
     * @param message 错误信息
     */
    public ModelException(final String message) {
        super(message);
        this.errCode = ResultEnum.MODEL_ERROR.getCode();
        this.errMsg = message;
    }

    public ModelException(final Integer errCode, final String message) {
        super(message);
        this.errCode = errCode;
        this.errMsg = message;
    }

    public ModelException(final ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.errCode = resultEnum.getCode();
        this.errMsg = resultEnum.getMessage();
    }

    public ModelException(final ResultEnum resultEnum, final String message) {
        super(resultEnum.getMessage());
        this.errCode = resultEnum.getCode();
        this.errMsg = message;
    }
}
