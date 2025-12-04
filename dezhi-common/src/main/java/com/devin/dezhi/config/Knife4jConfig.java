package com.devin.dezhi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 2025/12/4 19:36.
 *
 * <p>
 * Knife4j 配置
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class Knife4jConfig {

    /**
     * swagger配置.
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {

        // 设置联系方式
        Contact contact = new Contact();
        contact.setName("devin");
        contact.setEmail("wzh.devin@gmail.com");
        contact.setUrl("https://github.com/wzh-devin");

        // 配置信息
        return new OpenAPI()
                .info(new Info()
                        .contact(contact)
                        .title("Dezhi")
                        .version("1.0.0")
                        .description("得智博客")
                );
    }
}
