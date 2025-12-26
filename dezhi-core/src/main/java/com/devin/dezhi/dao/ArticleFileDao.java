package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.ArticleFile;
import com.devin.dezhi.mapper.ArticleFileMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2025/12/26 22:07:50.
 *
 * <p>
 *  文章-文件关联表(ArticleFile)Dao层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleFileDao extends ServiceImpl<ArticleFileMapper, ArticleFile> {

}
