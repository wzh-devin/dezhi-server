package com.devin.dezhi.controller;

import com.devin.dezhi.domain.dto.UploadSession;
import com.devin.dezhi.domain.vo.FileQueryVO;
import com.devin.dezhi.domain.vo.FileVO;
import com.devin.dezhi.domain.vo.req.InitiateUploadRequest;
import com.devin.dezhi.utils.ConvertUtils;
import com.devin.dezhi.utils.r.Addition;
import com.devin.dezhi.utils.r.ApiResult;
import com.devin.dezhi.utils.r.PageResult;
import com.devin.dezhi.vo.CommonDeleteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.devin.dezhi.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 * 文件素材表(File)Controller层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "file")
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    /**
     * 初始化文件上传.
     *
     * @param initiateUpload 请求参数
     * @return 响应结果
     */
    @PostMapping("/upload/initiate")
    @Operation(summary = "初始化上传")
    public ApiResult<UploadSession> initiateUpload(
            @RequestBody @Validated final InitiateUploadRequest initiateUpload
    ) {
        return ApiResult.success(fileService.initiateUpload(initiateUpload));
    }

    /**
     * 上传文件分片.
     *
     * @param uploadId   上传ID
     * @param chunkIndex 分片索引
     * @param file       文件
     * @return 响应结果
     */
    @PostMapping("/upload/chunk")
    @Operation(summary = "上传文件分片")
    public ApiResult<Void> uploadChunk(
            @Parameter(description = "会话上传id") @RequestPart("uploadId") final String uploadId,
            @Parameter(description = "分片索引") @RequestPart("chunkIndex") final String chunkIndex,
            @Parameter(description = "文件") @RequestPart("file") final MultipartFile file
    ) {
        fileService.uploadChunk(uploadId, ConvertUtils.toInteger(chunkIndex), file);
        return ApiResult.success();
    }

    /**
     * 完成上传.
     *
     * @param uploadId 上传ID
     * @return 响应结果
     */
    @PostMapping("/upload/complete")
    @Operation(summary = "完成上传")
    public ApiResult<FileVO> completeUpload(
            @Parameter(description = "会话上传id") @RequestParam("uploadId") final String uploadId
    ) {
        return ApiResult.success(fileService.completeUpload(uploadId));
    }

    /**
     * 取消上传.
     *
     * @param uploadId 上传ID
     * @return 响应结果
     */
    @PostMapping("/upload/cancel")
    @Operation(summary = "取消上传")
    public ApiResult<Void> cancelUpload(
            @Parameter(description = "会话上传id") @RequestParam("uploadId") final String uploadId
    ) {
        fileService.cancelUpload(uploadId);
        return ApiResult.success();
    }

    /**
     * 获取上传状态.
     *
     * @param uploadId 上传ID
     * @return 响应结果
     */
    @PostMapping("/upload/status")
    @Operation(summary = "获取上传状态")
    public ApiResult<UploadSession> getUploadStatus(
            @Parameter(description = "会话上传id") @RequestParam("uploadId") final String uploadId
    ) {
        return ApiResult.success(fileService.getUploadStatus(uploadId));
    }

    /**
     * 分页查询文件.
     *
     * @param fileQueryVO 请求参数
     * @return 响应结果
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询文件")
    public ApiResult<List<FileVO>> pageFile(
            @RequestBody @Validated final FileQueryVO fileQueryVO
    ) {
        PageResult<FileVO> pageResult = fileService.pageFile(fileQueryVO);
        Addition addition = Addition.of(pageResult);

        return ApiResult.success(pageResult.getRecords(), addition);
    }

    /**
     * 删除文件.
     *
     * @param deleteVO deleteVO
     * @return 响应结果
     */
    @PostMapping("/delete")
    @Operation(summary = "删除文件")
    public ApiResult<Void> deleteFile(
            @RequestBody @Validated final CommonDeleteVO deleteVO
    ) {
        fileService.deleteFile(deleteVO.getIdList());
        return ApiResult.success();
    }

}
