package org.example.meetlearning.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.StudentClassConverter;
import org.example.meetlearning.converter.StudentClassRegularConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.*;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.AvailableTimeCalculatorUtil;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.classes.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.evaluation.TeacherComplaintReqVo;
import org.example.meetlearning.vo.evaluation.TeacherEvaluationReqVo;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
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

    private final TextbookService textbookService;

    private final UserFinanceService userFinanceService;

    private final UserFinanceRecordService userFinanceRecordService;

    private final UserService userService;

    private final TeacherEvaluationService teacherEvaluationService;

    private final TeacherComplaintService teacherComplaintService;

    private final ZoomOAuthService zoomOAuthService;

    private final StudentClassMeetingService studentClassMeetingService;

    private final TeacherCourseTimeService teacherCourseTimeService;

    private final StudentClassRegularService studentClassRegularService;

    private final BaseConfigService baseConfigService;

    private final TeacherSalaryPcService teacherSalaryPcService;

    private final MeetingLogService meetingLogService;

    public RespVo<PageVo<StudentClassListRespVo>> studentClassPage(String userCode, StudentClassQueryVo queryVo) {
        User user = userService.selectByRecordId(userCode);
        Assert.notNull(user, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Map<String, Object> params = queryVo.getParams();
        if (StringUtils.equals(RoleEnum.TEACHER.name(), user.getType())) {
            params.put("teacherId", userCode);
        } else if (StringUtils.equals(RoleEnum.STUDENT.name(), user.getType())) {
            params.put("studentId", userCode);
        } else if (StringUtils.equals(RoleEnum.AFFILIATE.name(), user.getType())) {
            params.put("affiliateIds", List.of(userCode));
        }
        Page<StudentClass> page = studentClassService.selectPageByParams(params, queryVo.getPageRequest());
        List<String> userIds = page.getRecords().stream().map(StudentClass::getStudentId).distinct().toList();
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
        Map<String, Student> studentMap = new HashMap<>();
        List<String> studentIds = page.getRecords().stream().map(StudentClass::getStudentId).distinct().toList();
        if (!CollectionUtils.isEmpty(studentIds)) {
            List<Student> students = studentService.findByRecordIds(studentIds);
            studentMap = students.stream().collect(Collectors.toMap(Student::getRecordId, Function.identity()));
        }
        Map<String, Teacher> teacherMap = new HashMap<>();
        List<String> teacherIds = page.getRecords().stream().map(StudentClass::getTeacherId).distinct().toList();
        if (!CollectionUtils.isEmpty(teacherIds)) {
            List<Teacher> teachers = teacherService.selectByRecordIds(teacherIds);
            teacherMap = teachers.stream().collect(Collectors.toMap(Teacher::getRecordId, Function.identity()));
        }
        // 语言MAP
        Map<String, String> countryMap = new HashMap<>();
        List<SelectValueVo> selectValueVos = baseConfigService.selectByType(ConfigTypeEnum.LANGUAGE.name());
        if (!CollectionUtils.isEmpty(selectValueVos)) {
            countryMap = selectValueVos.stream().collect(Collectors.toMap(SelectValueVo::getValue, SelectValueVo::getLabel));
        }
        baseConfigService.selectByType(ConfigTypeEnum.COUNTRY.name()).stream().collect(Collectors.toMap(SelectValueVo::getValue, SelectValueVo::getLabel));
        Map<String, String> finalCountryMap = countryMap;
        Map<String, Student> finalStudentMap = studentMap;
        Map<String, Teacher> finalTeacherMap = teacherMap;
        PageVo<StudentClassListRespVo> pageVo = PageVo.map(page, list -> {
            StudentClassListRespVo respVo = StudentClassConverter.INSTANCE.toStudentClassListRespVo(list);
            if (StringUtils.isNotEmpty(list.getTeacherCountry())) {
                respVo.setTeacherLanguage(finalCountryMap.get(list.getTeacherCountry()));
            }
            if (userFinanceMap.containsKey(list.getStudentId())) {
                UserFinance userFinance = userFinanceMap.get(list.getStudentId());
                respVo.setStudentConsumption(BigDecimalUtil.nullOrZero(userFinance.getConsumptionQty()));
                respVo.setStudentBalance(BigDecimalUtil.nullOrZero(userFinance.getBalanceQty()));
            }
            if (userFinanceRecordHashMap.containsKey(list.getStudentId())) {
                UserFinanceRecord userFinanceRecord = userFinanceRecordHashMap.get(list.getStudentId());
                respVo.setEfficientDate(userFinanceRecord.getExpirationTime());
            }
            if (finalStudentMap.containsKey(list.getStudentId())) {
                Student student = finalStudentMap.get(list.getStudentId());
                respVo.setStudentLanguage(student.getLanguage());
                respVo.setStudentEmail(student.getEmail());
            }
            if (finalTeacherMap.containsKey(list.getTeacherId())) {
                Teacher teacher = finalTeacherMap.get(list.getTeacherId());
                respVo.setTeacherLanguage(teacher.getLanguage());
            }
            respVo.setIsCanJoin(true);
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
    @Transactional(rollbackFor = Exception.class)
    public RespVo<String> studentClassAdd(String userCode, String userName, StudentClassAddReqVo reqVo) throws IOException, ParseException {
        //校验拉取老师学生信息组装数据
        //查询学生信息
        Student student = reqVo.getStudentId() != null ? studentService.findByRecordId(reqVo.getStudentId()) : null;
        Assert.notNull(student, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        //查询老师信息
        Teacher teacher = reqVo.getTeacherId() != null ? teacherService.selectByRecordId(reqVo.getTeacherId()) : null;
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        //查询代理商信息
        Affiliate affiliate = null;
        if (student != null && StringUtils.isNotEmpty(student.getAffiliateId())) {
            affiliate = affiliateService.findByRecordId(student.getAffiliateId());
        }
        Assert.isTrue(reqVo.getCourseType() != null, "Course type" + getHint(LanguageContextEnum.OBJ_NOTNULL));
        //新增课时币学生扣减记录
        operaTokenLogs(userCode, userName, student.getRecordId(), teacher.getCoin().negate(), TokenContentEnum.COURSE_CLASS.getEnContent(), null, null, null);
        UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
        StudentClass studentClass = StudentClassConverter.INSTANCE.toCreate(userCode, userName, reqVo, student, teacher, affiliate, userFinance);
        //教材补充
        if (StringUtils.isNotEmpty(reqVo.getTextbookId())) {
            Textbook textbook = textbookService.selectByRecordId(reqVo.getTextbookId());
            studentClass.setTextbook(textbook.getName());
        }
        Date meetingDate = DateUtil.parse(DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd") + " " + studentClass.getBeginTime(), "yyyy-MM-dd HH:mm");
        //创建会议
        String meeting = zoomOAuthService.createMeeting(teacher, studentClass.getRecordId(), DateUtil.format(meetingDate, "yyyy-MM-dd HH:mm"), CourseTypeEnum.valueOf(studentClass.getCourseType()));
        JSONObject meetObj = new JSONObject(meeting);
        StudentClassMeeting meetingEntity = studentClassMeetingService.insertMeeting(userCode, userName, meetObj);

        studentClass.setMeetingRecordId(meetingEntity.getMeetId());
        studentClassService.insertEntity(studentClass);
        //记录老师已有课时
        teacherCourseTimeService.studentClassTimeSet(getLanguage(), List.of(studentClass));
        teacherSalaryPcService.updateSalary(userCode, userName, studentClass.getTeacherId(), new Date());
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<List<SelectValueVo>> classCoinList(StudentCoinQueryVo queryVo) {
        List<BigDecimal> coinList = new ArrayList<>();
        if (StringUtils.equals(queryVo.getCourseType(), CourseTypeEnum.GROUP.name())) {
            coinList = teacherService.coinGroupList();
        } else {
            coinList = teacherService.coinList();
        }
        List<SelectValueVo> selectValueVos = coinList.stream().map(price -> new SelectValueVo(price.toString(), "Any teacher with " + price + " tokens")).toList();
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
            params.put("teacherIds", teacherIds);
            List<TeacherCourseTime> courseTimes = teacherCourseTimeService.selectByTeacherIdDateTime(null, DateUtil.parse(queryVo.getCourseDate(), "yyyy-MM-dd"), arr[0], arr[1]);
            List<String> noTeacherIds = courseTimes.stream().map(TeacherCourseTime::getTeacherId).distinct().toList();
            if (!CollectionUtils.isEmpty(noTeacherIds)) {
                params.put("noTeacherIds", noTeacherIds);
            }
        }
        List<Teacher> teachers = teacherService.selectListParams(params);
        List<SelectValueVo> selectValueVos = teachers.stream().map(teacher -> new SelectValueVo(teacher.getRecordId(), teacher.getName())).toList();
        return new RespVo<>(selectValueVos);
    }


    /**
     * 3种场景
     * 1.管理员预约，优先选择价格
     * 2.学生预约课程
     * 3.固定上课课程
     */
    public List<String> classTimeList(StudentClassCommonQueryVo queryVo) {
        //1.管理员接口
        if (queryVo.getPriceContent() != null) {
            List<Teacher> teachers = teacherService.selectListParams(queryVo.getParams());
            List<String> teacherIds = teachers.stream().map(Teacher::getRecordId).toList();
            if (CollectionUtils.isEmpty(teacherIds)) {
                return List.of();
            }
            List<TeacherSchedule> teacherSchedules = teacherScheduleService.selectGroupTimeByParams(queryVo.getScheduleParams());
            return teacherSchedules.stream().map(schedule -> schedule.getBeginTime() + "-" + schedule.getEndTime()).toList();
        } else if (BooleanUtil.isTrue(queryVo.getIsRegular())) {
            if (!StringUtils.equals(CourseTypeEnum.GROUP.name(), queryVo.getCourseType())) {
                return List.of("00:00-00:30", "00:30-01:00", "01:00-01:30", "01:30-02:00", "02:00-02:30", "02:30-03:00", "03:00-03:30", "03:30-04:00",
                        "04:00-04:30", "04:30-04:30", "04:30-05:00", "05:00-05:30", "05:30-06:00", "06:00-06:30", "06:30-07:00", "07:00-07:30", "07:30-08:00",
                        "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00", "10:00-10:30", "10:30-11:00", "11:00-11:30", "11:30-12:00", "12:00-12:30",
                        "12:30-13:00", "13:00-13:30", "13:30-14:00", "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00", "16:00-16:30", "16:30-17:00",
                        "17:00-17:30", "17:30-18:00", "18:00-18:30", "18:30-19:00", "19:00-19:30", "19:30-20:00", "20:00-20:30", "20:30-21:00", "21:00-21:30",
                        "21:30-22:00", "22:00-22:30", "22:30-23:00", "23:00-23:30", "23:30-00:00"
                );
            } else {
                return List.of("00:00-01:00", "01:00-02:00", "02:00-03:00", "03:00-04:00", "04:00-05:00", "05:00-06:00", "06:00-07:00", "07:00-08:00",
                        "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
                        "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00", "23:00-00:00"
                );
            }
        } else if (StringUtils.isNotEmpty(queryVo.getCourseDate())) {
            List<String> halfTimeList = getHalfTimeList(queryVo.getTeacherId(), queryVo.getCourseDate(), queryVo.getCourseType(), queryVo.getStartTime(), queryVo.getStopTime());
            halfTimeList = CollectionUtils.isEmpty(halfTimeList) ? new ArrayList<>() : halfTimeList;
            if (StringUtils.equals(DateUtil.format(new Date(), "yyyy-MM-dd"), queryVo.getCourseDate())) {
                //获取当前时间时分
                String hour = DateUtil.format(new Date(), "HH:mm");
                halfTimeList = halfTimeList.stream().filter(time -> {
                    String timeStr = StringUtils.split(time, "-")[0];
                    return timeStr.compareTo(hour) >= 0;
                }).toList();
            }
            return halfTimeList;
        }
        return List.of();
    }


    public List<String> getHalfTimeList(String teacherId, String courseDate, String courseType, String startTime, String stopTime) {
        //1.查询日程
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        params.put("weekNum", ScheduleWeekEnum.getByDate(courseDate));
        String courseTypeStr = StringUtils.equals(CourseTypeEnum.TEST.name(), courseType) ? CourseTypeEnum.SINGLE.name() : courseType;
        params.put("courseType", courseTypeStr);
        if (startTime != null) {
            params.put("startTime", startTime);
        }
        if (stopTime != null) {
            params.put("stopTime", stopTime);
        }
        List<TeacherSchedule> teacherSchedules = teacherScheduleService.selectGroupTimeByParams(params);
        List<String> teacherSchedulesList = teacherSchedules.stream().filter(f -> f.getCourseType().equals(courseTypeStr)).sorted(Comparator.comparing(TeacherSchedule::getBeginTime)).map(schedule -> schedule.getBeginTime() + "-" + schedule.getEndTime()).toList();
        //当天已预约时间段
        List<TeacherCourseTime> teacherCourseTimes = teacherCourseTimeService.selectByTeacherIdTime(teacherId, DateUtil.parse(courseDate, "yyyy-MM-dd"));
        List<String> teacherCourseTimesList = teacherCourseTimes.stream().sorted(Comparator.comparing(TeacherCourseTime::getBeginTime)).map(schedule -> schedule.getBeginTime() + "-" + schedule.getEndTime()).toList();
        if (!StringUtils.equals(CourseTypeEnum.GROUP.name(), courseType)) {
            return AvailableTimeCalculatorUtil.getAvailableTimeSlots(teacherSchedulesList, teacherCourseTimesList);
        } else {
            teacherCourseTimesList = teacherCourseTimes.stream().sorted(Comparator.comparing(TeacherCourseTime::getBeginTime)).map(schedule -> schedule.getBeginTime() + "-" + schedule.getEndTime()).collect(Collectors.toList());
            teacherCourseTimesList = CollectionUtils.isEmpty(teacherCourseTimesList) ? new ArrayList<>() : teacherCourseTimesList;
            List<String> resultList = teacherCourseTimes.stream().filter(f -> f.getCourseType().equals(courseTypeStr)).map(schedule -> schedule.getBeginTime() + "-" + schedule.getEndTime()).collect(Collectors.toList());
            List<String> list = AvailableTimeCalculatorUtil.getAvailableTimeSlots(teacherSchedulesList, teacherCourseTimesList);
            if (!CollectionUtils.isEmpty(list)) {
                resultList.addAll(list);
            }
            return resultList.stream().distinct().toList();
        }
    }


    public RespVo<StudentClassTotalRespVo> classTotalList(String userCode, StudentClassQueryVo queryVo) {
        Map<String, Object> params = queryVo.getParams();
        User user = userService.selectByRecordId(userCode);
        if (StringUtils.equals(RoleEnum.TEACHER.name(), user.getType())) {
            params.put("teacherId", userCode);
        }
        Long cancelTotal = studentClassService.selectCancelByParams(params);
        Long completeTotal = studentClassService.selectCompleteByParams(params);
        return new RespVo<>(new StudentClassTotalRespVo(new BigDecimal(cancelTotal), new BigDecimal(completeTotal)));
    }

    /**
     * 老师或者管理员取消预约课程提醒
     * 学生变更时间提醒
     */
    public Boolean studentClassCancelOrUpdateVerify(String userCode, RecordIdQueryVo reqVo) {
        User user = userService.selectByRecordId(userCode);
        Assert.notNull(user, "User information not obtained userCode:" + userCode);
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
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
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        //todo 提前三天才能取消，三天内取消罚50%
        StudentClass newStudentClass = new StudentClass();
        newStudentClass.setId(studentClass.getId());
        newStudentClass.setCancelId(userCode);
        newStudentClass.setCancelTime(new Date());
        newStudentClass.setClassStatus(CourseStatusEnum.CANCEL.getStatus());
        studentClassService.updateEntity(newStudentClass);
    }

    public void studentClassUpdateTime(String userCode, String userName, StudentClassUpdateTimeReqVo reqVo) {
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        String beginTime = studentClass.getBeginTime();
        String endTime = studentClass.getEndTime();
        Date courseTime = studentClass.getCourseTime();
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Date classDate = DateUtil.parse(DateUtil.format(reqVo.getCourseDate(), "yyyy-MM-dd") + " " + studentClass.getBeginTime());
        long diffInMillie = classDate.getTime() - new Date().getTime();
        Assert.isTrue(diffInMillie >= 0, getHint(LanguageContextEnum.NOT_CHANGE));
        long dayNum = TimeUnit.HOURS.convert(diffInMillie, TimeUnit.MILLISECONDS);
        Assert.isTrue(BooleanUtil.isTrue(dayNum >= 3), getHint(LanguageContextEnum.NOT_CHANGE_TIME));
        //变更新时间
        studentClass.setCourseTime(reqVo.getCourseDate());
        studentClass.setBeginTime(reqVo.getBeginTime());
        studentClass.setEndTime(reqVo.getEndTime());
        studentClass.setUpdateTime(new Date());
        studentClass.setUpdator(userCode);
        studentClass.setUpdateName(userName);
        studentClassService.updateEntity(studentClass);
        //删掉原有的占用时间
        teacherCourseTimeService.deleteByTeacherCourseDateType(studentClass.getTeacherId(), studentClass.getCourseType(), courseTime, beginTime, endTime);
        //记录老师已有课时
        teacherCourseTimeService.studentClassTimeSet(getLanguage(), List.of(studentClass));
        teacherSalaryPcService.updateSalary(userCode, userName, studentClass.getTeacherId(), new Date());
    }


    public void studentClassEvaluate(String userCode, String userName, TeacherEvaluationReqVo reqVo) {
        //新增评论
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        TeacherEvaluationRecord teacherEvaluationRecord = StudentClassConverter.INSTANCE.toCreateTeacherEvaluationRecord(userCode, reqVo.getRating(), reqVo.getRemark(), studentClass);
        teacherEvaluationService.insert(teacherEvaluationRecord);

        //更新老师星级
        BigDecimal rating = teacherEvaluationService.selectRatingByTeacherId(studentClass.getTeacherId());
        Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
        Assert.notNull(teacher, getHint(LanguageContextEnum.TEACHER_NOTNULL));
        teacher.setRating(BigDecimalUtil.nullOrZero(rating));
        Teacher newTeacher = new Teacher();
        newTeacher.setId(teacher.getId());
        newTeacher.setRating(BigDecimalUtil.nullOrZero(rating));
        teacherService.updateEntity(newTeacher);
        studentClass.setIsEvaluation(true);
        if (BigDecimalUtil.equals(reqVo.getRating(), BigDecimal.ONE)) {
            //新增投诉
            TeacherComplaintRecord teacherComplaintRecord = StudentClassConverter.INSTANCE.toCreateTeacherComplaintRecord(userCode, teacher.getPrice(), reqVo.getRemark(), studentClass);
            teacherComplaintService.insert(teacherComplaintRecord);
            studentClass.setIsComplaint(true);
            studentClass.setClassStatus(CourseStatusEnum.WAIT_ONT_STAR.getStatus());
            // 更新薪资表
            studentClassService.updateEntity(studentClass);
            teacherSalaryPcService.updateSalary(userCode, userName, teacher.getRecordId(), DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        } else {
            studentClassService.updateEntity(studentClass);
        }
    }

    public void studentClassComplaint(String userCode, TeacherComplaintReqVo reqVo) {
        //新增评论
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
        Assert.notNull(teacher, "Teacher information not obtained");
        TeacherComplaintRecord teacherEvaluationRecord = StudentClassConverter.INSTANCE.toCreateTeacherComplaintRecord(userCode, teacher.getPrice(), reqVo.getRemark(), studentClass);
        teacherComplaintService.insert(teacherEvaluationRecord);

        studentClass.setIsComplaint(true);
        studentClassService.updateEntity(studentClass);
    }

    public void studentClassCancelComplaint(RecordIdQueryVo reqVo) {
        //新增评论
        StudentClass studentClass = studentClassService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
        Assert.notNull(teacher, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        teacherComplaintService.deletedEntity(studentClass.getRecordId());
    }


    public String meetingJoinUrl(String classId) throws IOException, ParseException {
        StudentClass studentClass = studentClassService.selectByRecordId(classId);
        Assert.notNull(studentClass, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        String meetingRecordId = studentClass.getMeetingRecordId();
        //如果没有会议信息则新建一个
        if (StringUtils.isEmpty(meetingRecordId)) {
            Date meetingDate = DateUtil.parse(DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd") + " " + studentClass.getBeginTime(), "yyyy-MM-dd HH:mm");
            //创建会议
            Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
            //获取不到会议信息就重新创建
            String meeting = zoomOAuthService.createMeeting(teacher, studentClass.getRecordId(), DateUtil.format(meetingDate, "yyyy-MM-dd HH:mm"), CourseTypeEnum.valueOf(studentClass.getCourseType()));
            JSONObject meetObj = new JSONObject(meeting);
            StudentClassMeeting meetingEntity = studentClassMeetingService.insertMeeting(studentClass.getCreator(), studentClass.getCreateName(), meetObj);
            studentClass.setMeetingRecordId(meetingEntity.getMeetId());
            studentClassService.updateEntity(studentClass);
            meetingRecordId = meetingEntity.getMeetId();
        }
        // 1. 解析会议开始时间
        String dateStr = studentClass.getCourseTime() + " " + studentClass.getBeginTime();
        Date beginDate = DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm");
        // 2. 获取当前时间（精确到分钟）
        Date currentDate = DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
        // 3. 计算时间差（会议开始时间 - 当前时间）
        long diffInMillis = beginDate.getTime() - currentDate.getTime();
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5分钟的毫秒数
        // 4. 核心校验逻辑：允许进入会议的条件
        boolean canEnterMeeting = diffInMillis <= fiveMinutesInMillis;
        StudentClassMeeting studentClassMeeting = studentClassMeetingService.selectByMeetingId(meetingRecordId);
        Assert.notNull(studentClassMeeting, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Assert.isTrue(StringUtils.isNotEmpty(studentClassMeeting.getMeetJoinUrl()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        //todo 为了测试
        if (!StringUtils.equals(studentClass.getStudentEmail(), "student@talk.com")) {
            Assert.isTrue(canEnterMeeting, getHint(LanguageContextEnum.MEETING_FIVE) + "joinLink-->:" + studentClassMeeting.getMeetJoinUrl());
        }
        return studentClassMeeting.getMeetJoinUrl();
    }

    /**
     * 1.生成固定上课记录
     * 2.
     *
     * @param userName
     * @param reqVo
     */
    public void studentClassRegular(String userCode, String userName, StudentClassRegularReqVo reqVo) {
        Assert.isTrue(!CollectionUtils.isEmpty(reqVo.getCourseDates()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Assert.isTrue(StringUtils.isNotEmpty(reqVo.getStudentId()), getHint(LanguageContextEnum.STUDENT_NOTNULL));
        Student student = studentService.findByRecordId(reqVo.getStudentId());
        Assert.notNull(student, getHint(LanguageContextEnum.STUDENT_NOTNULL));
        Assert.isTrue(StringUtils.isNotEmpty(reqVo.getTeacherId()), getHint(LanguageContextEnum.TEACHER_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(reqVo.getTeacherId());
        Assert.notNull(teacher, getHint(LanguageContextEnum.TEACHER_NOTNULL));
        //生成固定上课请求
        StudentClassRegular studentClassRegular = StudentClassRegularConverter.INSTANCE.toCreate(userCode, userName, reqVo, student, teacher);
        studentClassRegularService.insert(studentClassRegular);
        //生成固定上课请求时间记录
        List<StudentClassRegularRecord> regularRecords = new ArrayList<>();
        for (String courseTime : reqVo.getCourseTimes()) {
            studentClassRegular.setBeginTime(courseTime.split("-")[0]);
            studentClassRegular.setEndTime(courseTime.split("-")[1]);
            List<StudentClassRegularRecord> records = reqVo.getCourseDates().stream().map(courseDate -> StudentClassRegularConverter.INSTANCE.toCreateRecord(studentClassRegular, courseDate)).toList();
            regularRecords.addAll(records);
        }
        BigDecimal sumQty = studentClassRegular.getPrice();
        UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
        BigDecimal canQty = BigDecimalUtil.nullOrZero(userFinance.getBalanceQty());
        Assert.isTrue(BigDecimalUtil.gteThan(canQty, sumQty), getHint(LanguageContextEnum.INSUFFICIENT_BALANCE));
        regularRecords.forEach(studentClassRegularService::insertRecord);
        //生成固定请求排班
        teacherCourseTimeService.studentClassTimeSet(getLanguage(), regularRecords.stream().map(item -> StudentClassRegularConverter.INSTANCE.toStudentClass(studentClassRegular, item)).toList());
    }


    public List<StudentClassMeetLogRespVo> selectMeetLog(RecordIdQueryVo queryVo) {
        String recordId = queryVo.getRecordId();
        StudentClass studentClass = studentClassService.selectByRecordId(recordId);
        Assert.notNull(studentClass, "Course information not obtained");
        if (StringUtils.isEmpty(studentClass.getMeetingRecordId())) {
            return new ArrayList<>();
        }
        List<MeetingLog> meetingLogs = meetingLogService.selectByMeetingId(studentClass.getMeetingRecordId());
        return meetingLogs.stream().map(meetingLog -> new StudentClassMeetLogRespVo(meetingLog.getCreateTime(), meetingLog.getRemark())).toList();
    }
}
