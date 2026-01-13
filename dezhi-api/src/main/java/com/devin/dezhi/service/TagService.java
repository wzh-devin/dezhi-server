package com.devin.dezhi.service;

import com.devin.dezhi.domain.vo.TagQueryVO;
import com.devin.dezhi.domain.vo.TagVO;
import com.devin.dezhi.utils.r.PageResult;

/**
 * 2026/1/13 20:09.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TagService {

    /**
     * 分页查询标签.
     * @param queryVO 查询参数
     * @return 分页结果
     */
    PageResult<TagVO> pageTag(TagQueryVO queryVO);
}
