package org.example.meetlearning.service;


import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.ScheduleConverter;
import org.example.meetlearning.dao.entity.TeacherSchedule;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;
import org.example.meetlearning.service.impl.TeacherScheduleService;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleAddOrUpdateReqVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleInfoRespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherSchedulePcService {

    private final TeacherScheduleService teacherScheduleService;


    public RespVo<String> scheduleAdd(String userCode, ScheduleAddOrUpdateReqVo reqVo) {
        try {
            List<TeacherScheduleSet> teacherSchedules = reqVo.getDateRespVos().stream().map(item ->
                    ScheduleConverter.INSTANCE.toCreate(userCode, reqVo.getTeacherId(), reqVo.getWeekNum().name(), reqVo.getScheduleType().name(), item)
            ).toList();
            teacherScheduleService.insertSetBatch(teacherSchedules);
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
            Assert.notNull(reqVo.getWeekNum(), "weekNum cannot be empty");
            String weekNum = reqVo.getWeekNum().name();
            Assert.notNull(reqVo.getScheduleType(), "scheduleType cannot be empty");
            String scheduleType = reqVo.getScheduleType().name();
            List<TeacherScheduleSet> teacherSchedules = teacherScheduleService.selectSetByTeacherId(teacherId, weekNum, scheduleType);
            ScheduleInfoRespVo scheduleInfoRespVo = ScheduleConverter.INSTANCE.toScheduleInfoRespVo(teacherSchedules);
            return new RespVo<>(scheduleInfoRespVo);
        } catch (Exception e) {
            log.error("Query failed", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }


}
