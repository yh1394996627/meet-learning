package org.example.meetlearning.vo.schedule.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ScheduleDateVo {

    @Schema(name = "weekNum", description = "周数")
    private String weekNum;

    @Schema(name = "beginTime", description = "开始时间")
    private String beginTime;

    @Schema(name = "endTime", description = "结束时间")
    private String endTime;

}
