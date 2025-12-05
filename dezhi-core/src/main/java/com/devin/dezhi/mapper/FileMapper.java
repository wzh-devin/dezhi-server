package com.devin.dezhi.mapper;

import com.devin.dezhi.domain.entity.File;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 2025/12/05 23:51:00.
 *
 * <p>
 *  文件素材表(File)Mapper层
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
    
}
    
