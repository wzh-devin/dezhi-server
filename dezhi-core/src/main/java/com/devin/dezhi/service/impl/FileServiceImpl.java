package com.devin.dezhi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.config.MinioConfig;
import com.devin.dezhi.constant.FileConstant;
import com.devin.dezhi.constant.I18nConstant;
import com.devin.dezhi.constant.RedisKey;
import com.devin.dezhi.dao.FileDao;
import com.devin.dezhi.domain.dto.UploadSession;
import com.devin.dezhi.domain.entity.File;
import com.devin.dezhi.domain.vo.FileQueryVO;
import com.devin.dezhi.domain.vo.FileVO;
import com.devin.dezhi.domain.vo.req.InitiateUploadRequest;
import com.devin.dezhi.enums.FileUploadStatusEnum;
import com.devin.dezhi.enums.StatusFlagEnum;
import com.devin.dezhi.enums.StorageTypeEnum;
import com.devin.dezhi.exception.BusinessException;
import com.devin.dezhi.exception.ValidateException;
import com.devin.dezhi.service.FileService;
import com.devin.dezhi.service.MinioService;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.ConvertUtils;
import com.devin.dezhi.utils.FileTypeUtils;
import com.devin.dezhi.utils.IdGenerator;
import com.devin.dezhi.utils.RedisUtils;
import com.devin.dezhi.utils.i18n.I18nUtils;
import com.devin.dezhi.utils.r.PageResult;
import com.devin.dezhi.utils.r.ResultEnum;
import io.minio.ListPartsResponse;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 * 文件素材表(File)ServiceImpl层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDao fileDao;

    private final MinioService minioService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UploadSession initiateUpload(final InitiateUploadRequest initiateUpload) {
        checkUploadFile(initiateUpload);
        // 检查文件是否存在
        File file = fileDao.getByHash(initiateUpload.getFileHash());
        if (Objects.nonNull(file)) {
            // 如果文件被删除，则更新为正常状态
            if (StatusFlagEnum.DELETED.getStatus().equals(file.getDeleted())) {
                file.init();
                file.setDeleted(StatusFlagEnum.NORMAL.getStatus());
                file.update();
            }
            // 文件已经存在
            UploadSession session = new UploadSession();
            // 标记为秒传
            session.setUploadId("INSTANT");
            session.setFinalName(file.getFinalName());
            session.setStatus(FileUploadStatusEnum.FINISHED);
            return session;
        }
        String extension = FileTypeUtils.getExtension(initiateUpload.getOriginalName());
        String mimeType = FileTypeUtils.getMimeType(extension);
        String finalName = IdGenerator.generateUUID() + "." + extension;
        MinioConfig minioConfig = minioService.getMinioConfig();
        // 获取Minio上传id
        String minioUploadId = minioService.initiateMultipartUpload(finalName);
        UploadSession session = new UploadSession();
        session.setUploadId(IdGenerator.generateUUID());
        session.setMinioUploadId(minioUploadId);
        session.setFinalName(finalName);
        session.setOriginalName(initiateUpload.getOriginalName());
        session.setBucketName(minioConfig.getBucketName());
        session.setFileHash(initiateUpload.getFileHash());
        session.setTotalSize(initiateUpload.getFileSize());
        session.setTotalChunks(initiateUpload.getTotalChunks());
        session.setStatus(FileUploadStatusEnum.UPLOADING);
        // Redis保存上传会话
        RedisUtils.setEx(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, session.getUploadId()), JSON.toJSONString(session), 24, TimeUnit.HOURS);

        // PgSql保存到数据库
        File saveFile = new File();
        saveFile.setOriginalName(initiateUpload.getOriginalName());
        saveFile.setFinalName(finalName);
        saveFile.setBucketName(minioConfig.getBucketName());
        saveFile.setHash(initiateUpload.getFileHash());
        saveFile.setSize(ConvertUtils.toBigInteger(initiateUpload.getFileSize()));
        saveFile.setType(mimeType);
        saveFile.setExtension(extension);
        saveFile.setStorageType(StorageTypeEnum.MINIO.name());
        saveFile.save();

        return session;
    }

    @Override
    public void uploadChunk(
            final String uploadId,
            final Integer chunkIndex,
            final MultipartFile file
    ) {
        // 获取当前缓存的上传会话
        UploadSession session = JSONObject.parseObject(
                RedisUtils.get(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, uploadId)),
                UploadSession.class
        );
        if (Objects.isNull(session)) {
            throw new BusinessException(ResultEnum.SESSION_NOT_FOUND);
        }
        // 如果分片已经上传，则直接跳过即可
        if (session.getCompetedChunks().contains(chunkIndex)) {
            return;
        }
        // 上传Minio分片
        try {
            minioService.uploadChunk(
                    session.getFinalName(),
                    session.getMinioUploadId(),
                    chunkIndex,
                    file.getInputStream(),
                    file.getSize()
            );
            // 更新会话信息
            session.getCompetedChunks().add(chunkIndex);
            RedisUtils.setEx(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, session.getUploadId()), JSON.toJSONString(session), 24, TimeUnit.HOURS);
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.UPLOAD_ERROR, e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public FileVO completeUpload(final String uploadId) {
        // 获取当前缓存的上传会话
        UploadSession session = JSONObject.parseObject(
                RedisUtils.get(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, uploadId)),
                UploadSession.class
        );
        if (Objects.isNull(session)) {
            throw new BusinessException(ResultEnum.SESSION_NOT_FOUND);
        }
        // 验证所有分片状态
        if (session.getCompetedChunks().size() != session.getTotalChunks()) {
            throw new BusinessException(ResultEnum.UPLOAD_NOT_FINISHED, session.getCompetedChunks().size() + "/" + session.getTotalChunks());
        }

        // Minio分片检查
        ListPartsResponse partsResponse = minioService.listParts(session.getFinalName(), session.getMinioUploadId());
        List<Part> partList = partsResponse.result().partList();
        // 分片排序
        Part[] parts = partList.stream()
                .sorted(Comparator.comparingInt(Part::partNumber))
                .toArray(Part[]::new);
        if (parts.length != session.getTotalChunks()) {
            throw new BusinessException(
                    ResultEnum.UPLOAD_ERROR,
                    I18nUtils.getMessage(I18nConstant.CHUNK_COUNT_NOT_MATCH, session.getTotalChunks(), parts.length)
            );
        }

        // 合并完成上传
        minioService.completeMultipartUpload(session.getFinalName(), session.getMinioUploadId(), parts);

        // 生成文件地址
        String uri = minioService.getMinioConfig().getBucketName() + "/" + session.getFinalName();
        File file = fileDao.getByFinalName(session.getFinalName());
        file.setUri(uri);
        file.setStatus(FileUploadStatusEnum.FINISHED.name());
        file.update();

        // 删除会话缓存
        RedisUtils.delete(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, uploadId));

        return BeanCopyUtils.copy(file, FileVO.class);
    }

    @Override
    public void cancelUpload(final String uploadId) {
        // 获取当前缓存的上传会话
        UploadSession session = JSONObject.parseObject(
                RedisUtils.get(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, uploadId)),
                UploadSession.class
        );
        if (Objects.isNull(session)) {
            throw new BusinessException(ResultEnum.SESSION_NOT_FOUND);
        }
        minioService.cancelMultipartUpload(session.getFinalName(), session.getMinioUploadId());
        // 删除会话缓存
        RedisUtils.delete(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, uploadId));
    }

    @Override
    public PageResult<FileVO> pageFile(final FileQueryVO fileQueryVO) {
        Page<File> page = fileDao.pageByQuery(fileQueryVO);
        if (page.getTotal() == 0) {
            return PageResult.ofEmpty(page);
        }

        List<FileVO> fileVOList = page.getRecords().stream()
                .map(file -> {
                    String url = minioService.getPublicUrl(file.getFinalName());
                    FileVO fileVO = BeanCopyUtils.copy(file, FileVO.class);
                    fileVO.setUrl(url);
                    return fileVO;
                }).toList();

        return PageResult.of(fileVOList, page);
    }

    @Override
    public void deleteFile(final List<BigInteger> idList) {
        // 逻辑删除文件
        fileDao.lambdaUpdate()
                .eq(File::getDeleted, StatusFlagEnum.NORMAL.getStatus())
                .set(File::getDeleted, StatusFlagEnum.DELETED.getStatus())
                .in(File::getId, idList)
                .update();
    }

    @Override
    public UploadSession getUploadStatus(final String uploadId) {
        // 获取当前缓存的上传会话
        UploadSession session = JSONObject.parseObject(
                RedisUtils.get(RedisKey.generateRedisKey(RedisKey.UPLOAD_SESSION_KEY, uploadId)),
                UploadSession.class
        );
        if (Objects.isNull(session)) {
            throw new BusinessException(ResultEnum.SESSION_NOT_FOUND);
        }
        return session;
    }

    /**
     * 校验请求参数.
     *
     * @param initiateUpload 请求参数
     */
    private void checkUploadFile(final InitiateUploadRequest initiateUpload) {
        // 校验文件类型
        String extension = FileTypeUtils.getExtension(initiateUpload.getOriginalName());
        if (!FileTypeUtils.isAllowedImageType(extension)) {
            throw new ValidateException(ResultEnum.FILE_ERROR.getCode(), I18nConstant.FILE_TYPE_NOT_ALLOWED);
        }

        // 校验文件大小
        if (!FileTypeUtils.isValidFileSize(initiateUpload.getFileSize())) {
            throw new ValidateException(
                    ResultEnum.FILE_ERROR.getCode(),
                    I18nUtils.getMessage(
                            I18nConstant.FILE_SIZE_TOO_LARGE, FileTypeUtils.getMaxSize(FileTypeUtils.FileUnit.MB)
                    )
            );
        }

        // 校验分片数量
        if (initiateUpload.getTotalChunks() > FileConstant.MAX_CHUNK_SIZE) {
            throw new ValidateException(
                    ResultEnum.FILE_ERROR.getCode(),
                    I18nUtils.getMessage(I18nConstant.FILE_CHUNK_TOO_MANY, FileConstant.MAX_CHUNK_SIZE)
            );
        }
    }
}
