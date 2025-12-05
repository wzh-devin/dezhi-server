package com.devin.dezhi.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.domain.entity.Tag;
import com.devin.dezhi.domain.vo.TagQueryVO;
import com.devin.dezhi.mapper.TagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 2025/12/05 19:54:12.
 *
 * <p>
 *  标签(Tag)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagDao extends ServiceImpl<TagMapper, Tag> {

    /**
     * 分页查询.
     * @param queryVO 查询参数
     * @return 分页结果
     */
    public Page<Tag> pageByQuery(final TagQueryVO queryVO) {
        Page<Tag> page = new Page<>(
                queryVO.getPageNum(),
                queryVO.getPageSize()
        );

        return lambdaQuery()
                .like(StringUtils.hasLength(queryVO.getKeyword()), Tag::getName, queryVO.getKeyword())
                .orderByDesc(Tag::getUpdateTime)
                .page(page);
    }
}
