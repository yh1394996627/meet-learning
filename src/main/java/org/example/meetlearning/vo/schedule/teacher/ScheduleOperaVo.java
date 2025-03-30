package org.example.meetlearning.vo.schedule.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.enums.ScheduleTypeEnum;

@Data
public class ScheduleOperaVo {

    @Schema(name = "recordId", description = "老师id")
    private String recordId;

    @Schema(name = "status", description = "是否同意")
    private Boolean status;

}
