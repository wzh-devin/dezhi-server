package com.devin.dezhi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.dao.TagDao;
import com.devin.dezhi.domain.entity.Tag;
import com.devin.dezhi.domain.vo.TagQueryVO;
import com.devin.dezhi.domain.vo.TagSaveVO;
import com.devin.dezhi.domain.vo.TagUpdateVO;
import com.devin.dezhi.domain.vo.TagVO;
import com.devin.dezhi.service.TagService;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.r.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/05 19:54:12.
 *
 * <p>
 *  标签(Tag)ServiceImpl层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Override
    public void saveTag(final TagSaveVO tagSaveVO) {
        Tag tag = BeanCopyUtils.copy(tagSaveVO, Tag.class);
        tag.checkDuplicate();
        tag.save();
    }

    @Override
    public PageResult<TagVO> pageTag(final TagQueryVO queryVO) {
        Page<Tag> page = tagDao.pageByQuery(queryVO);
        if (page.getTotal() == 0) {
            return PageResult.ofEmpty(page);
        }
        List<TagVO> tagVOList = page.getRecords().stream()
                .map(tag -> BeanCopyUtils.copy(tag, TagVO.class))
                .toList();
        return PageResult.of(tagVOList, page);
    }

    @Override
    public void deleteTag(final List<BigInteger> idList) {
        tagDao.removeBatchByIds(idList);
    }

    @Override
    public void updateTag(final TagUpdateVO tagUpdateVO) {
        Tag tag = BeanCopyUtils.copy(tagUpdateVO, Tag.class);
        tag.checkDuplicate();
        tag.update();
    }
}
