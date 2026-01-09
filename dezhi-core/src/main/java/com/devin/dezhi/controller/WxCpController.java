package com.devin.dezhi.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.devin.dezhi.dao.UserDao;
import com.devin.dezhi.domain.entity.User;
import com.devin.dezhi.enums.DeviceTypeEnum;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.r.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

/**
 * 2026/1/9 18:03.
 *
 * <p>
 * 企微相关
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Tag(name = "企微相关")
@Slf4j
@RestController
@RequestMapping("/wx")
@RequiredArgsConstructor
public class WxCpController {

    private final WxCpService wxCpService;

    private final UserDao userDao;

    /**
     * WxCp登录.
     *
     * @return Url
     */
    @GetMapping("/login")
    @Operation(summary = "WxCp登录")
    public ApiResult<String> login() {
        String url = wxCpService.getOauth2Service()
                .buildAuthorizationUrl(
                        wxCpService.getWxCpConfigStorage().getOauth2redirectUri(),
                        generateState(),
                        "snsapi_base"
                );
        return ApiResult.success(url);
    }

    /**
     * WxCp扫码登录.
     *
     * @return Url
     */
    @GetMapping("/qr/login")
    @Operation(summary = "WxCp登录")
    public ApiResult<String> qrLogin() {
        String url = wxCpService.buildQrConnectUrl(
                        wxCpService.getWxCpConfigStorage().getOauth2redirectUri(),
                        "snsapi_base"
                );
        return ApiResult.success(url);
    }

    /**
     * WxCp回调.
     *
     * @param code  code
     * @param state state
     * @return ApiResult
     */
    @GetMapping("/callback")
    @Operation(summary = "WxCp回调")
    public ApiResult<Boolean> callback(
            @RequestParam("code") final String code,
            @RequestParam("state") final String state
    ) throws WxErrorException {

        WxCpOauth2UserInfo userInfo = wxCpService.getOauth2Service().getUserInfo(code);
        WxCpUser wxCpUser = wxCpService.getUserService().getById(userInfo.getUserId());

        User user = userDao.getById(wxCpUser.getName());

        if (Objects.isNull(user)) {
            user = new User();
            user.setUsername(wxCpUser.getName());
            user.setPassword(wxCpUser.getOpenUserId());
            user.setEmail(wxCpUser.getEmail());
            user.save();
        }

        StpUtil.login(user.getId(), DeviceTypeEnum.PC.name());

        return ApiResult.success();
    }

    private String generateState() {
        return IdGenerator.generateUUID();
    }
}
