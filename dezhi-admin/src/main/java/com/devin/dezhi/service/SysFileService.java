package com.devin.dezhi.service;

import com.devin.dezhi.domain.dto.UploadSession;
import com.devin.dezhi.domain.vo.FileQueryVO;
import com.devin.dezhi.domain.vo.FileVO;
import com.devin.dezhi.domain.vo.req.InitiateUploadRequest;
import com.devin.dezhi.utils.r.PageResult;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 * 文件素材表(File)Service层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SysFileService {

    /**
     * 初始化文件上传.
     *
     * @param initiateUpload 请求参数
     * @return 响应结果
     */
    UploadSession initiateUpload(InitiateUploadRequest initiateUpload);

    /**
     * 上传文件分片.
     *
     * @param uploadId       上传ID
     * @param chunkIndex     分片索引
     * @param file           文件
     */
    void uploadChunk(String uploadId, Integer chunkIndex, MultipartFile file);

    /**
     * 完成文件上传.
     *
     * @param uploadId 上传ID
     * @return 文件信息
     */
    FileVO completeUpload(String uploadId);

    /**
     * 取消文件上传.
     *
     * @param uploadId 上传ID
     */
    void cancelUpload(String uploadId);

    /**
     * 分页查询文件.
     *
     * @param fileQueryVO 查询参数
     * @return 文件列表
     */
    PageResult<FileVO> pageFile(FileQueryVO fileQueryVO);

    /**
     * 删除文件.
     *
     * @param idList 文件id列表
     */
    void deleteFile(List<BigInteger> idList);

    /**
     * 获取上传状态.
     *
     * @param uploadId 上传ID
     * @return 上传状态
     */
    UploadSession getUploadStatus(String uploadId);
}
