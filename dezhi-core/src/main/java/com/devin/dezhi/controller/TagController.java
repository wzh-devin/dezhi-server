package com.devin.dezhi.controller;

import com.devin.dezhi.domain.vo.TagQueryVO;
import com.devin.dezhi.domain.vo.TagSaveVO;
import com.devin.dezhi.domain.vo.TagUpdateVO;
import com.devin.dezhi.domain.vo.TagVO;
import com.devin.dezhi.utils.r.Addition;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.PageResult;
import com.devin.dezhi.vo.CommonDeleteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.devin.dezhi.service.TagService;
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
 * 2025/12/05 19:54:12.
 *
 * <p>
 * 标签(Tag)Controller层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "tag")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    /**
     * 保存标签.
     *
     * @param tagSaveVO 标签保存参数
     * @return ApiResult
     */
    @PostMapping("save")
    @Operation(summary = "保存标签")
    public ApiResult<Void> saveTag(
            @RequestBody @Validated final TagSaveVO tagSaveVO
    ) {
        tagService.saveTag(tagSaveVO);
        return ApiResult.success();
    }

    /**
     * 删除标签.
     *
     * @param tagDeleteVO 标签删除参数
     * @return ApiResult
     */
    @PostMapping("delete")
    @Operation(summary = "删除标签")
    public ApiResult<Void> deleteTag(
            @RequestBody final CommonDeleteVO tagDeleteVO
    ) {
        tagService.deleteTag(tagDeleteVO.getIdList());
        return ApiResult.success();
    }

    /**
     * 修改标签.
     *
     * @param tagUpdateVO 标签修改参数
     * @return ApiResult
     */
    @PostMapping("update")
    @Operation(summary = "修改标签")
    public ApiResult<Void> updateTag(
            @RequestBody @Validated final TagUpdateVO tagUpdateVO
    ) {
        tagService.updateTag(tagUpdateVO);
        return ApiResult.success();
    }

    /**
     * 获取标签下拉列表.
     * @return ApiResult
     */
    @GetMapping("/optional")
    @Operation(summary = "获取标签下拉列表")
    public ApiResult<List<TagVO>> optionalTag() {
        return ApiResult.success(tagService.optionalTag());
    }

    /**
     * 查询标签.
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param keyword  关键字
     * @return ApiResult
     */
    @GetMapping("page")
    @Operation(summary = "查询标签")
    public ApiResult<List<TagVO>> pageTag(
            @Parameter(description = "页码") @RequestParam(value = "pageNum") final Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(value = "pageSize") final Integer pageSize,
            @Parameter(description = "关键字") @RequestParam(value = "keyword", required = false) final String keyword
    ) {
        TagQueryVO queryVO = new TagQueryVO();
        queryVO.setPageSize(pageSize);
        queryVO.setPageNum(pageNum);
        queryVO.setKeyword(keyword);

        PageResult<TagVO> pageResult = tagService.pageTag(queryVO);
        Addition addition = Addition.of(pageResult);

        return ApiResult.success(pageResult.getRecords(), addition);
    }
}
