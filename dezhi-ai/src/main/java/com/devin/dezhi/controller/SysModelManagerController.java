package com.devin.dezhi.controller;

import com.alibaba.fastjson2.JSON;
import com.devin.dezhi.constant.RedisKey;
import com.devin.dezhi.domain.entity.ModelManager;
import com.devin.dezhi.domain.vo.ModelManagerUpdateVO;
import com.devin.dezhi.domain.vo.ModelManagerVO;
import com.devin.dezhi.enums.ModelProviderEnum;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.RedisUtils;
import com.devin.dezhi.utils.r.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 2026/01/08 11:07:24.
 *
 * <p>
 * AI模型管理(ModelManager)Controller层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "modelManager")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/modelManager")
public class SysModelManagerController {

    /**
     * 修改模型.
     *
     * @param modelManagerUpdateVO 模型修改参数
     * @return ApiResult
     */
    @PostMapping("update")
    @Operation(summary = "修改模型")
    public ApiResult<Void> updateModelManager(
            @RequestBody @Validated final ModelManagerUpdateVO modelManagerUpdateVO
    ) {
        ModelManager modelManager = BeanCopyUtils.copy(modelManagerUpdateVO, ModelManager.class);
        modelManager.update();
        // 将信息更新到缓存中
        RedisUtils.set(RedisKey.generateRedisKey(RedisKey.MODEL_KEY), JSON.toJSONString(modelManager));
        return ApiResult.success();
    }

    /**
     * 获取模型列表信息.
     *
     * @return ApiResult
     */
    @GetMapping("list")
    @Operation(summary = "获取模型列表信息")
    public ApiResult<List<ModelManagerVO>> modelManagerList() {
        return ApiResult.success(ModelManager.list());
    }

    /**
     * 获取模型供应商下拉列表.
     *
     * @return ApiResult
     */
    @GetMapping("/optional")
    @Operation(summary = "获取模型供应商下拉列表")
    public ApiResult<ModelProviderEnum[]> optionalModelManager() {
        return ApiResult.success(
                ModelProviderEnum.values()
        );
    }
}
