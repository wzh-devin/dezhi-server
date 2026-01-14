package com.devin.dezhi.portal.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.dao.CategoryDao;
import com.devin.dezhi.domain.entity.Category;
import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.domain.vo.CategoryVO;
import com.devin.dezhi.portal.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Override
    public PageResult<CategoryVO> pageCategory(final CategoryQueryVO queryVO) {
        Page<Category> page = categoryDao.pageByQuery(queryVO);
        if (page.getTotal() == 0) {
            return PageResult.ofEmpty(page);
        }
        List<CategoryVO> categoryVOList = page.getRecords().stream()
                .map(category -> BeanCopyUtils.copy(category, CategoryVO.class))
                .toList();
        return PageResult.of(categoryVOList, page);
    }
}
