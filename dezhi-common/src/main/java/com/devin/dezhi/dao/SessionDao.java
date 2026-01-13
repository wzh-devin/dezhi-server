package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.Session;
import com.devin.dezhi.mapper.SessionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2026/01/08 17:59:42.
 *
 * <p>
 *  用户会话表(Session)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionDao extends ServiceImpl<SessionMapper, Session> {

}
