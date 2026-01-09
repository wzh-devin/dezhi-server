package com.devin.dezhi.config;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 2026/1/9 18:21.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class WxConfig {

    @Value("${wx.corp-id}")
    private String corpId;

    @Value("${wx.secret}")
    private String corpSecret;

    @Value("${wx.agent-id}")
    private Integer agentId;

    @Value("${wx.redirect-uri}")
    private String redirectUri;

    @Value("${wx.token}")
    private String token;

    @Value("${wx.aes-key}")
    private String aesKey;

    /**
     * 创建微信服务.
     *
     * @return 微信服务
     */
    @Bean
    public WxCpService wxCpService() {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(corpId);
        config.setCorpSecret(corpSecret);
        config.setAgentId(agentId);
        config.setOauth2redirectUri(redirectUri);
        config.setToken(token);
        config.setAesKey(aesKey);

        WxCpService wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }
}
