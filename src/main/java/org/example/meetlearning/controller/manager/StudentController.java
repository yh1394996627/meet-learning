package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.meetlearning.controller.BaseHandler;
import org.example.meetlearning.service.StudentPcService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.*;
import org.example.meetlearning.vo.teacher.TeacherInfoRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "学生接口")
@RestController
public class StudentController implements BaseHandler {

    @Autowired
    private StudentPcService studentPcService;

    @Operation(summary = "学生列表", operationId = "studentPage")
    @PostMapping(value = "v1/student/page")
    public RespVo<PageVo<StudentListRespVo>> studentPage(@RequestBody StudentRequestQueryVo queryVo) {
        return studentPcService.studentPage(getUserCode(), queryVo);
    }

    @Operation(summary = "新增学生接口", operationId = "studentAdd")
    @PostMapping(value = "v1/student/add")
    public RespVo<String> studentAdd(@RequestBody StudentAddReqVo reqVo) {
        return studentPcService.studentAdd(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "更新学生接口", operationId = "studentUpdate")
    @PostMapping(value = "v1/student/update")
    public RespVo<String> studentUpdate(@RequestBody StudentUpdateReqVo reqVo) {
        return studentPcService.studentUpdate(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "删除学生接口", operationId = "studentDeleted")
    @PostMapping(value = "v1/student/deleted")
    public RespVo<String> studentDeleted(@RequestBody StudentRecordReqVo reqVo) {
        return studentPcService.deleteStudent(reqVo);
    }

    @Operation(summary = "更新学生备注", operationId = "studentRemarkUpdate")
    @PostMapping(value = "v1/student/remark/update")
    public RespVo<String> studentRemarkUpdate(@RequestBody StudentRemarkUpdateReqVo reqVo) {
        return studentPcService.studentRemarkUpdate(reqVo);
    }

    @Operation(summary = "查询学生详细信息", operationId = "studentInfo")
    @PostMapping(value = "v1/student/info")
    public RespVo<StudentInfoRespVo> studentInfo(@RequestBody RecordIdQueryVo reqVo) {
        return studentPcService.studentInfo(reqVo);
    }

    @Operation(summary = "学生端仪表盘-最火名师", operationId = "dashboardTeacher")
    @PostMapping(value = "v1/student/dashboard/teacher")
    public RespVo<List<TeacherInfoRespVo>> dashboardTeacher(@RequestBody StudentDashboardTeacherQueryVo queryVo) {
        return new RespVo<>(studentPcService.dashboardTeacher(queryVo));
    }

    @Operation(summary = "学生端仪表盘-日期课程", operationId = "dashboardNowClass")
    @PostMapping(value = "v1/student/dashboard/now/class")
    public RespVo<List<StudentDashboardClassRespVo>> dashboardNowClass() {
        return new RespVo<>(studentPcService.dashboardNowClass(getUserCode()));
    }
}
