package com.devin.dezhi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.dao.ArticleDao;
import com.devin.dezhi.dao.CategoryDao;
import com.devin.dezhi.dao.TagDao;
import com.devin.dezhi.domain.entity.Article;
import com.devin.dezhi.domain.entity.Category;
import com.devin.dezhi.domain.entity.Tag;
import com.devin.dezhi.domain.vo.ArticleQueryVO;
import com.devin.dezhi.domain.vo.ArticleUpdateVO;
import com.devin.dezhi.domain.vo.ArticleVO;
import com.devin.dezhi.enums.StatusFlagEnum;
import com.devin.dezhi.service.ArticleService;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.r.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 * 文章表(Article)ServiceImpl层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDao articleDao;

    private final CategoryDao categoryDao;

    private final TagDao tagDao;

    @Override
    public ArticleVO saveArticle() {
        Article article = new Article().save();
        return BeanCopyUtils.copy(article, ArticleVO.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateArticle(final ArticleUpdateVO articleUpdateVO) {
        Article article = BeanCopyUtils.copy(articleUpdateVO, Article.class);
        article.update();
    }

    @Override
    public ArticleVO getArticleInfo(final BigInteger articleId) {
        return articleDao.getArticleVOById(articleId);
    }

    @Override
    public PageResult<ArticleVO> pageArticle(final ArticleQueryVO queryVO) {
        Page<Article> page = articleDao.pageArticle(queryVO);
        if (page.getTotal() == 0) {
            return PageResult.ofEmpty(page);
        }
        List<Article> articleList = page.getRecords();
        // 查询列表
        List<BigInteger> articleIdList = articleList.stream().map(Article::getId).toList();
        Map<BigInteger, String> categoryMap = categoryDao.listByIds(
                        articleList.stream()
                                .map(Article::getCategoryId)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(
                        Collectors.toMap(Category::getId, Category::getName)
                );
        Map<BigInteger, Set<BigInteger>> tagMap = articleDao.getTagMap(articleIdList);
        Set<BigInteger> tagIdList = tagMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        List<Tag> tagList;
        if (!CollectionUtils.isEmpty(tagIdList)) {
            tagList = tagDao.listByIds(tagIdList);
        } else {
            tagList = new ArrayList<>();
        }
        // 组装数据
        return PageResult.of(
                BeanCopyUtils.copyList(articleList, ArticleVO.class)
                        .stream()
                        .peek(articleVO -> {
                            Optional.ofNullable(articleVO.getCategoryId())
                                    .ifPresent(a -> articleVO.setCategoryName(categoryMap.get(articleVO.getCategoryId())));
                            List<String> tagNameList = tagList.stream()
                                    .filter(
                                            tag -> tagMap.getOrDefault(
                                                    articleVO.getId(),
                                                    Collections.emptySet()
                                            ).contains(tag.getId())
                                    )
                                    .map(Tag::getName)
                                    .toList();
                            Optional.of(tagNameList)
                                    .filter(t -> !CollectionUtils.isEmpty(t))
                                    .ifPresentOrElse(
                                            articleVO::setTagList,
                                            () -> articleVO.setTagList(Collections.emptyList()));
                        }).toList(),
                page
        );
    }

    @Override
    public void deleteArticle(final List<BigInteger> idList) {
        articleDao.lambdaUpdate()
                .ne(Article::getStatus, StatusFlagEnum.DELETED.name())
                .set(Article::getStatus, StatusFlagEnum.DELETED.name())
                .in(Article::getId, idList)
                .update();
    }
}
