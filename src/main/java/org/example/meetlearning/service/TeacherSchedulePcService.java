package org.example.meetlearning.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.ScheduleConverter;
import org.example.meetlearning.dao.entity.TeacherSchedule;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.example.meetlearning.service.impl.TeacherScheduleService;
import org.example.meetlearning.util.TimeSplitterUtil;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleAddOrUpdateReqVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleInfoRespVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherSchedulePcService {

    private final TeacherScheduleService teacherScheduleService;


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
}
