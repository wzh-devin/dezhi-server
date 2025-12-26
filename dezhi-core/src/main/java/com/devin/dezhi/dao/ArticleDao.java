package com.devin.dezhi.dao;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.domain.entity.Article;
import com.devin.dezhi.domain.entity.ArticleTag;
import com.devin.dezhi.domain.entity.Category;
import com.devin.dezhi.domain.entity.Tag;
import com.devin.dezhi.domain.vo.ArticleQueryVO;
import com.devin.dezhi.domain.vo.ArticleVO;
import com.devin.dezhi.enums.ArticleStatusEnum;
import com.devin.dezhi.mapper.ArticleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devin.dezhi.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 * 文章表(Article)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleDao extends ServiceImpl<ArticleMapper, Article> {

    private final CategoryDao categoryDao;

    private final ArticleTagDao articleTagDao;

    private final TagDao tagDao;

    /**
     * 根据文章id获取文章信息.
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    public ArticleVO getArticleVOById(final BigInteger articleId) {
        Article article = lambdaQuery()
                .ne(Article::getStatus, ArticleStatusEnum.DELETED)
                .eq(Article::getId, articleId)
                .one();
        ArticleVO articleVO = BeanCopyUtils.copy(article, ArticleVO.class);
        if (Objects.nonNull(article.getCategoryId())) {
            Category category = categoryDao.getById(article.getCategoryId());
            articleVO.setCategoryName(category.getName());
        }
        List<ArticleTag> articleTagList = articleTagDao.lambdaQuery()
                .eq(ArticleTag::getArticleId, articleId)
                .list();
        if (!CollectionUtils.isEmpty(articleTagList)) {
            Set<BigInteger> tagSet = articleTagList.stream()
                    .map(ArticleTag::getTagId)
                    .collect(Collectors.toSet());
            List<String> tagNameList = tagDao.listByIds(tagSet)
                    .stream()
                    .map(Tag::getName)
                    .toList();
            articleVO.setTagList(tagNameList);
        }
        return articleVO;
    }

    /**
     * 分页查询文章.
     *
     * @param queryVO 查询条件
     * @return 文章列表
     */
    public Page<Article> pageArticle(final ArticleQueryVO queryVO) {
        Page<Article> page = new Page<>(
                queryVO.getPageNum(),
                queryVO.getPageSize()
        );

        LambdaQueryChainWrapper<Article> lambdaQuery = lambdaQuery();

        if (ArticleStatusEnum.NORMAL.equals(queryVO.getStatus())) {
            lambdaQuery = lambdaQuery.ne(Article::getStatus, ArticleStatusEnum.DELETED);
        } else {
            lambdaQuery = lambdaQuery.in(Article::getStatus, queryVO.getStatus().name());
        }

        return lambdaQuery
                .orderByDesc(Article::getUpdateTime)
                .page(page);
    }

    /**
     * 获取标签文章映射.
     *
     * @param articleIdList 文章id列表
     * @return 标签文章映射
     */
    public Map<BigInteger, Set<BigInteger>> getTagMap(final List<BigInteger> articleIdList) {
        return articleTagDao.lambdaQuery()
                .in(ArticleTag::getArticleId, articleIdList)
                .list()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                ArticleTag::getArticleId,
                                Collectors.mapping(
                                        ArticleTag::getTagId,
                                        Collectors.toSet()
                                )
                        )
                );
    }
}
