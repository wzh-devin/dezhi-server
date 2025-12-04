package com.devin.dezhi.utils.i18n;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 2025/12/4 18:41.
 *
 * <p>
 * 不合法检测
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class ValidationConfiguration {

    private MessageSource messageSource;

    /**
     * Set MessageSource.
     *
     * @param messageSource MessageSource
     */
    @Autowired
    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Create LocalValidatorFactoryBean Instance.
     *
     * @return LocalValidatorFactoryBean Instance
     */
    @NotNull(message = "{required.parameter.error}")
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
