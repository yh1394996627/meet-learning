package org.example.meetlearning.take;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.StudentClassMeeting;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.impl.StudentClassMeetingService;
import org.example.meetlearning.service.impl.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
