package com.devin.dezhi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.dao.CategoryDao;
import com.devin.dezhi.domain.entity.Category;
import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.domain.vo.CategorySaveVO;
import com.devin.dezhi.domain.vo.CategoryUpdateVO;
import com.devin.dezhi.domain.vo.CategoryVO;
import com.devin.dezhi.service.CategoryService;
import com.devin.dezhi.utils.BeanCopyUtils;
import com.devin.dezhi.utils.r.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/05 19:20:07.
 *
 * <p>
 *  分类(Category)ServiceImpl层
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
    public void saveCategory(final CategorySaveVO categorySaveVO) {
        Category category = BeanCopyUtils.copy(categorySaveVO, Category.class);
        category.save();
    }

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

    @Override
    public void deleteCategory(final List<BigInteger> idList) {
        categoryDao.removeBatchByIds(idList);
    }

    @Override
    public void updateCategory(final CategoryUpdateVO categoryUpdateVO) {
        Category category = BeanCopyUtils.copy(categoryUpdateVO, Category.class);
        category.update();
    }
}
