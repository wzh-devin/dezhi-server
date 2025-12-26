package com.devin.dezhi.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.alibaba.fastjson2.JSON;
import com.devin.dezhi.constant.I18nConstant;
import com.devin.dezhi.utils.HttpUtils;
import com.devin.dezhi.utils.i18n.I18nUtils;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.ResultEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2025/6/12 16:16.
 *
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 处理业务异常.
     *
     * @param e 业务异常
     * @return 响应结果集
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleBusinessException(final BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return ApiResult.fail(e.getErrCode(), e.getErrMsg());
    }

    /**
     * 处理未登录异常.
     *
     * @param e 未登录异常
     * @return 响应结果集
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiResult<?> handleNotLoginException(final NotLoginException e) {
        log.error("登录异常: {}", e.getLocalizedMessage());
        if ("-4".equals(e.getType())) {
            return ApiResult.fail(ResultEnum.LOGIN_ERROR.getCode(), I18nUtils.getMessage(I18nConstant.HAVE_OTHER_LOGIN));
        }
        return ApiResult.fail(ResultEnum.LOGIN_ERROR, e.getMessage());
    }

    /**
     * 处理参数校验异常.
     *
     * @param e 参数校验异常
     * @return 响应结果集
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleMethodArgumentNotValidException(final BindException e) {
        String errorField = Objects.requireNonNull(e.getFieldError()).getField();
        String errorMsg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        Map<String, String> map = new HashMap<>();
        map.put(errorField, errorMsg);
        return ApiResult.fail(ResultEnum.PARAM_IS_INVALID, map);
    }

    /**
     * 数据库重复Key异常.
     *
     * @param e 数据库重复Key异常
     * @return 响应结果集
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleDuplicateKeyException(final DuplicateKeyException e) {
        Map<String, String> errMap = extractDuplicateKeyAndValue(e.getMessage());
        log.error("数据库重复Key异常: {}", errMap);
        if (!errMap.isEmpty()) {
            return ApiResult.fail(ResultEnum.DUPLICATE_KEY, errMap.get("key"));
        }
        return ApiResult.fail(ResultEnum.DUPLICATE_KEY);
    }

    /**
     * 处理校验异常.
     *
     * @param e 校验异常
     * @return 响应结果集
     */
    @ExceptionHandler(ValidateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleDuplicateKeyException(final ValidateException e) {
        log.error("校验异常: {}", e.getMessage());
        return ApiResult.fail(e.getErrCode(), e.getMessage());
    }

    /**
     * 处理异常.
     *
     * @param request 请求
     * @param e       异常
     * @return 响应结果集
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleException(final HttpServletRequest request, final Exception e) {
        // 请求方法
        String method = request.getMethod();
        // ip地址
        String ipAddr = HttpUtils.getRemoteHost(request);
        // 请求路径
        String requestUrl = request.getRequestURL().toString();
        // 请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        // body
        String bodyString = HttpUtils.getBodyString(request);

        log.error("请求源IP【{}】 \n请求URL【{} {}】 \n请求参数：{} \nbody：{}",
                ipAddr, method, requestUrl, JSON.toJSON(parameterMap), bodyString);
        log.error("Exception:", e);
        return ApiResult.fail(ResultEnum.ERROR, e.getMessage());
    }

    /**
     * 提取重复键和值.
     *
     * @param errorMessage 错误信息
     * @return 键和值
     */
    private static Map<String, String> extractDuplicateKeyAndValue(final String errorMessage) {
        Map<String, String> result = new HashMap<>();

        // 正则表达式与上面相同
        final Pattern pattern = Pattern.compile("Key \\((.*)\\)=\\((.*)\\) already exists");
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find() && matcher.groupCount() >= 2) {
            // group(1) 捕获第一个括号的内容 (key)
            String key = matcher.group(1);
            // group(2) 捕获第二个括号的内容 (value)
            String value = matcher.group(2);

            result.put("key", key);
            result.put("value", value);
        }

        return result;
    }
}
