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
    private ScheduleWeekEnum weekNum;

    @Schema(name = "scheduleType", description = "时间段类型")
    private ScheduleTypeEnum scheduleType;

    @Schema(hidden = true)
    public ScheduleWeekEnum getWeekNum() {
        if (weekNum == null) {
            return ScheduleWeekEnum.MONDAY;
        }
        return weekNum;
    }

}
