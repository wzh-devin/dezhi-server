package com.devin.dezhi.portal.service;

import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.domain.vo.CategoryVO;
import com.devin.dezhi.utils.r.PageResult;

/**
 * 2026/1/13 22:52.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CategoryService {

    /**
     * 分页查询分类.
     *
     * @param queryVO 查询条件
     * @return 分类列表
     */
    PageResult<CategoryVO> pageCategory(CategoryQueryVO queryVO);
}
