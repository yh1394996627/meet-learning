package org.example.meetlearning.controller.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseHandler;
import org.example.meetlearning.dao.entity.TeacherEvaluationRecord;
import org.example.meetlearning.enums.TeacherSpecialistsEnum;
import org.example.meetlearning.service.TeacherPcService;
import org.example.meetlearning.service.TeacherSalaryPcService;
import org.example.meetlearning.vo.common.*;
import org.example.meetlearning.vo.teacher.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "老师接口")
@RestController
@AllArgsConstructor
public class TeacherController implements BaseHandler {

    private final TeacherPcService teacherPcService;

    private final TeacherSalaryPcService teacherSalaryPcService;

    @Operation(summary = "老师列表", operationId = "teacherPage")
    @PostMapping(value = "v1/teacher/page")
    public RespVo<PageVo<TeacherListRespVo>> teacherPage(@RequestBody TeacherQueryVo queryVo) {
        return teacherPcService.teacherPage(getUserCode(), queryVo);
    }

    @Operation(summary = "查询条件-管理人查询", operationId = "teacherManagerSearch")
    @PostMapping(value = "v1/teacher/search/manager")
    public RespVo<List<SelectValueVo>> teacherManagerSearch(@RequestBody TeacherQueryVo queryVo) {
        return teacherPcService.teacherManagerSearch(queryVo);
    }

    @Operation(summary = "更新老师里面的管理人调用", operationId = "teacherUpdateManagerSearch")
    @PostMapping(value = "v1/teacher/update/search/manager")
    public RespVo<List<SelectValueVo>> teacherUpdateManagerSearch(@RequestBody TeacherQueryVo queryVo) {
        return teacherPcService.teacherUpdateManagerSearch(queryVo);
    }

    @Operation(summary = "查询条件-国家查询", operationId = "countrySearch")
    @PostMapping(value = "v1/teacher/search/country")
    public RespVo<List<SelectValueVo>> countrySearch(@RequestBody TeacherQueryVo queryVo) {
        return teacherPcService.teacherCountrySearch(queryVo);
    }

    @Operation(summary = "查询条件-老师类型查询", operationId = "teacherTypeSearch")
    @PostMapping(value = "v1/teacher/search/teacher/type")
    public RespVo<List<SelectValueVo>> teacherTypeSearch() {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo(null, "ALL"));
        selectValueVos.add(new SelectValueVo("1", "Test Teachers"));
        selectValueVos.add(new SelectValueVo("0", "Regular Teachers"));
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "查询条件-老师状态查询", operationId = "teacherStatusSearch")
    @PostMapping(value = "v1/teacher/search/teacher/status")
    public RespVo<List<SelectValueVo>> teacherStatusSearch() {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo(null, "ALL"));
        selectValueVos.add(new SelectValueVo("1", "Enabled"));
        selectValueVos.add(new SelectValueVo("0", "Disabled"));
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "新增老师", operationId = "teacherAdd")
    @PostMapping(value = "v1/teacher/add")
    public RespVo<String> teacherAdd(@RequestBody TeacherAddReqVo reqVo) {
        return teacherPcService.teacherAdd(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "更新老师", operationId = "teacherUpdate")
    @PostMapping(value = "v1/teacher/update")
    public RespVo<String> teacherUpdate(@RequestBody TeacherUpdateReqVo reqVo) {
        return teacherPcService.teacherUpdate(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "管理员状态设置", operationId = "managerStatusSet")
    @PostMapping(value = "v1/teacher/manager/status")
    public RespVo<String> managerStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return teacherPcService.managerStatusSet(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "启用/停用状态设置", operationId = "managerAccountStatusSet")
    @PostMapping(value = "v1/teacher/account/status")
    public RespVo<String> managerAccountStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return teacherPcService.managerAccountStatusSet(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "团体课设置", operationId = "groupStatusSet")
    @PostMapping(value = "v1/teacher/group/set")
    public RespVo<String> groupStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return teacherPcService.groupStatusSet(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "删除老师功能", operationId = "deleteTeacher")
    @PostMapping(value = "v1/teacher/delete")
    public RespVo<String> deleteTeacher(@RequestBody RecordIdQueryVo reqVo) {
        return teacherPcService.deleteTeacher(getUserCode(),reqVo);
    }


    @Operation(summary = "课程类型", operationId = "groupStatus")
    @PostMapping(value = "v1/teacher/group/status")
    public RespVo<List<SelectValueVo>> groupStatus(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(teacherPcService.groupStatus(queryVo));
    }

    @Operation(summary = "老师类型设置", operationId = "teacherTypeStatusSet")
    @PostMapping(value = "v1/teacher/type")
    public RespVo<String> teacherTypeStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return teacherPcService.teacherTypeStatusSet(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "老师工资统计", operationId = "teacherTotal")
    @PostMapping(value = "v1/teacher/total")
    public RespVo<TeacherTotalRespVo> teacherTotal(@RequestBody TeacherQueryVo queryVo) {
        return teacherPcService.teacherTotal(queryVo);
    }

    @Operation(summary = "老师最新评论", operationId = "teacherLastComment")
    @PostMapping(value = "v1/teacher/last/comment")
    public RespVo<List<TeacherLastCommentRespVo>> teacherLastComment(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(teacherPcService.teacherLastCommentRespVo(getUserCode(), queryVo));
    }

    @Operation(summary = "查询老师评分记录", operationId = "teacherEvaluationRecord")
    @PostMapping(value = "v1/teacher/evaluation/record")
    public RespVo<PageVo<TeacherEvaluationRecordRespVo>> teacherEvaluationRecord(@RequestBody RecordIdPageQueryVo<TeacherEvaluationRecord> queryVo) {
        return new RespVo<>(teacherPcService.teacherEvaluationRecord(getUserCode(), queryVo));
    }

    @Operation(summary = "老师仪表盘", operationId = "dashboard")
    @PostMapping(value = "v1/teacher/dashboard")
    public RespVo<TeacherDashboardRespVo> dashboard() {
        return teacherPcService.dashboard(getUserCode(), getUserName());
    }

    @Operation(summary = "老师详细信息查询", operationId = "teacherInfo")
    @PostMapping(value = "v1/teacher/info")
    public RespVo<TeacherInfoRespVo> teacherInfo(@RequestBody RecordIdQueryVo queryVo) {
        return teacherPcService.teacherInfo(queryVo);
    }

    @Operation(summary = "学生端-老师列表", operationId = "teacherPage")
    @PostMapping(value = "v1/teacher/pc/page")
    public RespVo<PageVo<TeacherInfoRespVo>> studentTeacherPage(@RequestBody TeacherPcQueryVo queryVo) {
        return new RespVo<>(teacherPcService.teacherPcPage(queryVo));
    }

    @Operation(summary = "学生端-老师列表-国家查询", operationId = "countrySearch")
    @PostMapping(value = "v1/teacher/pc/search/country")
    public RespVo<List<SelectValueVo>> countrySearch() {
        return teacherPcService.teacherPcCountrySearch();
    }


    @Operation(summary = "学生端-老师列表-擅长查询", operationId = "countrySearch")
    @PostMapping(value = "v1/teacher/pc/search/specialists")
    public RespVo<List<SelectValueVo>> specialistsSearch() {
        List<SelectValueVo> selectValueVos = List.of(
                new SelectValueVo(TeacherSpecialistsEnum.ADULTS.name(), TeacherSpecialistsEnum.ADULTS.name()),
                new SelectValueVo(TeacherSpecialistsEnum.TEENS.name(), TeacherSpecialistsEnum.TEENS.name()),
                new SelectValueVo(TeacherSpecialistsEnum.KIDS.name(), TeacherSpecialistsEnum.KIDS.name())
        );
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "管理员老师列表-结算工资记录", operationId = "settlementSalaryList")
    @PostMapping(value = "v1/teacher/salary/settlement")
    public RespVo<List<TeacherSalaryRespVo>> settlementSalaryList(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(teacherSalaryPcService.settlementSalaryList(queryVo));
    }

    @Operation(summary = "管理员老师列表-结算工资操作", operationId = "settlementSalary")
    @PostMapping(value = "v1/teacher/settlement/salary")
    public RespVo<String> settlementSalary(@RequestBody RecordIdQueryVo queryVo) {
        teacherSalaryPcService.settlementSalary(getUserCode(), getUserName(), queryVo.getRecordId());
        return new RespVo<>("Operation successful");
    }
}
