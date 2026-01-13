package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.User;
import com.devin.dezhi.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2025/12/04 20:45:06.
 *
 * <p>
 *  用户表(User)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDao extends ServiceImpl<UserMapper, User> {

}
