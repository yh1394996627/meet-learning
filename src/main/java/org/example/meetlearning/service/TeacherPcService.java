package org.example.meetlearning.service;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.example.meetlearning.converter.TeacherConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.*;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherListRespVo;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherPriceReqVo;
import org.example.meetlearning.vo.teacher.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class TeacherPcService extends BasePcService {

    private final TeacherService teacherService;
    private final TeacherFeatureService teacherFeatureService;
    private final TeacherScheduleService teacherScheduleService;
    private final BaseConfigService baseConfigService;
    private final UserService userService;
    private final TeacherEvaluationService teacherEvaluationService;
    private final TextbookService textbookService;
    private final StudentClassService studentClassService;

    public RespVo<PageVo<TeacherListRespVo>> teacherPage(String userCode, TeacherQueryVo queryVo) {
        try {
            User accountUser = userService.selectByRecordId(userCode);
            Assert.notNull(accountUser, "User information not obtained");
            Map<String, Object> params = queryVo.getParams();
            //如果是老师账号查询管理的老师
            if (StringUtils.pathEquals(accountUser.getType(), RoleEnum.TEACHER.name())) {
                params.put("managerId", userCode);
            }

            List<SelectValueVo> baseConfigs = baseConfigService.selectByType(ConfigTypeEnum.COUNTRY.name());
            Map<String, String> countryMap = baseConfigs.stream().collect(Collectors.toMap(SelectValueVo::getValue, SelectValueVo::getLabel));
            Page<Teacher> teacherPage = teacherService.selectPageParams(params, queryVo.getPageRequest());
            PageVo<TeacherListRespVo> pageVo = PageVo.map(teacherPage, list -> {
                TeacherListRespVo respVo = TeacherConverter.INSTANCE.toListVo(list);
                if (countryMap.containsKey(list.getCountry())) {
                    respVo.setCountry(countryMap.get(list.getCountry()));
                }
                return respVo;
            });
            return new RespVo<>(pageVo);
        } catch (
                Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public PageVo<TeacherInfoRespVo> teacherPcPage(TeacherPcQueryVo queryVo) {
        List<String> teacherIds = new ArrayList<>();
        //查询时间段有空的老师
        ScheduleWeekEnum weekEnum = queryVo.getWeek();
        List<String> teacher1Ids = teacherScheduleService.selectTeacherIdByWeekNumAndTime(weekEnum.name(), queryVo.getBeginTime(), queryVo.getEndTime());
        if (CollectionUtils.isEmpty(teacher1Ids)) {
            return null;
        }
        //如果查询条件勾选了擅长
        if (StringUtils.hasText(queryVo.getSpecialists())) {
            teacher1Ids = !CollectionUtils.isEmpty(teacher1Ids) ? teacher1Ids : null;
            List<String> teacher2Ids = teacherFeatureService.selectTeacherIdBySpecialists(queryVo.getSpecialists(), teacher1Ids);
            teacherIds.addAll(teacher2Ids);
        } else {
            teacherIds.addAll(teacher1Ids);
        }
        //查詢老師信息
        Map<String, Object> params = queryVo.getParams();
        if (!CollectionUtils.isEmpty(teacherIds)) {
            params.put("recordIds", teacherIds);
        } else {
            return null;
        }
        params.put("zoomActivationStatus", true);
        Page<Teacher> teacherPage = teacherService.selectPageParams(params, queryVo.getPageRequest());
        PageVo<TeacherInfoRespVo> pageVo = PageVo.map(teacherPage, list -> {
            TeacherInfoRespVo respVo = TeacherConverter.INSTANCE.toTeacherInfo(list);
            respVo.setAvatarUrl(downloadFile(list.getAvatarUrl()));
            respVo.setVideoUrl(downloadFile(list.getVideoUrl()));
            return respVo;
        });
        return pageVo;
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
            List<SelectValueVo> searchVos = teacherService.selectAllGroupManager(queryVo.getParams());
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
            User newUser = addUser(userCode, userName, teacher.getRecordId(), teacher.getEmail(), reqVo.getPassword(),
                    RoleEnum.TEACHER, teacher.getName(), teacher.getEnName(), teacher.getEmail(), null);

            //创建用户关联的课时币
            addFinance(userCode, userName, newUser);
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
            if (StringUtils.hasText(teacher.getCurrencyCode())) {
                BaseConfig baseConfig = baseConfigService.selectByCode(teacher.getCurrencyCode());
                teacher.setCurrencyCode(baseConfig != null ? baseConfig.getCode() : null);
            }
            teacherService.updateEntity(teacher);

            //清掉原有特点并重新写入
            teacherFeatureService.deleteByTeacherId(teacher.getRecordId());
            Teacher finalTeacher = teacher;
            List<String> specialists = reqVo.getSpecialists();
            Map<String, Object> params = new HashMap<>();
            params.put("recordIds", specialists);
            List<Textbook> textbooks = textbookService.selectByParams(params);
            List<TeacherFeature> features = textbooks.stream().map(feature -> TeacherConverter.INSTANCE.toTeacherFeature(userCode, finalTeacher.getRecordId(), feature)).toList();
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
            teacher.setEnabledStatus(BooleanUtil.isTrue(reqVo.getStatus()));
            teacherService.updateEntity(teacher);
            return new RespVo<>("更新状态成功");
        } catch (Exception ex) {
            log.error("更新状态失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }


    public RespVo<String> groupStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
            Teacher newTeacher = new Teacher();
            newTeacher.setId(teacher.getId());
            newTeacher.setUpdator(userCode);
            newTeacher.setUpdateName(userName);
            newTeacher.setGroupStatus(BooleanUtil.isTrue(reqVo.getStatus()));
            teacherService.updateEntity(newTeacher);
            return new RespVo<>("更新状态成功");
        } catch (Exception ex) {
            log.error("更新状态失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<String> deleteTeacher(RecordIdQueryVo queryVo) {
        try {
            Teacher teacher = teacherService.selectByRecordId(queryVo.getRecordId());
            Assert.notNull(teacher, "Teacher cannot be empty");
            Assert.isTrue(BooleanUtil.isTrue(teacher.getEnabledStatus()), "The teacher is in the enabled state and cannot be deleted");
            Teacher newTeacher = new Teacher();
            newTeacher.setDeleted(true);
            newTeacher.setId(teacher.getId());
            teacherService.updateEntity(newTeacher);
            return new RespVo<>("更新状态成功");
        } catch (Exception ex) {
            log.error("更新状态失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public List<SelectValueVo> groupStatus(RecordIdQueryVo queryVo) {
        Teacher teacher = teacherService.selectByRecordId(queryVo.getRecordId());
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo(CourseTypeEnum.SINGLE.name(), CourseTypeEnum.SINGLE.name()));
        selectValueVos.add(new SelectValueVo(CourseTypeEnum.TEST.name(), CourseTypeEnum.TEST.name()));
        if (BooleanUtil.isTrue(teacher.getGroupStatus())) {
            selectValueVos.add(new SelectValueVo(CourseTypeEnum.GROUP.name(), CourseTypeEnum.GROUP.name()));
        }
        return selectValueVos;
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


    public List<TeacherLastCommentRespVo> teacherLastCommentRespVo(String userCode, RecordIdQueryVo queryVo) {
        String userId = StringUtils.hasText(queryVo.getRecordId()) ? queryVo.getRecordId() : userCode;
        List<TeacherEvaluationRecord> list = teacherEvaluationService.selectByTeacherIdLimit20(userId);
        return list.stream().map(TeacherConverter.INSTANCE::toCommentVo).toList();
    }

    public RespVo<TeacherDashboardRespVo> dashboard(String userCode) {
        try {
            Teacher teacher = teacherService.selectByRecordId(userCode);
            Assert.notNull(teacher, "Teacher cannot be empty");
            TeacherDashboardRespVo respVo = TeacherConverter.INSTANCE.toTeacherDashboard(teacher);
            List<StudentClass> studentClasses = studentClassService.selectClassStatusGroupByParams(userCode);
            //查询老师当月已确认完成的课程数量
            long confirmedComplete = studentClasses.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.FINISH.getStatus())).count();
            respVo.setConfirmedQty(new BigDecimal(confirmedComplete));
            //查询老师当月已取消的课程数量
            long cancelledComplete = studentClasses.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.CANCEL.getStatus())).count();
            respVo.setCancelledQty(new BigDecimal(cancelledComplete));
            //查询老师当月被投诉的课程数量
            respVo.setComplaintsQty(BigDecimal.ZERO);
            //查询老师当月已确认课程薪资
            BigDecimal confirmedClassesAmount = studentClasses.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.FINISH.getStatus())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice())).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
            respVo.setConfirmedClassesAmount(confirmedClassesAmount);
            //查询老师当月已取消扣款
            respVo.setCancelledDeductionsAmount(BigDecimal.ZERO);
            //查询老师当月投诉扣款
            respVo.setComplaintDeductionsAmount(BigDecimal.ZERO);
            //查询老师奖金
            respVo.setBonus(BigDecimal.ZERO);
            //查询管理老师佣金
            respVo.setBonus(BigDecimal.ZERO);
            //总薪资
            respVo.setTotalSalary(respVo.getTotalSalary());
            return new RespVo<>(respVo);
        } catch (Exception ex) {
            log.error("Query Error", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    public RespVo<List<SharedTeacherListRespVo>> sharedTeacherList() {
        List<Teacher> teacherList = teacherService.selectListByAll();
        List<SharedTeacherListRespVo> respVos = teacherList.stream().map(item -> {
            SharedTeacherListRespVo sharedTeacherListRespVo = TeacherConverter.INSTANCE.toSharedTeacherListRespVo(item);
            sharedTeacherListRespVo.setAvatarUtl(downloadFile(item.getAvatarUrl()));
            sharedTeacherListRespVo.setVideoUrl(downloadFile(item.getVideoUrl()));
            return sharedTeacherListRespVo;
        }).toList();
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
            String downloadAvatar = downloadFile(teacher.getAvatarUrl());
            String downloadVideo = downloadFile(teacher.getVideoUrl());
            teacher.setAvatarUrl(downloadAvatar);
            teacher.setVideoUrl(downloadVideo);
            Assert.notNull(teacher, "Teacher information not obtained");
            TeacherInfoRespVo respVo = TeacherConverter.INSTANCE.toTeacherInfo(teacher);
            respVo.setFileRecordVos(getFileRecordVoList(queryVo.getRecordId(), FileTypeEnum.CERTIFICATE.getFileType()));
            List<TeacherFeature> list = teacherFeatureService.selectByTeacherId(teacher.getRecordId());
            respVo.setSpecialties(list.stream().map(TeacherFeature::getTextbookName).toList());
            return new RespVo<>(respVo);
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