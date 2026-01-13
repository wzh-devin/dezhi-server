package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.ArticleTag;
import com.devin.dezhi.mapper.ArticleTagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/26 22:07:50.
 *
 * <p>
 *  文章-标签关联表(ArticleTag)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleTagDao extends ServiceImpl<ArticleTagMapper, ArticleTag> {

    /**
     * 根据articleId删除关联信息.
     * @param articleIdList 文章id列表
     */
    public void removeByArticleId(final List<BigInteger> articleIdList) {
        lambdaUpdate()
                .in(ArticleTag::getArticleId, articleIdList)
                .remove();
    }
}
