package org.example.meetlearning.controller.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.StudentClassPcService;
import org.example.meetlearning.vo.classes.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.evaluation.TeacherComplaintReqVo;
import org.example.meetlearning.vo.evaluation.TeacherEvaluationReqVo;
import org.example.meetlearning.vo.student.StudentInfoRespVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "学生预约课程接口")
@RestController
@Slf4j
@AllArgsConstructor
public class StudentClassController implements BaseController {

    private final StudentClassPcService studentClassPcService;

    @Operation(summary = "学生预约记录列表", operationId = "studentClassPage")
    @PostMapping(value = "v1/student/class/page")
    public RespVo<PageVo<StudentClassListRespVo>> studentClassPage(@RequestBody StudentClassQueryVo queryVo) {
        return studentClassPcService.studentClassPage(queryVo);
    }

    @Operation(summary = "新增学生预约", operationId = "studentClassAdd")
    @PostMapping(value = "v1/student/class/add")
    public RespVo<String> studentClassAdd(@RequestBody StudentClassAddReqVo reqVo) throws IOException {
        return studentClassPcService.studentClassAdd(getUserCode(), getUserName(), getUserName(), reqVo);
    }

    @Operation(summary = "选择课时币接口", operationId = "classCoinList")
    @PostMapping(value = "v1/student/class/coin/list")
    public RespVo<List<SelectValueVo>> classCoinList() {
        return studentClassPcService.classCoinList();
    }


    @Operation(summary = "选择时间段接口", operationId = "classTimeList")
    @PostMapping(value = "v1/student/class/time/list")
    public RespVo<List<String>> classTimeList(@RequestBody StudentClassCommonQueryVo queryVo) {
        return studentClassPcService.classTimeList(queryVo);
    }

    @Operation(summary = "选择老师接口", operationId = "classTeacherList")
    @PostMapping(value = "v1/student/class/teacher/list")
    public RespVo<List<SelectValueVo>> classTeacherList(@RequestBody StudentClassCommonQueryVo queryVo) {
        return studentClassPcService.classTeacherList(queryVo);
    }

    @Operation(summary = "统计接口", operationId = "classTotalList")
    @PostMapping(value = "v1/student/class/total")
    public RespVo<StudentClassTotalRespVo> classTotalList(@RequestBody StudentClassQueryVo queryVo) {
        return studentClassPcService.classTotalList(queryVo);
    }

    @Operation(summary = "取消课程罚款提示 ,false提示", operationId = "studentClassCancelVerify")
    @PostMapping(value = "v1/student/class/cancel/verify")
    public RespVo<Boolean> studentClassCancelVerify(@RequestBody RecordIdQueryVo reqVo) {
        return new RespVo<>(studentClassPcService.studentClassCancelOrUpdateVerify(getUserCode(), reqVo));
    }

    @Operation(summary = "取消预约课程", operationId = "studentClassCancel")
    @PostMapping(value = "v1/student/class/cancel")
    public RespVo<String> studentClassCancel(@RequestBody RecordIdQueryVo reqVo) {
        studentClassPcService.studentClassCancel(getUserCode(), reqVo);
        return new RespVo<>("Course cancelled successfully");
    }

    @Operation(summary = "学生更改课程时间", operationId = "studentClassUpdateTime")
    @PostMapping(value = "v1/student/class/update/time")
    public RespVo<String> studentClassUpdateTime(@RequestBody StudentClassUpdateTimeReqVo reqVo) {
        studentClassPcService.studentClassUpdateTime(getUserCode(), getUserName(), reqVo);
        return new RespVo<>("Course time changed successfully");
    }

    @Operation(summary = "学生评价", operationId = "studentClassEvaluate")
    @PostMapping(value = "v1/student/class/evaluate")
    public RespVo<String> studentClassEvaluate(@RequestBody TeacherEvaluationReqVo reqVo) {
        studentClassPcService.studentClassEvaluate(getUserCode(), reqVo);
        return new RespVo<>("Successfully added comment");
    }

    @Operation(summary = "学生投诉", operationId = "studentClassComplaint")
    @PostMapping(value = "v1/student/class/complaint")
    public RespVo<String> studentClassComplaint(@RequestBody TeacherComplaintReqVo reqVo) {
        studentClassPcService.studentClassComplaint(getUserCode(), reqVo);
        return new RespVo<>("New complaint successfully added");
    }


    @Operation(summary = "学生申请固定上课", operationId = "studentClassRegular")
    @PostMapping(value = "v1/student/class/regular")
    public RespVo<String> studentClassRegular(@RequestBody StudentClassRegularReqVo reqVo) {
        return new RespVo<>("New complaint successfully added");
    }

    @Operation(summary = "返回会议加入链接", operationId = "studentClassMeeting")
    @PostMapping(value = "v1/student/class/meeting")
    public RespVo<String> studentClassMeeting(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(studentClassPcService.meetingJoinUrl(queryVo.getRecordId()));
    }

}
