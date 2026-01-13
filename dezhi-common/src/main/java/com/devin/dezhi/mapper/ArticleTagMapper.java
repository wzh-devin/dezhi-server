package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2025/12/26 22:07:50.
 *
 * <p>
 *  文章-标签关联表(ArticleTag)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    
}
    
