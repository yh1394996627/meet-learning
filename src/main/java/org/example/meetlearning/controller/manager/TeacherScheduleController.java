package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.TeacherSchedulePcService;
import org.example.meetlearning.vo.classes.StudentClassRegularReqVo;
import org.example.meetlearning.vo.classes.StudentClassRegularRespVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleAddOrUpdateReqVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleInfoRespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleOperaVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleQueryVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "老师端日程-学生申请固定请求查询", operationId = "studentScheduleRegular")
    @PostMapping(value = "v1/schedule/class/regular")
    public RespVo<List<StudentClassRegularRespVo>> studentScheduleRegular() {
        StudentClassRegularRespVo studentClassRegularRespVo = new StudentClassRegularRespVo();
        studentClassRegularRespVo.setStudentId("111122332312");
        studentClassRegularRespVo.setStudentName("ZHang San");
        studentClassRegularRespVo.setStudentEmail("zangsan@qq.com");
        studentClassRegularRespVo.setCourseType("SINGLE");
        studentClassRegularRespVo.setCourseTime("9:30-10:00");
        studentClassRegularRespVo.setCourseDateList("2025-03-30;2025-03-31;2025-04-01,2025-04-31;2025-05-31;2025-06-31;");
        return new RespVo<>(List.of(studentClassRegularRespVo));
    }

    @Operation(summary = "老师端日程-同意、不同意操作", operationId = "studentScheduleRegular")
    @PostMapping(value = "v1/schedule/class/regular/opera")
    public RespVo<String> studentScheduleRegular(@RequestBody ScheduleOperaVo ScheduleOperaVo) {
        return new RespVo<>("Operation successful");
    }
}
