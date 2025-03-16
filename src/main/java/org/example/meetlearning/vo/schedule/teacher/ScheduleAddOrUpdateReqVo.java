package org.example.meetlearning.vo.schedule.teacher;

import lombok.Data;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.example.meetlearning.enums.ScheduleWeekEnum;

import java.util.List;

@Data
public class ScheduleAddOrUpdateReqVo {

    private String teacherId;

    private ScheduleTypeEnum scheduleType;

    private ScheduleWeekEnum weekNum;

    private List<ScheduleDateVo> dateRespVos;

}
