package com.devin.dezhi.admin.service;

import com.devin.dezhi.domain.vo.LoginVO;
import com.devin.dezhi.domain.vo.UserUpdateVO;
import com.devin.dezhi.domain.vo.UserVO;
import java.math.BigInteger;

/**
 * 2025/12/04 20:45:09.
 *
 * <p>
 * 用户表(User)Service层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SysUserService {

    /**
     * 登录.
     *
     * @param login 登录信息
     * @return Token
     */
    String login(LoginVO login);

    /**
     * 获取用户信息.
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(BigInteger uid);

    /**
     * 修改用户信息.
     *
     * @param updateVO 修改信息
     */
    void updateUserInfo(UserUpdateVO updateVO);

    /**
     * 登出.
     */
    void logout();
}
