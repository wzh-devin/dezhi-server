package com.devin.dezhi.config;

import com.devin.dezhi.interceptor.SaTokenInterceptor;
import com.devin.dezhi.utils.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
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
        SaTokenInterceptor saTokenInterceptor = SpringContextHolder.getBean(SaTokenInterceptor.class);
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
                "/**/login",
                "/portal/**"
        );
        // 添加拦截器，处理异步请求时跳过验证（SSE流式请求的二次派发）
        registry.addInterceptor(saTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(dezhiExcludePathPatterns)
                .excludePathPatterns(swaggerExcludePathPatterns);
    }

    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor asyncHttpTaskExecutor =
                SpringContextHolder.getBean("asyncHttpTaskExecutor", ThreadPoolTaskExecutor.class);
        configurer.setTaskExecutor(asyncHttpTaskExecutor);
        configurer.setDefaultTimeout(5 * 60 * 1000);
    }
}
