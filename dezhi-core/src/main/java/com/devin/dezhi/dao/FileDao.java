package com.devin.dezhi.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.domain.entity.File;
import com.devin.dezhi.domain.vo.FileQueryVO;
import com.devin.dezhi.enums.FileUploadStatusEnum;
import com.devin.dezhi.enums.StatusFlagEnum;
import com.devin.dezhi.mapper.FileMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 * 文件素材表(File)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileDao extends ServiceImpl<FileMapper, File> {

    /**
     * 通过文件hash查询文件.
     *
     * @param fileHash 文件hash
     * @return 文件
     */
    public File getByHash(final String fileHash) {
        return lambdaQuery()
                .eq(File::getHash, fileHash)
                .eq(File::getStatus, FileUploadStatusEnum.FINISHED.name())
                .one();
    }

    /**
     * 通过文件最终名称查询文件.
     *
     * @param finalName 文件最终名称
     * @return 文件
     */
    public File getByFinalName(final String finalName) {
        return lambdaQuery()
                .eq(File::getDeleted, StatusFlagEnum.NORMAL.getStatus())
                .eq(File::getFinalName, finalName)
                .one();
    }

    /**
     * 分页查询文件.
     *
     * @param queryVO 文件查询参数
     * @return 分页结果
     */
    public Page<File> pageByQuery(final FileQueryVO queryVO) {
        Page<File> page = new Page<>(
                queryVO.getPageNum(),
                queryVO.getPageSize()
        );

        return lambdaQuery()
                .eq(File::getDeleted, queryVO.getDeleted().getStatus())
                .like(StringUtils.hasLength(queryVO.getKeyword()), File::getOriginalName, queryVO.getKeyword())
                .eq(StringUtils.hasLength(queryVO.getStorageType()), File::getStorageType, queryVO.getStorageType())
                .eq(StringUtils.hasLength(queryVO.getType()), File::getType, queryVO.getType())
                .orderByDesc(File::getUpdateTime)
                .page(page);
    }
}
