package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 *  文章表(Article)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    
}
    
