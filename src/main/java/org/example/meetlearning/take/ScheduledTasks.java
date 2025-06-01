package org.example.meetlearning.take;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.StudentClassMeeting;
import org.example.meetlearning.dao.entity.UserFinance;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.impl.StudentClassMeetingService;
import org.example.meetlearning.service.impl.StudentClassService;
import org.example.meetlearning.service.impl.UserFinanceRecordService;
import org.example.meetlearning.service.impl.UserFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
@Transactional
@Slf4j
public class ScheduledTasks {

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    @Qualifier("emailExecutor")
    private Executor emailExecutor;

    @Autowired
    private EmailPcService emailPcService;

    @Autowired
    private StudentClassMeetingService studentClassMeetingService;

    @Autowired
    private UserFinanceRecordService userFinanceRecordService;

    @Autowired
    private UserFinanceService userFinanceService;

    //@Scheduled(cron = "0 * * * * ?")  // 每分钟执行一次
    @Scheduled(cron = "0 25,55 * * * *") // 秒 分 时 日 月 周
    public void executeForLoopWithThreadPool() {
        System.out.println("定时任务开始 | 线程: " + Thread.currentThread().getName());
        // 获取目标时间（原有逻辑）
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        LocalDateTime targetTime = minute == 25
                ? now.truncatedTo(ChronoUnit.HOURS).plusMinutes(30)
                : now.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        //查询需要通知的课程
        Date courseDate = DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm-ss");
        String formattedTime = targetTime.format(formatter);
        List<StudentClass> classes = studentClassService.selectClassByTimeBt(courseDate, formattedTime);
        log.info("课程提前5分钟闪上课提醒 size:{}", classes.size());
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }
        //查询会议地址
        List<String> meetIds = classes.stream().map(StudentClass::getMeetingRecordId).distinct().toList();
        Map<String, String> meetingMap = new HashMap<>();
        List<StudentClassMeeting> studentClassMeetings = studentClassMeetingService.selectByMeetingIds(meetIds);
        if (!CollectionUtils.isEmpty(studentClassMeetings)) {
            meetingMap = studentClassMeetings.stream().collect(Collectors.toMap(StudentClassMeeting::getMeetId, StudentClassMeeting::getMeetJoinUrl));
        }
        // 使用线程池异步发送邮件
        Map<String, String> finalMeetingMap = meetingMap;
        classes.forEach(course ->
                emailExecutor.execute(() -> emailPcService.sendNotice(DateUtil.format(course.getCourseTime(), "yyyy-MM-dd"), course.getTeacherName(), finalMeetingMap.get(course.getMeetingRecordId()), course.getStudentEmail()))
        );
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void runTaskAtMidnight() {
        log.info("课时币有效期刷新定时任务执行");
        List<UserFinanceRecord> records = userFinanceRecordService.selectByLtDate(new Date());
        for (UserFinanceRecord record : records) {
            record.setDeleted(true);
            userFinanceRecordService.updateByEntity(record);
        }
        List<String> userIds = records.stream().map(UserFinanceRecord::getUserId).distinct().toList();
        Map<String,Object> params = new HashMap<>();
        params.put("userIds",userIds);
        List<UserFinanceRecord> userFinanceRecords = userFinanceRecordService.selectDaByParams(params);
        userFinanceRecords = userFinanceRecords.stream().sorted(Comparator.comparing(UserFinanceRecord::getExpirationTime).reversed()).toList();
        Map<String,List<UserFinanceRecord>> userFinanceRecordMap = userFinanceRecords.stream().collect(Collectors.groupingBy(UserFinanceRecord::getUserId));
        List<UserFinance> userFinance = userFinanceService.selectByUserIds(userIds);
        for (UserFinance finance : userFinance) {
            List<UserFinanceRecord> list = userFinanceRecordMap.get(finance.getUserId());
            BigDecimal balanceQty =  list.stream().map(UserFinanceRecord::getBalanceQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            finance.setBalanceQty(balanceQty);
            finance.setUpdateTime(new Date());
            finance.setExpirationTime(list.get(0).getExpirationTime());
            userFinanceService.updateByEntity(finance);
        }
        log.info("课时币状态处理完成");
        log.info("预约课程老师旷课处理执行");
        //将课程结束时间小于当前时间但是课程状态还是未开始的课程按旷课处理
        List<StudentClass> studentClasses = studentClassService.selectAbsentByDate(DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd")));
        log.info("缺席的课程数量 count:{}",  studentClasses.size());
        for (StudentClass studentClass : studentClasses) {
            studentClass.setTeacherCourseStatus(CourseStatusEnum.ABSENT.getStatus());
            studentClass.setClassStatus(CourseStatusEnum.ABSENT.getStatus());
            studentClassService.updateEntity(studentClass);
        }
        //更新老师薪资表

        log.info("预约课程老师旷课处理执行结束");
    }
}
