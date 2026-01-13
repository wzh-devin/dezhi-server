package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2026/01/08 17:59:42.
 *
 * <p>
 *  对话消息表(Message)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    
}
    
