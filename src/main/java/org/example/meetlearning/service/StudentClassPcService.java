package org.example.meetlearning.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.MeetingConverter;
import org.example.meetlearning.converter.StudentClassConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.enums.ScheduleWeekEnum;
import org.example.meetlearning.enums.TokenContentEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.classes.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.evaluation.TeacherComplaintReqVo;
import org.example.meetlearning.vo.evaluation.TeacherEvaluationReqVo;
import org.example.meetlearning.vo.student.StudentInfoRespVo;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class StudentClassPcService extends BasePcService {

    private final StudentClassService studentClassService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final AffiliateService affiliateService;

    private final TeacherScheduleService teacherScheduleService;

    private final TeacherFeatureService teacherFeatureService;

    private final UserFinanceService userFinanceService;

    private final UserFinanceRecordService userFinanceRecordService;

    private final UserService userService;

    private final TeacherEvaluationService teacherEvaluationService;

    private final TeacherComplaintService teacherComplaintService;

    private final ZoomOAuthService zoomOAuthService;

    private final StudentClassMeetingService studentClassMeetingService;


    public RespVo<PageVo<StudentClassListRespVo>> studentClassPage(StudentClassQueryVo queryVo) {
        Page<StudentClass> page = studentClassService.selectByParams(queryVo.getParams(), queryVo.getPageRequest());
        List<String> userIds = page.getRecords().stream().map(StudentClass::getStudentId).toList();
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

        PageVo<StudentClassListRespVo> pageVo = PageVo.map(page, list -> {
            StudentClassListRespVo respVo = StudentClassConverter.INSTANCE.toStudentClassListRespVo(list);
            if (userFinanceMap.containsKey(list.getStudentId())) {
                UserFinance userFinance = userFinanceMap.get(list.getStudentId());
                respVo.setStudentConsumption(BigDecimalUtil.nullOrZero(userFinance.getConsumptionQty()));
                respVo.setStudentBalance(BigDecimalUtil.nullOrZero(userFinance.getBalanceQty()));
            }
            if (userFinanceRecordHashMap.containsKey(list.getStudentId())) {
                UserFinanceRecord userFinanceRecord = userFinanceRecordHashMap.get(list.getStudentId());
                respVo.setEfficientDate(userFinanceRecord.getExpirationTime());
            }
            return respVo;
        });
        return new RespVo<>(pageVo);
    }


    /**
     * 新增预约课程
     * 1.校验
     * 2.拉取老师学生信息组装数据
     * 3.学生扣掉课时币，添加扣除记录
     * 4.创建会议,生产会议链接
     */
    public RespVo<String> studentClassAdd(String entCode, String userCode, String userName, StudentClassAddReqVo reqVo) throws IOException {
        //1.校验 2.拉取老师学生信息组装数据
        //查询学生信息
        Student student = reqVo.getStudentId() != null ? studentService.findByRecordId(reqVo.getStudentId()) : null;
        Assert.notNull(student, "Student information not obtained");
        //查询老师信息
        Teacher teacher = reqVo.getTeacherId() != null ? teacherService.selectByRecordId(reqVo.getTeacherId()) : null;
        Assert.notNull(teacher, "Teacher information not obtained");
        //查询代理商信息
        Affiliate affiliate = null;
        if (student != null && StringUtils.isNotEmpty(student.getAffiliateId())) {
            affiliate = affiliateService.findByRecordId(student.getAffiliateId());
        }
        Assert.isTrue(reqVo.getCourseType() != null, "Course type cannot be empty");
        //3.新增课时币学生扣减记录
        operaTokenLogs(userCode, userName, student.getRecordId(), teacher.getPrice(), TokenContentEnum.COURSE_CLASS.getEnContent());
        UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
        StudentClass studentClass = StudentClassConverter.INSTANCE.toCreate(entCode, userCode, reqVo, student, teacher, affiliate, userFinance);
        List<TeacherFeature> features = teacherFeatureService.selectByTeacherId(teacher.getRecordId());
        if (!CollectionUtils.isEmpty(features)) {
            List<String> courseList = features.stream().map(TeacherFeature::getSpecialists).toList();
            studentClass.setCourseName(StringUtils.join(courseList.toArray(), ","));
        }
        Date meetingDate = DateUtil.parse(DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd") + " " + studentClass.getBeginTime(), "yyyy-MM-dd HH:mm");
        //创建会议
        String meeting = zoomOAuthService.createMeeting(teacher, studentClass.getRecordId(), DateUtil.format(meetingDate, "yyyy-MM-dd HH:mm"), CourseTypeEnum.valueOf(studentClass.getCourseType()));
        JSONObject meetObj = new JSONObject(meeting);
        StudentClassMeeting meetingEntity = studentClassMeetingService.insertMeeting(entCode, userCode, meetObj);

        studentClass.setMeetingRecordId(meetingEntity.getMeetUuid());
        studentClassService.insertEntity(studentClass);
        return new RespVo<>("New successfully added");
    }

    public RespVo<List<SelectValueVo>> classCoinList() {
        List<BigDecimal> priceList = teacherService.priceGroupList();
        List<SelectValueVo> selectValueVos = priceList.stream().map(price -> new SelectValueVo(price.toString(), "Any teacher with " + price + " tokens")).toList();
        return new RespVo<>(selectValueVos);
    }

    public RespVo<List<SelectValueVo>> classTeacherList(StudentClassCommonQueryVo queryVo) {
        Map<String, Object> params = queryVo.getParams();
        if (StringUtils.isNotEmpty(queryVo.getCourseTime())) {
            String[] arr = StringUtils.split(queryVo.getCourseTime(), "-");
            ScheduleWeekEnum week = ScheduleWeekEnum.getByDate(queryVo.getCourseDate());
            List<String> teacherIds = null;
            if (week != null) {
                teacherIds = teacherScheduleService.selectTeacherIdByWeekNumAndTime(week.name(), arr[0], arr[1]);
            }
            if (teacherIds != null) {
                teacherIds = teacherIds.stream().distinct().toList();
            }
            if (CollectionUtils.isEmpty(teacherIds)) {
                return new RespVo<>(List.of());
            }
            params.put("recordIds", teacherIds);
        }
        List<Teacher> teachers = teacherService.selectListParams(params);
        List<SelectValueVo> selectValueVos = teachers.stream().map(teacher -> new SelectValueVo(teacher.getRecordId(), teacher.getName())).toList();
        return new RespVo<>(selectValueVos);
    }

    public RespVo<List<String>> classTimeList(StudentClassCommonQueryVo queryVo) {
        List<Teacher> teachers = teacherService.selectListParams(queryVo.getParams());
        List<String> teacherIds = teachers.stream().map(Teacher::getRecordId).toList();
        if (CollectionUtils.isEmpty(teacherIds)) {
            return new RespVo<>(List.of());
        }
        List<TeacherSchedule> teacherSchedules = teacherScheduleService.selectGroupTimeByParams(queryVo.getScheduleParams());
        List<String> resultList = teacherSchedules.stream().map(schedule -> schedule.getBeginTime() + "-" + schedule.getEndTime()).toList();
        return new RespVo<>(resultList);
    }

    public RespVo<StudentClassTotalRespVo> classTotalList(StudentClassQueryVo queryVo) {
        Long cancelTotal = studentClassService.selectCancelByParams(queryVo.getParams());
        Long completeTotal = studentClassService.selectCompleteByParams(queryVo.getParams());
        return new RespVo<>(new StudentClassTotalRespVo(new BigDecimal(completeTotal), new BigDecimal(cancelTotal)));
    }

    /**
     * 老师或者管理员取消预约课程提醒
     * 学生变更时间提醒
     */
    public Boolean studentClassCancelOrUpdateVerify(String userCode, RecordIdQueryVo reqVo) {
        User user = userService.selectByRecordId(userCode);
        Assert.notNull(user, "User information not obtained userCode:" + userCode);
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, "Course information not obtained");
        if (StringUtils.equals(RoleEnum.TEACHER.name(), user.getType())) {
            Date classDate = studentClass.getCourseTime();
            long diffInMillie = Math.abs(classDate.getTime() - new Date().getTime());
            long dayNum = TimeUnit.DAYS.convert(diffInMillie, TimeUnit.MILLISECONDS);
            return BooleanUtil.isTrue(dayNum >= 3);
        } else if (StringUtils.equals(RoleEnum.STUDENT.name(), user.getType())) {
            Date classDate = studentClass.getCourseTime();
            long diffInMillie = Math.abs(classDate.getTime() - new Date().getTime());
            long dayNum = TimeUnit.HOURS.convert(diffInMillie, TimeUnit.MILLISECONDS);
            return BooleanUtil.isTrue(dayNum >= 3);
        } else {
            return true;
        }
    }

    /**
     * 老师或者管理员取消预约课程
     * 1.提前三天才能取消，三天内取消罚50%
     * 2.记录取消时间，取消人，和取消状态
     */
    public void studentClassCancel(String userCode, RecordIdQueryVo reqVo) {
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, "Course information not obtained");
        //todo 提前三天才能取消，三天内取消罚50%
        StudentClass newStudentClass = new StudentClass();
        newStudentClass.setId(studentClass.getId());
        newStudentClass.setCancelId(userCode);
        newStudentClass.setCancelTime(new Date());
        studentClassService.updateEntity(newStudentClass);
    }

    public void studentClassUpdateTime(String userCode, String userName, StudentClassUpdateTimeReqVo reqVo) {
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, "Course information not obtained");
        Date classDate = studentClass.getCourseTime();
        long diffInMillie = Math.abs(classDate.getTime() - new Date().getTime());
        long dayNum = TimeUnit.HOURS.convert(diffInMillie, TimeUnit.MILLISECONDS);
        Assert.notNull(BooleanUtil.isTrue(dayNum >= 3), "There are still 3 hours left until the start of the course and the time cannot be changed");
        StudentClass newStudentClass = new StudentClass();
        newStudentClass.setId(studentClass.getId());
        newStudentClass.setCourseTime(reqVo.getCourseDate());
        newStudentClass.setBeginTime(reqVo.getBeginTime());
        newStudentClass.setEndTime(reqVo.getEndTime());
        newStudentClass.setUpdateTime(new Date());
        newStudentClass.setUpdator(userCode);
        newStudentClass.setUpdateName(userName);
        studentClassService.updateEntity(newStudentClass);
    }


    public void studentClassEvaluate(String userCode, TeacherEvaluationReqVo reqVo) {
        //新增评论
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, "Course information not obtained");
        TeacherEvaluationRecord teacherEvaluationRecord = StudentClassConverter.INSTANCE.toCreateTeacherEvaluationRecord(userCode, reqVo.getRating(), reqVo.getRemark(), studentClass);
        teacherEvaluationService.insert(teacherEvaluationRecord);

        //更新老师星级
        BigDecimal rating = teacherEvaluationService.selectRatingByTeacherId(studentClass.getTeacherId());
        Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
        Assert.notNull(teacher, "Teacher information not obtained");
        teacher.setRating(BigDecimalUtil.nullOrZero(rating));
        Teacher newTeacher = new Teacher();
        newTeacher.setId(teacher.getId());
        newTeacher.setRating(BigDecimalUtil.nullOrZero(rating));
        teacherService.updateEntity(newTeacher);

        studentClass.setIsEvaluation(true);
        studentClassService.updateEntity(studentClass);
    }

    public void studentClassComplaint(String userCode, TeacherComplaintReqVo reqVo) {
        //新增评论
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, "Course information not obtained");
        Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
        Assert.notNull(teacher, "Teacher information not obtained");
        TeacherComplaintRecord teacherEvaluationRecord = StudentClassConverter.INSTANCE.toCreateTeacherComplaintRecord(userCode, teacher.getPrice(), reqVo.getRemark(), studentClass);
        teacherComplaintService.insert(teacherEvaluationRecord);

        studentClass.setIsComplaint(true);
        studentClassService.updateEntity(studentClass);
    }


    public String meetingJoinUrl(String classId) throws IOException {
        StudentClass studentClass = studentClassService.selectByRecordId(classId);
        Assert.notNull(studentClass, "Course information not obtained");
        String meetingRecordId = studentClass.getMeetingRecordId();
        //如果没有会议信息则新建一个
        if (StringUtils.isEmpty(meetingRecordId)) {
            Date meetingDate = DateUtil.parse(DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd") + " " + studentClass.getBeginTime(), "yyyy-MM-dd HH:mm");
            //创建会议
            Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
            String meeting = zoomOAuthService.createMeeting(teacher, studentClass.getRecordId(), DateUtil.format(meetingDate, "yyyy-MM-dd HH:mm"), CourseTypeEnum.valueOf(studentClass.getCourseType()));
            JSONObject meetObj = new JSONObject(meeting);
            StudentClassMeeting meetingEntity = studentClassMeetingService.insertMeeting(studentClass.getCreator(), studentClass.getCreateName(), meetObj);
            studentClass.setMeetingRecordId(meetingEntity.getMeetUuid());
            studentClassService.updateEntity(studentClass);
            meetingRecordId = meetingEntity.getMeetUuid();
        }
        String dateStr = studentClass.getCourseTime() + " " + studentClass.getBeginTime();
        Date beginDate = DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm");
        Date date1 = new Date();

        long diffInMillis = Math.abs(beginDate.getTime() - date1.getTime());
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5分钟的毫秒数
        boolean isLessThan5Minutes = diffInMillis < fiveMinutesInMillis;
        //todo 先去掉校验，上线后开启
        //Assert.isTrue(isLessThan5Minutes, "You can only enter the meeting five minutes in advance");
        StudentClassMeeting studentClassMeeting = studentClassMeetingService.selectByMeetingId(meetingRecordId);

        Assert.notNull(studentClassMeeting, "Meeting information not obtained");
        Assert.isTrue(StringUtils.isNotEmpty(studentClassMeeting.getMeetJoinUrl()), "Meeting information not obtained");
        return studentClassMeeting.getMeetJoinUrl();
    }
}
