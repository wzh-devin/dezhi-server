package com.devin.dezhi.admin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.devin.dezhi.constant.I18nConstant;
import com.devin.dezhi.dao.UserDao;
import com.devin.dezhi.domain.entity.User;
import com.devin.dezhi.domain.vo.LoginVO;
import com.devin.dezhi.domain.vo.UserUpdateVO;
import com.devin.dezhi.domain.vo.UserVO;
import com.devin.dezhi.enums.DeviceTypeEnum;
import com.devin.dezhi.exception.BusinessException;
import com.devin.dezhi.admin.service.SysUserService;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.EncryptUtils;
import com.devin.dezhi.utils.i18n.I18nUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.Objects;

/**
 * 2025/12/04 20:45:09.
 *
 * <p>
 *  用户表(User)ServiceImpl层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final UserDao userDao;

    @Override
    public String login(final LoginVO login) {
        // 查询用户是否存在
        User user = userDao.lambdaQuery()
                .eq(User::getUsername, login.getUsername())
                .one();
        if (Objects.isNull(user)) {
            throw new BusinessException(I18nUtils.getMessage(I18nConstant.USER_NOT_EXIST));
        }
        // 验证密码是否正确
        if (!EncryptUtils.bcryptCheck(login.getPassword(), user.getPassword())) {
            throw new BusinessException(I18nUtils.getMessage(I18nConstant.PASSWORD_ERROR));
        }
        // 登录
        StpUtil.login(user.getId(), DeviceTypeEnum.PC.name());
        // 返回token
        return StpUtil.getTokenValue();
    }

    @Override
    public UserVO getUserInfo(final BigInteger uid) {
        User user = userDao.getById(uid);
        if (Objects.isNull(user)) {
            throw new BusinessException(I18nUtils.getMessage(I18nConstant.USER_NOT_EXIST));
        }
        return BeanCopyUtils.copy(user, UserVO.class);
    }

    @Override
    public void updateUserInfo(final UserUpdateVO updateVO) {
        User user = BeanCopyUtils.copy(updateVO, User.class);
        user.update();
        // 退出登录
        logout();
    }

    @Override
    public void logout() {
        StpUtil.logout(StpUtil.getLoginId(), StpUtil.getLoginDeviceType());
    }
}
