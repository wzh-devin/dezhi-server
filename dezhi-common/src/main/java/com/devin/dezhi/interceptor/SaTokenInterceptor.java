package com.devin.dezhi.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 2026/1/1 02:09.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class SaTokenInterceptor extends SaInterceptor {

    public SaTokenInterceptor() {
        super(handle -> StpUtil.checkLogin());
    }

    /**
     * 拦截器处理.
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 拦截结果
     * @throws Exception 拦截异常
     */
    @Override
    public boolean preHandle(
            @NotNull final HttpServletRequest request,
            @NotNull final HttpServletResponse response,
            @NotNull final Object handler
    ) throws Exception {
        // 异步派发请求跳过验证，避免 SSE 流式请求上下文丢失问题
        if (request.getDispatcherType() == DispatcherType.ASYNC) {
            return true;
        }
        return super.preHandle(request, response, handler);
    }
}
