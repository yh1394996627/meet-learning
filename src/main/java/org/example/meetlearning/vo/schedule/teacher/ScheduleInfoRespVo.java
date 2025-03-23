package org.example.meetlearning.vo.schedule.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.example.meetlearning.enums.ScheduleWeekEnum;

import java.util.Date;
import java.util.List;

@Data
public class ScheduleInfoRespVo {

    @Schema(name = "teacherId", description = "老师id")
    private String teacherId;

    @Schema(name = "scheduleType", description = "时间段类型")
    private ScheduleTypeEnum scheduleType;

    @Schema(name = "weekNum", description = "周数")
    private ScheduleWeekEnum weekNum;

    @Schema(name = "dateRespVos", description = "时间段集合")
    private List<ScheduleDateVo> dateRespVos;

    @Schema(name = "offDates", description = "时间集合")
    private List<Date> offDates;

}
