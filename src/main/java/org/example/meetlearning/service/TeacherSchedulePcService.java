package org.example.meetlearning.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.ScheduleConverter;
import org.example.meetlearning.converter.StudentClassConverter;
import org.example.meetlearning.converter.StudentClassRegularConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.example.meetlearning.enums.TokenContentEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.TimeSplitterUtil;
import org.example.meetlearning.vo.classes.StudentClassRegularRespVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleAddOrUpdateReqVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleInfoRespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleOperaVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleQueryVo;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TeacherSchedulePcService extends BasePcService {

    private final TeacherScheduleService teacherScheduleService;

    private final StudentClassRegularService studentClassRegularService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final AffiliateService affiliateService;

    private final UserFinanceService userFinanceService;

    private final ZoomOAuthService zoomOAuthService;

    private final StudentClassMeetingService studentClassMeetingService;

    private final StudentClassService studentClassService;

    private final TeacherCourseTimeService teacherCourseTimeService;


    public RespVo<String> scheduleAdd(String userCode, ScheduleAddOrUpdateReqVo reqVo) {
        try {
            if (reqVo.getScheduleType() != ScheduleTypeEnum.OFF) {
                //删除记录重新保存
                teacherScheduleService.deleteSetByTeacherId(reqVo.getTeacherId(), reqVo.getWeekNum().name(), reqVo.getScheduleType().name());
                List<TeacherScheduleSet> teacherSchedules = reqVo.getDateRespVos().stream().map(item ->
                        ScheduleConverter.INSTANCE.toCreate(userCode, reqVo.getTeacherId(), reqVo.getWeekNum().name(), reqVo.getScheduleType().name(), item)
                ).toList();
                if (!CollectionUtils.isEmpty(teacherSchedules)) {
                    teacherScheduleService.insertSetBatch(teacherSchedules);
                }
                updateSchedule(reqVo.getTeacherId(), reqVo.getWeekNum().name());
            } else {
                //删除记录重新保存
                teacherScheduleService.deleteSetByTeacherId(reqVo.getTeacherId(), null, reqVo.getScheduleType().name());
                List<TeacherScheduleSet> teacherOffSchedules = reqVo.getOffDates().stream().map(item ->
                        ScheduleConverter.INSTANCE.toCreateOff(userCode, reqVo.getTeacherId(), null, reqVo.getScheduleType().name(), item)
                ).toList();
                if (!CollectionUtils.isEmpty(teacherOffSchedules)) {
                    teacherScheduleService.insertSetBatch(teacherOffSchedules);
                }
            }
            return new RespVo<>("Successful");
        } catch (Exception e) {
            log.error("Add Or Update failed", e);
            return new RespVo<>("Add Or Update failed", false, e.getMessage());
        }
    }

    public RespVo<ScheduleInfoRespVo> scheduleInfo(ScheduleQueryVo reqVo) {
        try {
            String teacherId = reqVo.getTeacherId();
            Assert.isTrue(StringUtils.hasText(teacherId), "teacherId cannot be empty");
            String weekNum = reqVo.getWeekNum();
            Assert.notNull(reqVo.getScheduleType(), "scheduleType cannot be empty");
            String scheduleType = reqVo.getScheduleType().name();
            if (reqVo.getScheduleType() == ScheduleTypeEnum.OFF || !StringUtils.hasText(weekNum)) {
                weekNum = null;
            }
            List<TeacherScheduleSet> teacherSchedules = teacherScheduleService.selectSetByTeacherId(teacherId, weekNum, scheduleType);
            ScheduleInfoRespVo scheduleInfoRespVo = null;
            if (!CollectionUtils.isEmpty(teacherSchedules)) {
                scheduleInfoRespVo = ScheduleConverter.INSTANCE.toScheduleInfoRespVo(teacherSchedules);
            }
            return new RespVo<>(scheduleInfoRespVo);
        } catch (Exception e) {
            log.error("Query failed", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }


    /**
     * 更新日程有效时间  分小时和 1小时
     */
    private void updateSchedule(String teacherId, String weekNum) {
        // 删除原有日程
        teacherScheduleService.deleteByTeacherId(teacherId, weekNum);
        // 重新生成日程 存在特殊日程取特殊日程不存在则取普通日程
        List<TeacherScheduleSet> schedules = teacherScheduleService.selectSetByTeacherId(teacherId, weekNum, ScheduleTypeEnum.SPECIAL.name());
        List<TeacherSchedule> teacherSchedules = new ArrayList<>();
        if (CollectionUtils.isEmpty(schedules)) {
            schedules = teacherScheduleService.selectSetByTeacherId(teacherId, weekNum, ScheduleTypeEnum.REGULAR.name());
        }

        for (TeacherScheduleSet schedule : schedules) {
            //按照每半小时拆分
            List<String> timeList = TimeSplitterUtil.splitByHalfHour(schedule.getBeginTime(), schedule.getEndTime(), 30);
            for (String s : timeList) {
                String[] arr = StringUtils.split(s, "-");
                if (arr != null && arr.length == 2) {
                    TeacherSchedule teacherSchedule = ScheduleConverter.INSTANCE.toCreateSchedule(schedule);
                    teacherSchedule.setBeginTime(arr[0]);
                    teacherSchedule.setEndTime(arr[1]);
                    teacherSchedule.setCourseType(CourseTypeEnum.SINGLE.name());
                    teacherSchedules.add(teacherSchedule);
                }
            }
            //按照每1小时拆分
            List<String> timeOneList = TimeSplitterUtil.splitByHalfHour(schedule.getBeginTime(), schedule.getEndTime(), 60);
            for (String s : timeOneList) {
                String[] arr = StringUtils.split(s, "-");
                if (arr != null && arr.length == 2) {
                    TeacherSchedule teacherSchedule = ScheduleConverter.INSTANCE.toCreateSchedule(schedule);
                    teacherSchedule.setBeginTime(arr[0]);
                    teacherSchedule.setEndTime(arr[1]);
                    teacherSchedule.setCourseType(CourseTypeEnum.SINGLE.name());
                    teacherSchedules.add(teacherSchedule);
                }
            }

        }

        if (!CollectionUtils.isEmpty(teacherSchedules)) {
            teacherScheduleService.insertBatch(teacherSchedules);
        }
    }


    public List<StudentClassRegularRespVo> studentScheduleRegular(String userCode) {
        List<StudentClassRegular> regulars = studentClassRegularService.selectByTeacherId(userCode);
        if (CollectionUtils.isEmpty(regulars)) {
            return new ArrayList<>();
        }
        List<String> teacherIds = regulars.stream().map(StudentClassRegular::getTeacherId).distinct().toList();
        List<StudentClassRegularRecord> records = studentClassRegularService.selectRecordByRegularId(teacherIds);
        Map<String, List<StudentClassRegularRecord>> recordMap = records.stream().collect(Collectors.groupingBy(StudentClassRegularRecord::getRegularId));
        return regulars.stream().map(item -> {
            List<StudentClassRegularRecord> list = recordMap.get(item.getRecordId());
            List<Date> courseDates = list.stream().map(StudentClassRegularRecord::getCourseTime).distinct().toList();
            return StudentClassRegularConverter.INSTANCE.toRespVo(item, courseDates);
        }).toList();
    }


    public void studentScheduleRegular(String userCode, String userName, ScheduleOperaVo scheduleOperaVo) throws IOException {
        Assert.isTrue(StringUtils.hasText(scheduleOperaVo.getRecordId()), "recordId cannot be empty");
        StudentClassRegular studentClassRegular = studentClassRegularService.selectByRecordId(scheduleOperaVo.getRecordId());
        Assert.notNull(studentClassRegular, "regular cannot be empty");
        studentClassRegular.setAuditStatus(BooleanUtil.isTrue(scheduleOperaVo.getStatus()) ? 0 : 1);
        studentClassRegularService.updateEntity(studentClassRegular);
        //查询固定请求记录
        List<StudentClassRegularRecord> records = studentClassRegularService.selectRecordByRegularId(List.of(studentClassRegular.getRecordId()));


        if (BooleanUtil.isTrue(scheduleOperaVo.getStatus())) {
            //批量预约课程
            Student student = studentClassRegular.getStudentId() != null ? studentService.findByRecordId(studentClassRegular.getStudentId()) : null;
            Assert.notNull(student, "Student information not obtained");
            //查询老师信息
            Teacher teacher = studentClassRegular.getTeacherId() != null ? teacherService.selectByRecordId(studentClassRegular.getTeacherId()) : null;
            Assert.notNull(teacher, "Teacher information not obtained");
            //查询代理商信息
            Affiliate affiliate = null;
            if (student != null && org.codehaus.plexus.util.StringUtils.isNotEmpty(student.getAffiliateId())) {
                affiliate = affiliateService.findByRecordId(student.getAffiliateId());
            }
            for (StudentClassRegularRecord record : records) {
                Assert.isTrue(studentClassRegular.getCourseType() != null, "Course type cannot be empty");
                //3.新增课时币学生扣减记录
                operaTokenLogs(userCode, userName, student.getRecordId(), teacher.getPrice().negate(), TokenContentEnum.COURSE_CLASS.getEnContent());
                UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
                StudentClass studentClass = StudentClassConverter.INSTANCE.toCreateByRegular(userCode, userName, studentClassRegular, record, student, teacher, affiliate, userFinance);
                Date meetingDate = DateUtil.parse(DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd") + " " + studentClass.getBeginTime(), "yyyy-MM-dd HH:mm");
                //创建会议
                String meeting = zoomOAuthService.createMeeting(teacher, studentClass.getRecordId(), DateUtil.format(meetingDate, "yyyy-MM-dd HH:mm"), CourseTypeEnum.valueOf(studentClass.getCourseType()));
                JSONObject meetObj = new JSONObject(meeting);
                StudentClassMeeting meetingEntity = studentClassMeetingService.insertMeeting(userCode, userName, meetObj);

                studentClass.setMeetingRecordId(meetingEntity.getMeetId());
                studentClassService.insertEntity(studentClass);
                //记录老师已有课时
                teacherCourseTimeService.studentClassTimeSet(List.of(studentClass));
            }
        } else {
            for (StudentClassRegularRecord record : records) {
                //删除占用的时间段
                teacherCourseTimeService.deleteByTeacherIdTime(studentClassRegular.getTeacherId(), record.getCourseTime(), studentClassRegular.getBeginTime(), studentClassRegular.getEndTime());
            }
        }
    }
}
