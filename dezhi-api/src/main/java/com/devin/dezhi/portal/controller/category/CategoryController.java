package com.devin.dezhi.portal.controller.category;

import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.domain.vo.CategoryVO;
import com.devin.dezhi.portal.service.CategoryService;
import com.devin.dezhi.utils.r.Addition;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 2026/1/13 22:51.
 *
 * <p>
 *     门户分类相关接口
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "portal-category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 查询分类.
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResult
     */
    @GetMapping("page")
    @Operation(summary = "查询分类")
    public ApiResult<List<CategoryVO>> pageCategory(
            @Parameter(description = "页码") @RequestParam(value = "pageNum") final Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(value = "pageSize") final Integer pageSize
    ) {
        CategoryQueryVO queryVO = new CategoryQueryVO();
        queryVO.setPageSize(pageSize);
        queryVO.setPageNum(pageNum);

        PageResult<CategoryVO> pageResult = categoryService.pageCategory(queryVO);
        Addition addition = Addition.of(pageResult);

        return ApiResult.success(pageResult.getRecords(), addition);
    }
}
