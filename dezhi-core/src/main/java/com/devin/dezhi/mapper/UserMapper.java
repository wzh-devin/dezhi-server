package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2025/12/04 20:45:07.
 *
 * <p>
 *  用户表(User)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
}
    
