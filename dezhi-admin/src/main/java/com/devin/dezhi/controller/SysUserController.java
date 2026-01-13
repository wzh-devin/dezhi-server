package com.devin.dezhi.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.devin.dezhi.domain.vo.LoginVO;
import com.devin.dezhi.domain.vo.UserUpdateVO;
import com.devin.dezhi.domain.vo.UserVO;
import com.devin.dezhi.utils.ConvertUtils;
import com.devin.dezhi.utils.r.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.devin.dezhi.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;

/**
 * 2025/12/04 20:45:08.
 *
 * <p>
 * 用户表(User)Controller层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "user")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class SysUserController {

    private final SysUserService userService;

    /**
     * 登录.
     *
     * @param login 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public ApiResult<String> login(
            @RequestBody @Validated final LoginVO login
    ) {
        return ApiResult.success(userService.login(login));
    }

    /**
     * 获取当前用户信息.
     *
     * @return 用户信息
     */
    @GetMapping("/getUserInfo")
    @Operation(summary = "获取当前用户信息")
    public ApiResult<UserVO> getUserInfo() {
        BigInteger uid = ConvertUtils.toBigInteger(StpUtil.getLoginId());
        return ApiResult.success(userService.getUserInfo(uid));
    }

    /**
     * 修改用户信息.
     *
     * @param updateVO 修改信息
     * @return 修改结果
     */
    @PostMapping("/edit")
    @Operation(summary = "修改用户信息")
    public ApiResult<Void> editUserInfo(
            @RequestBody @Validated final UserUpdateVO updateVO
    ) {
        userService.updateUserInfo(updateVO);
        return ApiResult.success();
    }

    /**
     * 登出.
     *
     * @return 登出
     */
    @PostMapping("/logout")
    @Operation(summary = "登出")
    public ApiResult<Void> logout() {
        userService.logout();
        return ApiResult.success();
    }
}
