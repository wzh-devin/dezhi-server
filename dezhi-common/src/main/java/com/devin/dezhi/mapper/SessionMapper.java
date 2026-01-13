package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.Session;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2026/01/08 17:59:42.
 *
 * <p>
 *  用户会话表(Session)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface SessionMapper extends BaseMapper<Session> {
    
}
    
