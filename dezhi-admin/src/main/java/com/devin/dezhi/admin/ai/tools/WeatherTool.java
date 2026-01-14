package com.devin.dezhi.admin.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 2026/1/8 18:12.
 *
 * <p>
 *     天气工具类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Component
public class WeatherTool {

    /**
     * 获取天气信息.
     * @param city  城市
     * @return 天气信息
     */
    @Tool(description = "获取天气信息")
    public String getWeather(
            @ToolParam(description = "城市名称，如：北京、上海") final String city
    ) {
        log.info("查询天气: {}", city);
        // 实际项目中调用天气API
        return String.format("%s今日天气：晴，温度25°C，湿度60%%", city);
    }
}
