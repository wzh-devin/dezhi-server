package com.devin.dezhi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 2025/12/4 18:41.
 *
 * <p>
 * 启动配置
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@SpringBootApplication
public class DezhiApplication {

    // CHECKSTYLE:OFF

    /**
     * 得智启动入口.
     *
     * @param args 启动参数
     */
    public static void main(final String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DezhiApplication.class, args);
        String port = context.getEnvironment().getProperty("server.port");
        String contextPath = context.getEnvironment().getProperty("server.servlet.context-path");
        StringBuilder swaggerPath = new StringBuilder()
                .append("http://localhost:")
                .append(port)
                .append(contextPath)
                .append("/doc.html");
        log.info("Swagger URL: {}", swaggerPath);
    }
    // CHECKSTYLE:ON
}
