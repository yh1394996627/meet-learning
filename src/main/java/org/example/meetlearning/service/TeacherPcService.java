package org.example.meetlearning.service;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.TeacherConverter;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherFeature;
import org.example.meetlearning.dao.entity.TeacherSchedule;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.enums.ScheduleWeekEnum;
import org.example.meetlearning.service.impl.BaseConfigService;
import org.example.meetlearning.service.impl.TeacherFeatureService;
import org.example.meetlearning.service.impl.TeacherScheduleService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherListRespVo;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherPriceReqVo;
import org.example.meetlearning.vo.teacher.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TeacherPcService extends BasePcService {

    private final TeacherService teacherService;

    private final TeacherFeatureService teacherFeatureService;
    private final TeacherScheduleService teacherScheduleService;
    private final BaseConfigService baseConfigService;

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

    public RespVo<PageVo<TeacherInfoRespVo>> teacherPcPage(TeacherPcQueryVo queryVo) {
        try {
            List<String> teacherIds = new ArrayList<>();
            //查询时间段有空的老师
            ScheduleWeekEnum weekEnum = queryVo.getWeek();
            List<String> teacher1Ids = teacherScheduleService.selectTeacherIdByWeekNumAndTime(weekEnum.name(), queryVo.getBeginTime(), queryVo.getEndTime());

            //如果查询条件勾选了擅长
            if (StringUtils.hasText(queryVo.getSpecialists())) {
                teacher1Ids = CollectionUtils.isEmpty(teacher1Ids) ? teacher1Ids : null;
                List<String> teacher2Ids = teacherFeatureService.selectTeacherIdBySpecialists(queryVo.getSpecialists(), teacher1Ids);
                teacherIds.addAll(teacher2Ids);
            } else {
                teacherIds.addAll(teacher1Ids);
            }
            //查詢老師信息
            Map<String, Object> params = queryVo.getParams();
            params.put("recordIds", teacherIds);
            Page<Teacher> teacherPage = teacherService.selectPageParams(params, queryVo.getPageRequest());
            PageVo<TeacherInfoRespVo> pageVo = PageVo.map(teacherPage, TeacherConverter.INSTANCE::toTeacherInfo);
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
            Assert.isTrue(StringUtils.hasText(reqVo.getEmail()), "Email cannot be empty");
            Assert.isTrue(StringUtils.hasText(reqVo.getPassword()), "Password cannot be empty");
            Teacher teacher = TeacherConverter.INSTANCE.toCreateTeacher(userCode, userName, reqVo);
            teacherService.insertEntity(teacher);
            //创建登陆帐号
            addUser(userCode, userName, teacher.getRecordId(), teacher.getEmail(), reqVo.getPassword(),
                    RoleEnum.STUDENT, teacher.getName(), teacher.getEnName(), teacher.getEmail());

            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<String> teacherUpdate(String userCode, String userName, TeacherUpdateReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "RecordId cannot be empty");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            Assert.notNull(teacher, "Teacher cannot be empty");
            teacher = TeacherConverter.INSTANCE.toUpdateTeacher(userCode, userName, teacher, reqVo);
            teacherService.updateEntity(teacher);

            //清掉原有特点并重新写入
            teacherFeatureService.deleteByTeacherId(teacher.getRecordId());
            Teacher finalTeacher = teacher;
            List<TeacherFeature> features = reqVo.getSpecialists().stream().map(feature -> TeacherConverter.INSTANCE.toTeacherFeature(userCode, finalTeacher.getRecordId(), feature)).toList();
            if (!CollectionUtils.isEmpty(features)) {
                teacherFeatureService.insertBatch(features);
            }
            return new RespVo<>("Teacher update successful");
        } catch (Exception ex) {
            log.error("Failed to update teacher", ex);
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

    public RespVo<TeacherDashboardRespVo> dashboard(String userCode) {
        try {
            Teacher teacher = teacherService.selectByRecordId(userCode);
            Assert.notNull(teacher, "Teacher cannot be empty");
            TeacherDashboardRespVo respVo = TeacherConverter.INSTANCE.toTeacherDashboard(teacher);
            return new RespVo<>(respVo);
        } catch (Exception ex) {
            log.error("Query Error", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<List<SharedTeacherListRespVo>> sharedTeacherList() {
        List<Teacher> teacherList = teacherService.selectListByAll();
        List<SharedTeacherListRespVo> respVos = teacherList.stream().map(TeacherConverter.INSTANCE::toSharedTeacherListRespVo).toList();
        return new RespVo<>(respVos);
    }

    public RespVo<String> sharedTeacherPriceSet(SharedTeacherPriceReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "RecordId cannot be empty");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            Assert.notNull(teacher, "Teacher information not obtained");
            teacher.setPrice(reqVo.getPrice());
            teacherService.updateEntity(teacher);
            return new RespVo<>("Price setting successful");
        } catch (Exception ex) {
            log.error("Price setting failed", ex);
            return new RespVo<>("Price setting failed", false, ex.getMessage());
        }
    }

    public RespVo<TeacherInfoRespVo> teacherInfo(RecordIdQueryVo queryVo) {
        try {
            String recordId = queryVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "RecordId cannot be empty");
            Teacher teacher = teacherService.selectByRecordId(recordId);
            URL downloadAvatar = downloadAvatar(teacher.getAvatarUrl());
            URL downloadVideo = downloadAvatar(teacher.getVideoUrl());
            teacher.setAvatarUrl(downloadAvatar != null ? downloadAvatar.toString() : "");
            teacher.setVideoUrl(downloadVideo != null ? downloadVideo.toString() : "");
            Assert.notNull(teacher, "Teacher information not obtained");
            return new RespVo<>(TeacherConverter.INSTANCE.toTeacherInfo(teacher));
        } catch (Exception ex) {
            log.error("Query failed", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<List<SelectValueVo>> teacherPcCountrySearch() {
        try {
            List<SelectValueVo> selectValueVos = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            List<SelectValueVo> searchVos = teacherService.selectGroupCountry(params);
            selectValueVos.addAll(searchVos);
            return new RespVo<>(selectValueVos);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }
}