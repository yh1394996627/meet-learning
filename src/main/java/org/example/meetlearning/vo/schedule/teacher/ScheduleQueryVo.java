package org.example.meetlearning.vo.schedule.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.example.meetlearning.enums.ScheduleWeekEnum;

import java.util.Map;

@Data
public class ScheduleQueryVo {

    @Schema(name = "teacherId", description = "老师id")
    private String teacherId;

    @Schema(name = "weekNum", description = "周数")
    private String weekNum;

    @Schema(name = "scheduleType", description = "时间段类型")
    private ScheduleTypeEnum scheduleType;

}
