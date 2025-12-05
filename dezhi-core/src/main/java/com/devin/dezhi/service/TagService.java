package com.devin.dezhi.service;

import com.devin.dezhi.domain.vo.TagQueryVO;
import com.devin.dezhi.domain.vo.TagSaveVO;
import com.devin.dezhi.domain.vo.TagUpdateVO;
import com.devin.dezhi.domain.vo.TagVO;
import com.devin.dezhi.utils.r.PageResult;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/12/05 19:54:12.
 *
 * <p>
 *  标签(Tag)Service层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TagService {
    /**
     * 保存标签.
     *
     * @param tagSaveVO 标签保存参数
     */
    void saveTag(TagSaveVO tagSaveVO);

    /**
     * 分页查询标签.
     *
     * @param queryVO 查询参数
     * @return 标签列表
     */
    PageResult<TagVO> pageTag(TagQueryVO queryVO);

    /**
     * 删除标签.
     *
     * @param idList 标签id列表
     */
    void deleteTag(List<BigInteger> idList);

    /**
     * 修改标签.
     *
     * @param tagUpdateVO 标签修改参数
     */
    void updateTag(TagUpdateVO tagUpdateVO);
}
