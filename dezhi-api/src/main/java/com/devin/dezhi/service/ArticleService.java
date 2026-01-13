package com.devin.dezhi.service;

import com.devin.dezhi.domain.vo.ArticleQueryVO;
import com.devin.dezhi.domain.vo.ArticleVO;
import com.devin.dezhi.utils.r.PageResult;
import java.math.BigInteger;
import java.util.List;

/**
 * 2026/1/13 20:09.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ArticleService {

    /**
     * 分页查询文章.
     * @param queryVO 查询参数
     * @return 分页结果
     */
    PageResult<ArticleVO> pageArticle(ArticleQueryVO queryVO);

    /**
     * 获取推荐文章列表.
     * @return 推荐文章列表
     */
    List<ArticleVO> recommendedArticleList();

    /**
     * 获取文章信息.
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleVO getArticleInfo(BigInteger articleId);
}
