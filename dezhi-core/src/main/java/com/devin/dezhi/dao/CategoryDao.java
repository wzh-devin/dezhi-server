package com.devin.dezhi.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devin.dezhi.domain.entity.Category;
import com.devin.dezhi.domain.vo.CategoryQueryVO;
import com.devin.dezhi.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 2025/12/05 19:20:06.
 *
 * <p>
 *  分类(Category)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryDao extends ServiceImpl<CategoryMapper, Category> {

    /**
     * 分页查询.
     * @param queryVO 查询参数
     * @return 分页结果
     */
    public Page<Category> pageByQuery(final CategoryQueryVO queryVO) {
        Page<Category> page = new Page<>(
                queryVO.getPageNum(),
                queryVO.getPageSize()
        );

        return lambdaQuery()
                .like(StringUtils.hasLength(queryVO.getKeyword()), Category::getName, queryVO.getKeyword())
                .orderByDesc(Category::getUpdateTime)
                .page(page);
    }
}
