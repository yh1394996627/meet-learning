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
    private final TeacherSalaryService teacherSalaryService;
    private final TeacherSalaryPcService teacherSalaryPcService;

    public RespVo<PageVo<TeacherListRespVo>> teacherPage(String userCode, TeacherQueryVo queryVo) {
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, getHint(LanguageContextEnum.USER_NOTNULL));
        Map<String, Object> params = queryVo.getParams();
        //如果是老师账号查询管理的老师
        if (StringUtils.pathEquals(accountUser.getType(), RoleEnum.TEACHER.name())) {
            params.put("managerId", userCode);
        }
        List<SelectValueVo> baseConfigs = baseConfigService.selectByType(ConfigTypeEnum.COUNTRY.name());
        Map<String, String> countryMap = baseConfigs.stream().collect(Collectors.toMap(SelectValueVo::getValue, SelectValueVo::getLabel));
        Page<Teacher> teacherPage = teacherService.selectPageParams(params, queryVo.getPageRequest());
        List<String> teacherIds = teacherPage.getRecords().stream().map(Teacher::getRecordId).distinct().toList();
        List<TeacherSalary> salaryList = teacherSalaryService.selectByUnVerTeacherIds(teacherIds);
        Map<String, TeacherSalary> salaryMap = salaryList.stream().collect(Collectors.toMap(TeacherSalary::getTeacherId, teacherSalary -> teacherSalary));
        PageVo<TeacherListRespVo> pageVo = PageVo.map(teacherPage, list -> {
            TeacherListRespVo respVo = TeacherConverter.INSTANCE.toListVo(list);
            if (countryMap.containsKey(list.getCountry())) {
                respVo.setCountry(countryMap.get(list.getCountry()));
            }
            if (salaryMap.containsKey(list.getRecordId())) {
                TeacherSalary salary = salaryMap.get(list.getRecordId());
                respVo.setConfirmed(BigDecimalUtil.add(salary.getConfirmedQty(), salary.getGroupConfirmedQty()));
                respVo.setOneStar(BigDecimalUtil.add(salary.getOneStarQty(), salary.getGroupOneStarQty()));
                respVo.setAbsent(BigDecimalUtil.add(salary.getAbsentQty(), salary.getGroupAbsentQty()));
                BigDecimal amount = BigDecimalUtil.nullOrZero(salary.getConfirmedAmount())
                        .subtract(BigDecimalUtil.nullOrZero(salary.getAbsentAmount()))
                        .subtract(BigDecimalUtil.nullOrZero(salary.getDeductionAmount()))
                        .subtract(BigDecimalUtil.nullOrZero(salary.getOneStarAmount()));
                respVo.setSalary(amount);
            }
            return respVo;
        });
        return new RespVo<>(pageVo);
    }

    public PageVo<TeacherInfoRespVo> teacherPcPage(TeacherPcQueryVo queryVo) {
        List<String> teacherIds = new ArrayList<>();
        //查询时间段有空的老师
        ScheduleWeekEnum weekEnum = queryVo.getWeek();
        List<String> teacher1Ids = teacherScheduleService.selectTeacherIdByWeekNumAndTime(weekEnum.name(), queryVo.getBeginTime(), queryVo.getEndTime());
        teacher1Ids = teacher1Ids.stream().distinct().toList();
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
        List<String> teacherRecordIds = teacherPage.getRecords().stream().map(Teacher::getRecordId).distinct().toList();
        List<TeacherFeature> teacherFeatures = teacherFeatureService.selectByTeacherIds(teacherRecordIds);
        Map<String, List<TeacherFeature>> textbookMap = teacherFeatures.stream().collect(Collectors.groupingBy(TeacherFeature::getTeacherId));

        PageVo<TeacherInfoRespVo> pageVo = PageVo.map(teacherPage, list -> {
            TeacherInfoRespVo respVo = TeacherConverter.INSTANCE.toTeacherInfo(list);
            respVo.setAvatarUrl(downloadFile(list.getAvatarUrl()));
            respVo.setVideoUrl(downloadFile(list.getVideoUrl()));
            if (textbookMap.containsKey(list.getRecordId())) {
                List<TeacherFeature> features = textbookMap.get(list.getRecordId());
                List<String> textbookNames = features.stream().map(TeacherFeature::getTextbookName).distinct().toList();
                respVo.setTextbooks(textbookNames);
            }
            return respVo;
        });
        return pageVo;
    }

    public RespVo<List<SelectValueVo>> teacherManagerSearch(TeacherQueryVo queryVo) {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("ALL", "ALL"));
        selectValueVos.add(new SelectValueVo("NO MANAGER", "NO MANAGER"));
        List<SelectValueVo> searchVos = teacherService.selectGroupManager(queryVo.getParams());
        selectValueVos.addAll(searchVos);
        return new RespVo<>(selectValueVos);
    }

    public RespVo<List<SelectValueVo>> teacherUpdateManagerSearch(TeacherQueryVo queryVo) {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("", "None"));
        List<SelectValueVo> searchVos = teacherService.selectAllGroupManager(queryVo.getParams());
        selectValueVos.addAll(searchVos);
        return new RespVo<>(selectValueVos);
    }


    public RespVo<List<SelectValueVo>> teacherCountrySearch(TeacherQueryVo queryVo) {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo("ALL", "ALL"));
        List<SelectValueVo> searchVos = teacherService.selectGroupCountry(queryVo.getParams());
        selectValueVos.addAll(searchVos);
        return new RespVo<>(selectValueVos);
    }

    public RespVo<String> teacherAdd(String userCode, String userName, TeacherAddReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getEmail()), "Email" + getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Assert.isTrue(StringUtils.hasText(reqVo.getPassword()), "Password" + getHint(LanguageContextEnum.OBJECT_NOTNULL));
        //判断邮箱是否存在
        User user = userService.selectByAccountCode(reqVo.getEmail());
        Assert.isNull(user, getHint(LanguageContextEnum.USER_EXIST) + "【" + reqVo.getEmail() + "】");
        Teacher teacher = TeacherConverter.INSTANCE.toCreateTeacher(userCode, userName, reqVo);
        teacherService.insertEntity(teacher);
        //创建登陆帐号
        User newUser = addUser(userCode, userName, teacher.getRecordId(), teacher.getEmail(), reqVo.getPassword(),
                RoleEnum.TEACHER, teacher.getName(), teacher.getEnName(), teacher.getEmail(), null);

        //创建用户关联的课时币
        addFinance(userCode, userName, newUser);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> teacherUpdate(String userCode, String userName, TeacherUpdateReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        //判断邮箱是否存在
        User user = userService.selectByAccountCode(reqVo.getEmail());
        Assert.isTrue(user == null || StringUtils.pathEquals(user.getRecordId(), reqVo.getRecordId()), getHint(LanguageContextEnum.USER_EXIST) + "【" + reqVo.getEmail() + "】");

        Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        teacher = TeacherConverter.INSTANCE.toUpdateTeacher(userCode, userName, teacher, reqVo);
        if (StringUtils.hasText(teacher.getCurrencyCode())) {
            BaseConfig baseConfig = baseConfigService.selectByCode(teacher.getCurrencyCode());
            teacher.setCurrencyCode(baseConfig != null ? baseConfig.getCode() : null);
        }
        teacherService.updateEntity(teacher);
        //清掉原有特点并重新写入
        teacherFeatureService.deleteByTeacherId(teacher.getRecordId());
        Teacher finalTeacher = teacher;
        List<String> specialists = reqVo.getSpecialties();
        if (!CollectionUtils.isEmpty(specialists)) {
            Map<String, Object> params = new HashMap<>();
            params.put("names", specialists);
            List<Textbook> textbooks = textbookService.selectByParams(params);
            List<TeacherFeature> features = textbooks.stream().map(feature -> TeacherConverter.INSTANCE.toTeacherFeature(userCode, finalTeacher.getRecordId(), feature)).toList();
            if (!CollectionUtils.isEmpty(features)) {
                teacherFeatureService.insertBatch(features);
            }
        }
        //更新用户表数据
        updateBaseDate(teacher.getRecordId(), teacher.getName(), teacher.getEmail());
        //更新课程数据
        studentClassService.updateClassEntityByTeacher(teacher.getName(), teacher.getRecordId());
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> managerStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
        teacher.setUpdator(userCode);
        teacher.setUpdateName(userName);
        teacher.setManagerStatus(BooleanUtil.isTrue(reqVo.getStatus()));
        teacherService.updateEntity(teacher);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    public RespVo<String> managerAccountStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
        teacher.setUpdator(userCode);
        teacher.setUpdateName(userName);
        teacher.setEnabledStatus(BooleanUtil.isTrue(reqVo.getStatus()));
        teacherService.updateEntity(teacher);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    public RespVo<String> groupStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
        Teacher newTeacher = new Teacher();
        newTeacher.setId(teacher.getId());
        newTeacher.setUpdator(userCode);
        newTeacher.setUpdateName(userName);
        newTeacher.setGroupStatus(BooleanUtil.isTrue(reqVo.getStatus()));
        teacherService.updateEntity(newTeacher);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> deleteTeacher(RecordIdQueryVo queryVo) {
        Teacher teacher = teacherService.selectByRecordId(queryVo.getRecordId());
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Assert.isTrue(!BooleanUtil.isTrue(teacher.getEnabledStatus()), getHint(LanguageContextEnum.TEACHER_CAN_DELETED));
        Teacher newTeacher = new Teacher();
        newTeacher.setDeleted(true);
        newTeacher.setId(teacher.getId());
        teacherService.updateEntity(newTeacher);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public List<SelectValueVo> groupStatus(RecordIdQueryVo queryVo) {
        Teacher teacher = teacherService.selectByRecordId(queryVo.getRecordId());
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        selectValueVos.add(new SelectValueVo(CourseTypeEnum.SINGLE.name(), CourseTypeEnum.SINGLE.name()));
        if (BooleanUtil.isTrue(teacher.getTestStatus())) {
            selectValueVos.add(new SelectValueVo(CourseTypeEnum.TEST.name(), CourseTypeEnum.TEST.name()));
        }
        if (BooleanUtil.isTrue(teacher.getGroupStatus())) {
            selectValueVos.add(new SelectValueVo(CourseTypeEnum.GROUP.name(), CourseTypeEnum.GROUP.name()));
        }
        return selectValueVos;
    }


    public RespVo<String> teacherTypeStatusSet(String userCode, String userName, TeacherStatusReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
        teacher.setUpdator(userCode);
        teacher.setUpdateName(userName);
        teacher.setTestStatus(BooleanUtil.isTrue(reqVo.getStatus()));
        teacherService.updateEntity(teacher);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    public RespVo<TeacherTotalRespVo> teacherTotal(TeacherQueryVo queryVo) {
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
    }


    public List<TeacherLastCommentRespVo> teacherLastCommentRespVo(String userCode, RecordIdQueryVo queryVo) {
        String userId = StringUtils.hasText(queryVo.getRecordId()) ? queryVo.getRecordId() : userCode;
        List<TeacherEvaluationRecord> list = teacherEvaluationService.selectByTeacherIdLimit20(userId);
        return list.stream().map(TeacherConverter.INSTANCE::toCommentVo).toList();
    }

    public RespVo<TeacherDashboardRespVo> dashboard(String userCode, String userName) {
        Teacher teacher = teacherService.selectByRecordId(userCode);
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        TeacherDashboardRespVo respVo = TeacherConverter.INSTANCE.toTeacherDashboard(teacher);
        List<StudentClass> studentClasses = studentClassService.selectClassStatusGroupByParams(userCode);
        respVo.setConfirmedQty(BigDecimal.ZERO);
        respVo.setComplaintsQty(BigDecimal.ZERO);
        respVo.setAbsentQty(BigDecimal.ZERO);
        respVo.setBonus(BigDecimal.ZERO);
        respVo.setConfirmedClassesAmount(BigDecimal.ZERO);
        respVo.setCancelledDeductionsAmount(BigDecimal.ZERO);
        respVo.setComplaintDeductionsAmount(BigDecimal.ZERO);
        respVo.setAbsentAmount(BigDecimal.ZERO);
        TeacherSalary teacherSalary = teacherSalaryService.selectByUnVerTeacherId(userCode);
        if (teacherSalary != null) {
            respVo.setConfirmedQty(BigDecimalUtil.add(teacherSalary.getConfirmedQty(), teacherSalary.getGroupConfirmedQty()));
            respVo.setComplaintsQty(BigDecimalUtil.add(teacherSalary.getOneStarQty(), teacherSalary.getGroupOneStarQty()));
            respVo.setCancelledDeductionsAmount(BigDecimalUtil.add(teacherSalary.getDeductionQty(), teacherSalary.getGroupDeductionQtyQty()));
            respVo.setComplaintDeductionsAmount(teacherSalary.getOneStarAmount());
            respVo.setConfirmedClassesAmount(teacherSalary.getConfirmedAmount());
            respVo.setAbsentQty(teacherSalary.getAbsentQty());
            respVo.setAbsentAmount(teacherSalary.getAbsentAmount());
        }
        //查询老师当月已取消的课程数量
        long cancelledComplete = studentClasses.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.CANCEL.getStatus())).count();
        respVo.setCancelledQty(new BigDecimal(cancelledComplete));
        //查询管理老师佣金
        respVo.setBonus(BigDecimal.ZERO);
        //总薪资
        respVo.setTotalSalary(respVo.getTotalSalary());
        return new RespVo<>(respVo);
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
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        teacher.setPrice(reqVo.getPrice());
        teacherService.updateEntity(teacher);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<TeacherInfoRespVo> teacherInfo(RecordIdQueryVo queryVo) {
        String recordId = queryVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(recordId);
        String downloadAvatar = downloadFile(teacher.getAvatarUrl());
        String downloadVideo = downloadFile(teacher.getVideoUrl());
        teacher.setAvatarUrl(downloadAvatar);
        teacher.setVideoUrl(downloadVideo);
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        TeacherInfoRespVo respVo = TeacherConverter.INSTANCE.toTeacherInfo(teacher);
        respVo.setFileRecordVos(getFileRecordVoList(queryVo.getRecordId(), FileTypeEnum.CERTIFICATE.getFileType()));
        List<TeacherFeature> list = teacherFeatureService.selectByTeacherId(teacher.getRecordId());
        respVo.setSpecialties(list.stream().map(TeacherFeature::getTextbookName).toList());
        return new RespVo<>(respVo);
    }

    public RespVo<List<SelectValueVo>> teacherPcCountrySearch() {
        List<SelectValueVo> selectValueVos = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        List<SelectValueVo> searchVos = teacherService.selectGroupCountry(params);
        selectValueVos.addAll(searchVos);
        return new RespVo<>(selectValueVos);
    }
}