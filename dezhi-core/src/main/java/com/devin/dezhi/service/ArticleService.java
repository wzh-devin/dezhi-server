package com.devin.dezhi.service;

import com.devin.dezhi.domain.vo.ArticleQueryVO;
import com.devin.dezhi.domain.vo.ArticleUpdateVO;
import com.devin.dezhi.domain.vo.ArticleVO;
import com.devin.dezhi.utils.r.PageResult;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 *  文章表(Article)Service层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ArticleService {

    /**
     * 保存文章.
     *
     * @return ArticleVO
     */
    ArticleVO saveArticle();

    /**
     * 修改文章.
     *
     * @param articleUpdateVO 修改参数
     */
    void updateArticle(ArticleUpdateVO articleUpdateVO);

    /**
     * 获取文章信息.
     *
     * @param articleId 文章id
     * @return ArticleVO
     */
    ArticleVO getArticleInfo(BigInteger articleId);

    /**
     * 分页查询文章.
     *
     * @param queryVO 查询参数
     * @return PageResult
     */
    PageResult<ArticleVO> pageArticle(ArticleQueryVO queryVO);

    /**
     * 删除文章.
     *
     * @param idList 删除文章id列表
     */
    void deleteArticle(List<BigInteger> idList);
}
