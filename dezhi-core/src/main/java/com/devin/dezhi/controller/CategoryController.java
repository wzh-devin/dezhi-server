package com.devin.dezhi.controller;

import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.domain.vo.CategorySaveVO;
import com.devin.dezhi.domain.vo.CategoryUpdateVO;
import com.devin.dezhi.domain.vo.CategoryVO;
import com.devin.dezhi.utils.r.Addition;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.PageResult;
import com.devin.dezhi.vo.CommonDeleteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.devin.dezhi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 2025/12/05 19:20:06.
 *
 * <p>
 * 分类(Category)Controller层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 保存分类.
     *
     * @param categorySaveVO 分类保存参数
     * @return ApiResult
     */
    @PostMapping("save")
    @Operation(summary = "保存分类")
    public ApiResult<Void> saveCategory(
            @RequestBody @Validated final CategorySaveVO categorySaveVO
    ) {
        categoryService.saveCategory(categorySaveVO);
        return ApiResult.success();
    }

    /**
     * 删除分类.
     *
     * @param categoryDeleteVO 分类删除参数
     * @return ApiResult
     */
    @PostMapping("delete")
    @Operation(summary = "删除分类")
    public ApiResult<Void> deleteCategory(
            @RequestBody final CommonDeleteVO categoryDeleteVO
    ) {
        categoryService.deleteCategory(categoryDeleteVO.getIdList());
        return ApiResult.success();
    }

    /**
     * 修改分类.
     *
     * @param categoryUpdateVO 分类修改参数
     * @return ApiResult
     */
    @PostMapping("update")
    @Operation(summary = "修改分类")
    public ApiResult<Void> updateCategory(
            @RequestBody @Validated final CategoryUpdateVO categoryUpdateVO
    ) {
        categoryService.updateCategory(categoryUpdateVO);
        return ApiResult.success();
    }

    /**
     * 获取分类下拉列表.
     * @return ApiResult
     */
    @GetMapping("/optional")
    @Operation(summary = "获取分类下拉列表")
    public ApiResult<List<CategoryVO>> optionalCategory() {
        return ApiResult.success(categoryService.optionalCategory());
    }

    /**
     * 查询分类.
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param keyword  关键字
     * @return ApiResult
     */
    @GetMapping("page")
    @Operation(summary = "查询分类")
    public ApiResult<List<CategoryVO>> pageCategory(
            @Parameter(description = "页码") @RequestParam(value = "pageNum") final Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(value = "pageSize") final Integer pageSize,
            @Parameter(description = "关键字") @RequestParam(value = "keyword", required = false) final String keyword
    ) {
        CategoryQueryVO queryVO = new CategoryQueryVO();
        queryVO.setPageSize(pageSize);
        queryVO.setPageNum(pageNum);
        queryVO.setKeyword(keyword);

        PageResult<CategoryVO> pageResult = categoryService.pageCategory(queryVO);
        Addition addition = Addition.of(pageResult);

        return ApiResult.success(pageResult.getRecords(), addition);
    }
}
