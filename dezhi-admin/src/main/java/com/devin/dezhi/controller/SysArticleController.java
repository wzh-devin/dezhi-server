package com.devin.dezhi.controller;

import com.devin.dezhi.domain.vo.ArticleQueryVO;
import com.devin.dezhi.domain.vo.ArticleUpdateVO;
import com.devin.dezhi.domain.vo.ArticleVO;
import com.devin.dezhi.enums.ArticleStatusEnum;
import com.devin.dezhi.service.SysArticleService;
import com.devin.dezhi.utils.r.Addition;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.PageResult;
import com.devin.dezhi.vo.CommonDeleteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/26 21:59:57.
 *
 * <p>
 * 文章表(Article)Controller层
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
@RequestMapping("/admin/article")
public class SysArticleController {

    private final SysArticleService articleService;

    /**
     * 初始化新增文章.
     *
     * @return ApiResult
     */
    @PostMapping("/save")
    @Operation(summary = "初始化新增文章")
    public ApiResult<ArticleVO> saveArticle() {
        return ApiResult.success(articleService.saveArticle());
    }

    /**
     * 修改文章.
     *
     * @param articleUpdateVO 文章修改参数
     * @return ApiResult
     */
    @PutMapping("/update")
    @Operation(summary = "修改文章")
    public ApiResult<Void> updateArticle(
            @RequestBody final ArticleUpdateVO articleUpdateVO
    ) {
        articleService.updateArticle(articleUpdateVO);
        return ApiResult.success();
    }

    /**
     * 获取文章信息.
     *
     * @param articleId 文章id
     * @return ApiResult
     */
    @GetMapping("/getArticleInfo/{articleId}")
    @Operation(summary = "获取文章信息")
    public ApiResult<ArticleVO> getArticleInfo(
            @Parameter(description = "文章id", in = ParameterIn.PATH) @PathVariable("articleId") final BigInteger articleId
    ) {
        return ApiResult.success(articleService.getArticleInfo(articleId));
    }

    /**
     * 分页查询文章.
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param status   文章状态
     * @param keyword  关键词
     * @return ApiResult
     */
    @GetMapping("/pageArticle")
    @Operation(summary = "分页查询文章")
    public ApiResult<List<ArticleVO>> pageArticle(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") final Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") final Integer pageSize,
            @Parameter(description = "文章状态") @RequestParam(value = "status") final ArticleStatusEnum status,
            @Parameter(description = "关键词") @RequestParam(value = "keyword", required = false) final String keyword
    ) {
        ArticleQueryVO queryVO = new ArticleQueryVO();
        queryVO.setPageNum(pageNum);
        queryVO.setPageSize(pageSize);
        queryVO.setStatus(status);
        queryVO.setKeyword(keyword);

        PageResult<ArticleVO> pageResult = articleService.pageArticle(queryVO);
        Addition addition = Addition.of(pageResult);

        return ApiResult.success(pageResult.getRecords(), addition);
    }

    /**
     * 批量删除文章.
     *
     * @param deleteVO 删除参数
     * @return ApiResult
     */
    @PostMapping("/delete")
    @Operation(summary = "批量删除文章")
    public ApiResult<Void> deleteArticle(
           @RequestBody final CommonDeleteVO deleteVO
    ) {
        articleService.deleteArticle(deleteVO.getIdList());
        return ApiResult.success();
    }

    /**
     * 清空回收站.
     *
     * @param deleteVO 删除参数
     * @return ApiResult
     */
    @PostMapping("/clearRecycleBin")
    @Operation(summary = "清空回收站")
    public ApiResult<Void> clearRecycleBin(
            @RequestBody final CommonDeleteVO deleteVO
    ) {
        articleService.clearRecycleBin(deleteVO.getIdList());
        return ApiResult.success();
    }
}
