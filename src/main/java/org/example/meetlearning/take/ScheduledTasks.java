package org.example.meetlearning.take;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.converter.TokenConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Function;
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

    @Autowired
    private ZoomOAuthService zoomOAuthService;

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TokensLogService tokensLogService;


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
    //@Scheduled(cron = "0 * * * * ?")  // 每分钟
    public void runTaskAtMidnight() {
        log.info("课时币有效期刷新定时任务执行");
        Date yesterday = DateUtil.offsetDay(new Date(), -1);
        List<TokensLog> tokenLogs = new ArrayList<>();
        List<UserFinanceRecord> records = userFinanceRecordService.selectByLtDate(yesterday);
        for (UserFinanceRecord record : records) {
            record.setDeleted(true);
            userFinanceRecordService.updateByEntity(record);
            TokensLog tokensLog = TokenConverter.INSTANCE.toCreateTokenByFinanceRecord("SYSTEM", "SYSTEM", record.getUserId(), record.getUserName(), BigDecimal.ZERO, record.getUserEmail(), record.getCanQty().negate(), null, "Coin expiration handling");
            tokensLog.setUserId(record.getUserId());
            if (!StringUtils.isEmpty(record.getCurrencyCode())) {
                tokensLog.setCurrencyCode(record.getCurrencyCode());
                tokensLog.setCurrencyName(record.getCurrencyName());
            }
            tokenLogs.add(tokensLog);
        }
        List<String> userIds = records.stream().map(UserFinanceRecord::getUserId).distinct().toList();
        if (!CollectionUtils.isEmpty(userIds)) {
            Map<String, Object> params = new HashMap<>();
            params.put("userIds", userIds);
            List<UserFinanceRecord> userFinanceRecords = userFinanceRecordService.selectDaByParams(params);
            userFinanceRecords = userFinanceRecords.stream().sorted(Comparator.comparing(UserFinanceRecord::getExpirationTime).reversed()).toList();
            Map<String, List<UserFinanceRecord>> userFinanceRecordMap = userFinanceRecords.stream().collect(Collectors.groupingBy(UserFinanceRecord::getUserId));
            List<UserFinance> userFinance = userFinanceService.selectByUserIds(userIds);
            for (UserFinance finance : userFinance) {
                List<UserFinanceRecord> list = userFinanceRecordMap.get(finance.getUserId());
                BigDecimal balanceQty = CollectionUtils.isEmpty(list) ? BigDecimal.ZERO : list.stream().map(UserFinanceRecord::getCanQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                finance.setBalanceQty(balanceQty);
                finance.setUpdateTime(new Date());
                finance.setExpirationTime(CollectionUtils.isEmpty(list) ? null : list.get(0).getExpirationTime());
                userFinanceService.updateByEntity(finance);
                tokenLogs.stream().filter(tokenLog -> StringUtils.equals(tokenLog.getUserId(), finance.getUserId())).forEach(tokenLog -> {
                    tokenLog.setBalance(balanceQty);
                });
            }
            //存储课时币记录
            if (!CollectionUtils.isEmpty(tokenLogs)) {
                for (TokensLog tokenLog : tokenLogs) {
                    tokensLogService.insertEntity(tokenLog);
                }
            }
        }
        log.info("课时币状态处理完成");
        log.info("预约课程老师旷课处理执行");
        //将课程结束时间小于当前时间但是课程状态还是未开始的课程按旷课处理
        // 格式化为 yyyy-MM-dd 并解析为日期对象（时间部分为 00:00:00）
        Date yesterdayStart = DateUtil.parse(DateUtil.format(yesterday, "yyyy-MM-dd"));
        List<StudentClass> studentClasses = studentClassService.selectAbsentByDate(yesterdayStart);
        log.info("缺席的课程数量 count:{}", studentClasses.size());
        for (StudentClass studentClass : studentClasses) {
            studentClass.setTeacherCourseStatus(CourseStatusEnum.ABSENT.getStatus());
            studentClass.setClassStatus(CourseStatusEnum.ABSENT.getStatus());
            studentClassService.updateEntity(studentClass);
        }
        //更新老师薪资表
        log.info("预约课程老师旷课处理执行结束");
    }

    /**
     * 定时任务目的 整时整点结束课程 更具course_time + end_time
     *
     * @throws IOException
     */
    @Scheduled(cron = "0 0,30 * * * ?")
    public void closeMeetingTask() throws IOException {
        //获取到你跟前日期和结束时间节点
        Date courseTime = DateUtil.parseDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        String endTime = DateUtil.format(new Date(), "HH:mm");
        List<StudentClass> studentClasses = studentClassService.selectByCourseDate(courseTime, endTime);
        List<String> teacherIds = studentClasses.stream().map(StudentClass::getTeacherId).distinct().toList();
        List<Teacher> teachers = teacherService.selectByRecordIds(teacherIds);
        Map<String, Teacher> teacherMap = teachers.stream().collect(Collectors.toMap(Teacher::getRecordId, Function.identity()));
        for (StudentClass studentClass : studentClasses) {
            Teacher teacher = teacherMap.get(studentClass.getTeacherId());
            if (teacher != null && StringUtils.isNotEmpty(teacher.getZoomAccountId())) {
                zoomOAuthService.scheduleEndMeetingTask(studentClass.getMeetingRecordId(), teacher.getZoomAccountId());
            }
        }
    }


//    voov.meeting.app-id=304901095834
//    voov.meeting.secret-id=EOonFfGcgNFdaeBTZz4ngu6xggRrhkWT2
//    voov.meeting.secret-key=cvP3MlGcqfMQna0eRGd7j4vnNrW9KtpvTzuh93uzA2DarChC3
//    voov.meeting.base-url=https://api.meeting.qq.com
}
