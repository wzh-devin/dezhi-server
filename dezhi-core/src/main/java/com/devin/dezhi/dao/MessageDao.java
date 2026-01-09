package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.Message;
import com.devin.dezhi.mapper.MessageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2026/01/08 17:59:42.
 *
 * <p>
 *  对话消息表(Message)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

}
