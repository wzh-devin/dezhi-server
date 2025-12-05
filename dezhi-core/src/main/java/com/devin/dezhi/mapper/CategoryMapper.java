package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2025/12/05 19:20:06.
 *
 * <p>
 *  分类(Category)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
}
    
