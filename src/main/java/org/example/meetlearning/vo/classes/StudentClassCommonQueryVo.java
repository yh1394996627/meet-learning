package org.example.meetlearning.vo.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.enums.ScheduleWeekEnum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class StudentClassCommonQueryVo {

    @Schema(name = "teacherId", description = "老师ID")
    private String teacherId;

    @Schema(name = "priceContent", description = "选择的价格")
    private BigDecimal priceContent;

    @Schema(name = "courseType", description = "课程类型")
    private String courseType;

    @Schema(name = "courseDate", description = "课程时间")
    private String courseDate;

    @Schema(name = "courseTime", description = "时间段 查询老师的时候用")
    private String courseTime;

    @Schema(name = "startTime", description = "时间段 - 开始")
    private String startTime;

    @Schema(name = "stopTime", description = "时间段 - 结束")
    private String stopTime;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (priceContent != null) {
            params.put("tokenPrice", priceContent);
        }
        return params;
    }

    @Schema(hidden = true)
    public Map<String, Object> getScheduleParams() {
        Map<String, Object> params = new HashMap<>();
        if (courseDate != null) {
            params.put("weekNum", ScheduleWeekEnum.getByDate(courseDate));
        }
        if (startTime != null) {
            params.put("startTime", startTime);
        }
        if (stopTime != null) {
            params.put("stopTime", stopTime);
        }
        return params;
    }
}
