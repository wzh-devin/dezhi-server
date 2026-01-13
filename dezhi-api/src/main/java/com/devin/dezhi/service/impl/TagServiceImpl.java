package com.devin.dezhi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.dao.TagDao;
import com.devin.dezhi.domain.entity.Tag;
import com.devin.dezhi.domain.vo.TagQueryVO;
import com.devin.dezhi.domain.vo.TagVO;
import com.devin.dezhi.service.TagService;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.r.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 2026/1/13 22:52.
 *
 * <p>
 *     门户分类服务
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

    private final TagDao categoryDao;

    @Override
    public PageResult<TagVO> pageTag(final TagQueryVO queryVO) {
        Page<Tag> page = categoryDao.pageByQuery(queryVO);
        if (page.getTotal() == 0) {
            return PageResult.ofEmpty(page);
        }
        List<TagVO> categoryVOList = page.getRecords().stream()
                .map(category -> BeanCopyUtils.copy(category, TagVO.class))
                .toList();
        return PageResult.of(categoryVOList, page);
    }
}
