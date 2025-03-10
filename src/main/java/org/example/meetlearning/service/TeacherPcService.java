package org.example.meetlearning.service;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.TeacherConverter;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.teacher.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TeacherPcService {

    private final TeacherService teacherService;

    public RespVo<PageVo<TeacherListRespVo>> teacherPage(TeacherQueryVo queryVo) {
        try {
            Page<Teacher> teacherPage = teacherService.selectPageParams(queryVo.getParams(), queryVo.getPageRequest());
            PageVo<TeacherListRespVo> pageVo = PageVo.map(teacherPage, TeacherConverter.INSTANCE::toListVo);
            return new RespVo<>(pageVo);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<List<SelectValueVo>> teacherManagerSearch(TeacherQueryVo queryVo) {
        try {
            List<SelectValueVo> selectValueVos = new ArrayList<>();
            selectValueVos.add(new SelectValueVo("ALL", "ALL"));
            selectValueVos.add(new SelectValueVo("NO MANAGER", "NO MANAGER"));
            List<SelectValueVo> searchVos = teacherService.selectGroupManager(queryVo.getParams());
            selectValueVos.addAll(searchVos);
            return new RespVo<>(selectValueVos);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<List<SelectValueVo>> teacherUpdateManagerSearch(TeacherQueryVo queryVo) {
        try {
            List<SelectValueVo> selectValueVos = new ArrayList<>();
            selectValueVos.add(new SelectValueVo("", "None"));
            List<SelectValueVo> searchVos = teacherService.selectGroupManager(queryVo.getParams());
            selectValueVos.addAll(searchVos);
            return new RespVo<>(selectValueVos);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<List<SelectValueVo>> teacherCountrySearch(TeacherQueryVo queryVo) {
        try {
            List<SelectValueVo> selectValueVos = new ArrayList<>();
            selectValueVos.add(new SelectValueVo("ALL", "ALL"));
            List<SelectValueVo> searchVos = teacherService.selectGroupCountry(queryVo.getParams());
            selectValueVos.addAll(searchVos);
            return new RespVo<>(selectValueVos);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<String> teacherAdd(String userCode, String userName, TeacherAddReqVo reqVo) {
        try {
            Teacher teacher = TeacherConverter.INSTANCE.toCreateTeacher(userCode, userName, reqVo);
            teacherService.insertEntity(teacher);
            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<String> teacherUpdate(String userCode, String userName, TeacherUpdateReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            teacher = TeacherConverter.INSTANCE.toUpdateTeacher(userCode, userName, teacher, reqVo);
            teacherService.updateEntity(teacher);
            return new RespVo<>("更新成功");
        } catch (Exception ex) {
            log.error("更新失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<String> managerStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            teacher.setUpdator(userCode);
            teacher.setUpdateName(userName);
            teacher.setManagerStatus(BooleanUtil.isTrue(reqVo.getStatus()));
            teacherService.updateEntity(teacher);
            return new RespVo<>("更新状态成功");
        } catch (Exception ex) {
            log.error("更新状态失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<String> managerAccountStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            teacher.setUpdator(userCode);
            teacher.setUpdateName(userName);
            teacher.setManagerStatus(BooleanUtil.isTrue(reqVo.getStatus()));
            teacherService.updateEntity(teacher);
            return new RespVo<>("更新状态成功");
        } catch (Exception ex) {
            log.error("更新状态失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<String> teacherTypeStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            teacher.setUpdator(userCode);
            teacher.setUpdateName(userName);
            teacher.setTestStatus(BooleanUtil.isTrue(reqVo.getStatus()));
            teacherService.updateEntity(teacher);
            return new RespVo<>("更新状态成功");
        } catch (Exception ex) {
            log.error("更新状态失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<TeacherTotalRespVo> teacherTotal(TeacherQueryVo queryVo) {
        try {
            Map<String, Object> params = queryVo.getParams();
            params.remove("managerId");
            params.remove("managerStatus");
            //查询工资统计
            BigDecimal totalSalary = teacherService.selectSalaryTotal(params);
            //查询老师工资统计
            params.put("managerStatus", false);
            BigDecimal teacherTotalSalary = teacherService.selectSalaryTotal(params);
            TeacherTotalRespVo respVo = new TeacherTotalRespVo(totalSalary, teacherTotalSalary, BigDecimalUtil.sub(totalSalary, teacherTotalSalary));
            return new RespVo<>(respVo);
        } catch (Exception ex) {
            log.error("统计信息查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<PageVo<TeacherLastCommentRespVo>> teacherLastCommentRespVo(String userCode, String userName, TeacherCommentQueryVo queryVo) {
        try {
            TeacherLastCommentRespVo vo = TeacherConverter.INSTANCE.toCommentVo(userCode, userName);
            Page<TeacherLastCommentRespVo> page = new Page<>(queryVo.getCurrent(), queryVo.getPageSize(), 1);
            page.setRecords(List.of(vo));
            PageVo<TeacherLastCommentRespVo> pageVo = PageVo.map(page, list -> TeacherConverter.INSTANCE.toCommentVo(userCode, userName));
            return new RespVo<>(pageVo);
        } catch (Exception ex) {
            log.error("Query Error", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }
}