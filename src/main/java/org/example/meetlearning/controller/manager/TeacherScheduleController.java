package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.TeacherSchedulePcService;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleAddOrUpdateReqVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleInfoRespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleQueryVo;
import org.example.meetlearning.vo.teacher.TeacherAddReqVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "老师日程接口")
@RestController
@AllArgsConstructor
public class TeacherScheduleController implements BaseController {

    private final TeacherSchedulePcService teacherSchedulePcService;

    @Operation(summary = "新增过更新时间段", operationId = "scheduleAdd")
    @PostMapping(value = "v1/schedule/save")
    public RespVo<String> scheduleAdd(@RequestBody ScheduleAddOrUpdateReqVo reqVo) {
        return teacherSchedulePcService.scheduleAdd(getUserCode(), reqVo);
    }

    @Operation(summary = "查询时间段", operationId = "scheduleInfo")
    @PostMapping(value = "v1/schedule/info")
    public RespVo<ScheduleInfoRespVo> scheduleInfo(@RequestBody ScheduleQueryVo reqVo) {
        return teacherSchedulePcService.scheduleInfo(reqVo);
    }


}
