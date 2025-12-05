package com.devin.dezhi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigInteger;
import java.util.List;

/**
 * 2025/7/16 13:47.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0
 * @since 1.0
 */
@Data
@Schema(description = "通用删除VO")
public class CommonDeleteVO {
    @Schema(description = "id列表")
    private List<BigInteger> idList;
}
