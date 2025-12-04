package org.example.meetlearning.service;

import java.util.Date;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.StudentConverter;
import org.example.meetlearning.converter.TeacherConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.enums.LanguageContextEnum;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.*;
import org.example.meetlearning.vo.teacher.TeacherInfoRespVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.management.relation.Role;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class StudentPcService extends BasePcService {

    private final StudentService studentService;

    private final UserService userService;

    private final UserFinanceService userFinanceService;

    private final UserFinanceRecordService userFinanceRecordService;

    private final TeacherService teacherService;

    private final StudentClassService studentClassService;

    /**
     * 学生信息分页查询
     *
     * @param queryVo
     * @return
     */
    public RespVo<PageVo<StudentListRespVo>> studentPage(String userCode, StudentRequestQueryVo queryVo) {
        Map<String, Object> params = queryVo.getParams();
        User user = userService.selectByRecordId(userCode);
        if (StringUtils.pathEquals(RoleEnum.AFFILIATE.name(), user.getType())) {
            params.put("affiliateIds", List.of(userCode));
        }
        Page<Student> page = studentService.findPageByParams(params, queryVo.getPageRequest());
        List<String> userIds = page.getRecords().stream().map(Student::getRecordId).toList();
        Map<String, UserFinance> userFinanceMap;
        Map<String, UserFinanceRecord> userFinanceRecordHashMap;
        if (!CollectionUtils.isEmpty(userIds)) {
            //获取学生课时币信息
            List<UserFinance> userFinances = userFinanceService.selectByUserIds(userIds);
            userFinanceMap = userFinances.stream().collect(Collectors.toMap(UserFinance::getUserId, Function.identity()));

            List<UserFinanceRecord> userFinanceRecordList = userFinanceRecordService.selectDateGroupByUserIds(userIds);
            userFinanceRecordHashMap = userFinanceRecordList.stream().collect(Collectors.toMap(UserFinanceRecord::getUserId, Function.identity()));
        } else {
            userFinanceRecordHashMap = new HashMap<>();
            userFinanceMap = new HashMap<>();
        }

        //组装返回数据
        PageVo<StudentListRespVo> pageVo = PageVo.map(page, list -> {
            StudentListRespVo respVo = StudentConverter.INSTANCE.toStudentListRespVo(list);
            if (userFinanceMap.containsKey(list.getRecordId())) {
                UserFinance userFinance = userFinanceMap.get(list.getRecordId());
                respVo.setBalance(BigDecimalUtil.nullOrZero(userFinance.getBalanceQty()));
                respVo.setIsDeleted(BigDecimalUtil.eqZero(respVo.getBalance()));
            }
            if (userFinanceRecordHashMap.containsKey(list.getRecordId())) {
                UserFinanceRecord userFinanceRecord = userFinanceRecordHashMap.get(list.getRecordId());
                respVo.setExpirationTime(userFinanceRecord.getExpirationTime());
            }
            return respVo;
        });
        return new RespVo<>(pageVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public RespVo<String> studentAdd(String userCode, String userName, StudentAddReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getEnName()), "Email" + getHint(LanguageContextEnum.OBJ_NOTNULL));
        Assert.isTrue(StringUtils.hasText(reqVo.getPassword()), "Password" + getHint(LanguageContextEnum.OBJ_NOTNULL));
        //判断邮箱是否存在
        User user = userService.selectByAccountCode(reqVo.getEmail());
        Assert.isNull(user, getHint(LanguageContextEnum.USER_EXIST) + "【" + reqVo.getEmail() + "】");
        Student student = StudentConverter.INSTANCE.toCreateStudent(userCode, userName, reqVo);
        //如果是代理商 则添加代理商信息
        User managerUser = userService.selectByRecordId(userCode);
        if (managerUser != null && StringUtils.pathEquals(RoleEnum.AFFILIATE.name(), managerUser.getType())) {
            student.setAffiliateId(managerUser.getRecordId());
            student.setAffiliateName(managerUser.getName());
        }
        studentService.save(student);

        //创建登陆帐号
        String managerId = StringUtils.hasText(student.getAffiliateId()) ? student.getAffiliateId() : null;
        User newUser = addUser(userCode, userName, student.getRecordId(), student.getEmail(), reqVo.getPassword(),
                RoleEnum.STUDENT, student.getName(), student.getName(), student.getEmail(), managerId);

        //创建用户关联的课时币
        addFinance(userCode, userName, newUser);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> studentUpdate(String userCode, String userName, StudentUpdateReqVo reqVo) {
        String recordId = reqVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Student student = studentService.findByRecordId(recordId);
        String oldManagerId = student.getAffiliateId();
        student = StudentConverter.INSTANCE.toUpdateStudent(student, reqVo);
        if (StringUtils.hasText(reqVo.getAffiliateId())) {
            User affiliateUser = userService.selectByRecordId(reqVo.getAffiliateId());
            Assert.notNull(affiliateUser, getHint(LanguageContextEnum.OBJECT_NOTNULL));
            student.setAffiliateId(affiliateUser.getRecordId());
            student.setAffiliateName(affiliateUser.getName());
        }
        student.setUpdator(userCode);
        student.setUpdateName(userName);
        student.setUpdateTime(new Date());
        studentService.update(student);
        //更新user代理商
        if (!org.apache.commons.lang3.StringUtils.equals(oldManagerId, student.getAffiliateId()) && StringUtils.hasText(student.getAffiliateId())) {
            User user = userService.selectByRecordId(recordId);
            User newUser = new User();
            newUser.setManagerId(student.getAffiliateId());
            newUser.setId(user.getId());
            userService.updateEntity(newUser);
        }
        //更新用户表数据
        updateBaseDate(student.getRecordId(), student.getName(), student.getEmail());
        //更新课程数据
        studentClassService.updateClassEntityByStudent(student.getName(), student.getRecordId());
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> deleteStudent(String userCode,StudentRecordReqVo reqVo) {
        User user = userService.selectByRecordId(userCode);
        Assert.isTrue(org.codehaus.plexus.util.StringUtils.equals(user.getEmail(), "admin@talk.com"), getHint(LanguageContextEnum.AUTO_DELETE));
        String recordId = reqVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        studentService.deletedByRecordId(recordId);
        //删除登陆帐号和余额信息
        deleteUser(recordId);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> studentRemarkUpdate(StudentRemarkUpdateReqVo reqVo) {
        String recordId = reqVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Student student = studentService.findByRecordId(recordId);
        student.setRemark(reqVo.getRemark());
        studentService.update(student);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    public RespVo<StudentInfoRespVo> studentInfo(RecordIdQueryVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Student student = studentService.findByRecordId(reqVo.getRecordId());
        Assert.notNull(student, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        StudentInfoRespVo respVo = StudentConverter.INSTANCE.toStudentInfoRespVo(student);
        UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
        Assert.notNull(userFinance, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        respVo.setBalance(userFinance.getBalanceQty());
        respVo.setExpirationTime(userFinance.getExpirationTime());
        if (StringUtils.hasText(student.getAvatarUrl())) {
            respVo.setAvatarUrl(downloadFile(student.getAvatarUrl()));
        }
        return new RespVo<>(respVo);
    }

    public List<TeacherInfoRespVo> dashboardTeacher(StudentDashboardTeacherQueryVo queryVo) {
        List<Teacher> teacherList = new ArrayList<>();
        if (queryVo.getType() == 1) {
            teacherList = teacherService.selectTop5ByRating();
        } else {
            teacherList = teacherService.selectTop5ByQty();
        }
        return teacherList.stream().map(item -> {
            TeacherInfoRespVo respVo = TeacherConverter.INSTANCE.toTeacherInfo(item);
            respVo.setAvatarUrl(downloadFile(item.getAvatarUrl()));
            respVo.setVideoUrl(downloadFile(item.getVideoUrl()));
            return respVo;
        }).toList();
    }


    public List<StudentDashboardClassRespVo> dashboardNowClass(String recordId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", recordId);
        List<StudentClass> studentClassList = studentClassService.selectByParams(params);
        studentClassList = studentClassList.stream().sorted(Comparator.comparing(StudentClass::getCourseTime)).toList();
        List<StudentDashboardClassRecordRespVo> studentDashboardClassRecordRespVoList = studentClassList.stream().map(studentClass -> {
            StudentDashboardClassRecordRespVo respVo = new StudentDashboardClassRecordRespVo();
            respVo.setDate(studentClass.getCourseTime());
            respVo.setRecordId(studentClass.getRecordId());
            respVo.setCourseName(studentClass.getCourseName());
            respVo.setCourseTime(DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd " + studentClass.getBeginTime() + "-" + studentClass.getEndTime()));
            CourseStatusEnum courseStatus = CourseStatusEnum.getCourseStatusByType(studentClass.getClassStatus());
            if (courseStatus != null) {
                respVo.setCourseStatus(courseStatus.getEntRemark());
            }
            return respVo;
        }).toList();
        Map<Date, List<StudentDashboardClassRecordRespVo>> map = studentDashboardClassRecordRespVoList.stream().collect(Collectors.groupingBy(StudentDashboardClassRecordRespVo::getDate));
        List<StudentDashboardClassRespVo> result = new ArrayList<>();
        map.forEach((key, value) -> {
            StudentDashboardClassRespVo respVo = new StudentDashboardClassRespVo();
            respVo.setDate(key);
            respVo.setRespVos(value);
            result.add(respVo);
        });
        return result;
    }

}
