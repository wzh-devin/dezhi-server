package com.devin.dezhi.portal.controller.article;

import com.devin.dezhi.domain.vo.ArticleQueryVO;
import com.devin.dezhi.domain.vo.ArticleVO;
import com.devin.dezhi.enums.ArticleStatusEnum;
import com.devin.dezhi.portal.service.ArticleService;
import com.devin.dezhi.utils.r.Addition;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

/**
 * 2026/1/13 20:05.
 *
 * <p>
 * 门户文章相关接口
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "article")
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/article")
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 分页查询文章.
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param categoryId 分类id
     * @param tagId 标签id
     * @param keyword 关键词
     * @return ApiResult
     */
    @GetMapping("/pageArticle")
    @Operation(summary = "分页查询文章")
    public ApiResult<List<ArticleVO>> pageArticle(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") final Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") final Integer pageSize,
            @Parameter(description = "分类id") @RequestParam(value = "categoryId", required = false) final BigInteger categoryId,
            @Parameter(description = "标签id") @RequestParam(value = "tagId", required = false) final BigInteger tagId,
            @Parameter(description = "关键词") @RequestParam(value = "keyword", required = false) final String keyword
    ) {
        ArticleQueryVO queryVO = new ArticleQueryVO();
        queryVO.setStatus(ArticleStatusEnum.PUBLISHED);
        queryVO.setPageNum(pageNum);
        queryVO.setPageSize(pageSize);
        queryVO.setCategoryId(categoryId);
        queryVO.setTagId(tagId);
        queryVO.setKeyword(keyword);

        PageResult<ArticleVO> pageResult = articleService.pageArticle(queryVO);
        Addition addition = Addition.of(pageResult);

        return ApiResult.success(pageResult.getRecords(), addition);
    }

    /**
     * 推荐文章.
     *
     * @return ApiResult
     */
    @GetMapping("/recommend")
    @Operation(summary = "推荐文章")
    public ApiResult<List<ArticleVO>> recommendedArticleList() {
        return ApiResult.success(articleService.recommendedArticleList());
    }

    /**
     * 获取文章详情.
     *
     * @param articleId 文章id
     * @return ApiResult
     */
    @GetMapping("/{articleId}")
    @Operation(summary = "获取文章详情")
    public ApiResult<ArticleVO> getArticleInfo(
            @Parameter(description = "文章id") @PathVariable("articleId") final BigInteger articleId
    ) {
        return ApiResult.success(articleService.getArticleInfo(articleId));
    }
}
