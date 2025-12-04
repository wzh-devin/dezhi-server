package com.devin.dezhi.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

/**
 * 2025/12/4 19:25.
 *
 * <p>
 * Mvc配置类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 配置跨域请求.
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // 配置swagger排除路径
        List<String> swaggerExcludePathPatterns = List.of(
                "/doc.html",
                "/webjars/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/favicon.ico",
                "/error"
        );
        // 配置项目排除路径
        List<String> dezhiExcludePathPatterns = List.of(
                "/**/login"
        );
        // 添加拦截器
        registry.addInterceptor(
                        new SaInterceptor(
                                handle -> {
                                    StpUtil.checkLogin();
                                }
                        )
                )
                .addPathPatterns("/**")
                .excludePathPatterns(dezhiExcludePathPatterns)
                .excludePathPatterns(swaggerExcludePathPatterns);
    }
}
