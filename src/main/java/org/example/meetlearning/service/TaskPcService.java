package org.example.meetlearning.service;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.UserFinance;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.service.impl.StudentClassService;
import org.example.meetlearning.service.impl.UserFinanceRecordService;
import org.example.meetlearning.service.impl.UserFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class TaskPcService {

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private UserFinanceRecordService userFinanceRecordService;

    @Autowired
    private UserFinanceService userFinanceService;

    @Autowired
    private TeacherSalaryPcService teacherSalaryPcService;

    public void runTaskAtMidnight() {
        List<UserFinanceRecord> records = userFinanceRecordService.selectByLtDate(new Date());
        if (!CollectionUtils.isEmpty(records)) {
            log.info("课时币有效期刷新定时任务执行");
            for (UserFinanceRecord record : records) {
                record.setDeleted(true);
                userFinanceRecordService.updateByEntity(record);
            }
            List<String> userIds = records.stream().map(UserFinanceRecord::getUserId).distinct().toList();
            Map<String, Object> params = new HashMap<>();
            params.put("userIds", userIds);
            List<UserFinanceRecord> userFinanceRecords = userFinanceRecordService.selectDaByParams(params);
            for (UserFinanceRecord userFinanceRecord : userFinanceRecords) {
                userFinanceRecord.setExpirationTime(userFinanceRecord.getExpirationTime() != null
                        ? userFinanceRecord.getExpirationTime()
                        : DateUtil.parseDate("2099-01-01"));
            }
            userFinanceRecords = userFinanceRecords.stream().sorted(Comparator.comparing(UserFinanceRecord::getExpirationTime).reversed()).toList();
            Map<String, List<UserFinanceRecord>> userFinanceRecordMap = userFinanceRecords.stream().collect(Collectors.groupingBy(UserFinanceRecord::getUserId));
            List<UserFinance> userFinance = userFinanceService.selectByUserIds(userIds);
            for (UserFinance finance : userFinance) {
                List<UserFinanceRecord> list = userFinanceRecordMap.containsKey(finance.getUserId())
                        ? userFinanceRecordMap.get(finance.getUserId())
                        : new ArrayList<>();
                BigDecimal balanceQty = list.stream().map(UserFinanceRecord::getBalanceQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                finance.setBalanceQty(balanceQty);
                finance.setUpdateTime(new Date());
                finance.setExpirationTime(CollectionUtils.isEmpty(list) ? null : list.get(0).getExpirationTime());
                userFinanceService.updateByEntity(finance);
            }
            log.info("课时币状态处理完成");
        }
        log.info("预约课程老师旷课处理执行");
        //将课程结束时间小于当前时间但是课程状态还是未开始的课程按旷课处理
        List<StudentClass> studentClasses = studentClassService.selectAbsentByDate(DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd")));
        log.info("缺席的课程数量 count:{}", studentClasses.size());
        for (StudentClass studentClass : studentClasses) {
            studentClass.setTeacherCourseStatus(CourseStatusEnum.ABSENT.getStatus());
            studentClass.setClassStatus(CourseStatusEnum.ABSENT.getStatus());
            studentClassService.updateEntity(studentClass);
        }
        Map<String, List<StudentClass>> map = studentClasses.stream().collect(Collectors.groupingBy(StudentClass::getTeacherId));

        map.forEach((teacherId, list) -> {
            StudentClass class1 = list.get(0);
            teacherSalaryPcService.updateSalary("SYSTEM", "SYSTEM", class1.getTeacherId(), DateUtil.parseDate(DateUtil.formatDate(new Date())));
        });

        log.info("预约课程老师旷课处理执行结束");
    }
}
