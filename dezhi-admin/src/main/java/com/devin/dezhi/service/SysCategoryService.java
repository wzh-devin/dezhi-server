package com.devin.dezhi.service;

import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.domain.vo.CategorySaveVO;
import com.devin.dezhi.domain.vo.CategoryUpdateVO;
import com.devin.dezhi.domain.vo.CategoryVO;
import com.devin.dezhi.utils.r.PageResult;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/05 19:20:07.
 *
 * <p>
 * 分类(Category)Service层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SysCategoryService {

    /**
     * 保存分类.
     *
     * @param categorySaveVO 分类保存参数
     */
    void saveCategory(CategorySaveVO categorySaveVO);

    /**
     * 分页查询分类.
     *
     * @param queryVO 查询参数
     * @return 分类列表
     */
    PageResult<CategoryVO> pageCategory(CategoryQueryVO queryVO);

    /**
     * 删除分类.
     *
     * @param idList 分类id列表
     */
    void deleteCategory(List<BigInteger> idList);

    /**
     * 修改分类.
     *
     * @param categoryUpdateVO 分类修改参数
     */
    void updateCategory(CategoryUpdateVO categoryUpdateVO);

    /**
     * 分类下拉列表.
     *
     * @return CategoryVO
     */
    List<CategoryVO> optionalCategory();
}
