package org.example.meetlearning.controller.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.meetlearning.vo.common.KeywordQueryVo;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.teacher.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "老师接口")
@RestController
public class TeacherController {

    @Operation(summary = "老师列表", operationId = "teacherPage")
    @PostMapping(value = "v1/teacher/page")
    public RespVo<PageVo<TeacherListRespVo>> teacherPage(@RequestBody TeacherQueryVo queryVo) {
        TeacherListRespVo respVo = new TeacherListRespVo();
        respVo.setRecordId("123213123123");
        respVo.setRole("manage");
        respVo.setName("1111");
        respVo.setAttendance(new BigDecimal("12"));
        respVo.setRating(new BigDecimal("133"));
        respVo.setPrice(new BigDecimal("2"));
        respVo.setCreditsPrice("1");
        respVo.setRate(new BigDecimal("45"));
        respVo.setConfirmed(new BigDecimal("12"));
        respVo.setCanceled(new BigDecimal("2"));
        respVo.setComplaints(new BigDecimal("1"));
        respVo.setSalary(new BigDecimal("13"));
        return new RespVo<>(PageVo.getNewPageVo(1, 10, 1, 1, List.of(respVo)));

    }

    @Operation(summary = "查询条件-管理人查询", operationId = "teacherManagerSearch")
    @PostMapping(value = "v1/teacher/search/manager")
    public RespVo<List<SelectValueVo>> teacherManagerSearch(@RequestBody KeywordQueryVo queryVo) {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("ALL", "ALL"));
        selectValueVos.add(new SelectValueVo("NO MANAGER", "NO MANAGER"));
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "查询条件-国家查询", operationId = "countrySearch")
    @PostMapping(value = "v1/teacher/search/country")
    public RespVo<List<SelectValueVo>> countrySearch() {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("ALL", "ALL"));
        selectValueVos.add(new SelectValueVo("Philippines", "Philippines"));
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "查询条件-老师类型查询", operationId = "teacherTypeSearch")
    @PostMapping(value = "v1/teacher/search/teacher/type")
    public RespVo<List<SelectValueVo>> teacherTypeSearch() {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("0", "ALL"));
        selectValueVos.add(new SelectValueVo("1", "Test Teachers"));
        selectValueVos.add(new SelectValueVo("2", "Regular Teachers"));
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "查询条件-老师状态查询", operationId = "teacherStatusSearch")
    @PostMapping(value = "v1/teacher/search/teacher/status")
    public RespVo<List<SelectValueVo>> teacherStatusSearch() {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("0", "ALL"));
        selectValueVos.add(new SelectValueVo("1", "Enabled"));
        selectValueVos.add(new SelectValueVo("2", "Disabled"));
        return new RespVo<>(selectValueVos);
    }

    @Operation(summary = "新增老师", operationId = "teacherAdd")
    @PostMapping(value = "v1/teacher/add")
    public RespVo<String> teacherAdd(@RequestBody TeacherAddReqVo reqVo) {
        return new RespVo<>("新增成功");
    }

    @Operation(summary = "更新老师", operationId = "teacherUpdate")
    @PostMapping(value = "v1/teacher/update")
    public RespVo<String> teacherUpdate(@RequestBody TeacherUpdateReqVo reqVo) {
        return new RespVo<>("更新成功");
    }

    @Operation(summary = "管理员状态设置", operationId = "managerStatusSet")
    @PostMapping(value = "v1/teacher/manager/status")
    public RespVo<String> managerStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return new RespVo<>("设置成功");
    }

    @Operation(summary = "启用/停用状态设置", operationId = "managerAccountStatusSet")
    @PostMapping(value = "v1/teacher/account/status")
    public RespVo<String> managerAccountStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return new RespVo<>("设置成功");
    }

    @Operation(summary = "老师类型设置", operationId = "teacherTypeStatusSet")
    @PostMapping(value = "v1/teacher/teacher/type")
    public RespVo<String> teacherTypeStatusSet(@RequestBody TeacherStatusReqVo reqVo) {
        return new RespVo<>("设置成功");
    }

    @Operation(summary = "老师工资统计", operationId = "teacherTotal")
    @PostMapping(value = "v1/teacher/teacher/total")
    public RespVo<TeacherTotalRespVo> teacherTotal(@RequestBody TeacherQueryVo queryVo) {
        TeacherTotalRespVo respVo = new TeacherTotalRespVo();
        respVo.setTotalSalary(new BigDecimal("12"));
        respVo.setTeacherTotalSalary(new BigDecimal("8"));
        respVo.setManagerTotalSalary(new BigDecimal("4"));
        return new RespVo<>(respVo);
    }

}
