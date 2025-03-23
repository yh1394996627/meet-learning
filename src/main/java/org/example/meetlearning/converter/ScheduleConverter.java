package org.example.meetlearning.converter;

import cn.hutool.core.date.DateUtil;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.TeacherSchedule;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.example.meetlearning.enums.ScheduleWeekEnum;
import org.example.meetlearning.vo.schedule.teacher.ScheduleDateVo;
import org.example.meetlearning.vo.schedule.teacher.ScheduleInfoRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

@Mapper
public interface ScheduleConverter {

    ScheduleConverter INSTANCE = Mappers.getMapper(ScheduleConverter.class);

    default ScheduleInfoRespVo toScheduleInfoRespVo(List<TeacherScheduleSet> sets) {
        ScheduleInfoRespVo scheduleInfoRespVo = new ScheduleInfoRespVo();
        TeacherScheduleSet teacherScheduleSet = sets.get(0);
        scheduleInfoRespVo.setTeacherId(teacherScheduleSet.getTeacherId());
        scheduleInfoRespVo.setScheduleType(ScheduleTypeEnum.valueOf(teacherScheduleSet.getScheduleType()));
        if (StringUtils.isNotEmpty(teacherScheduleSet.getWeekNum())) {
            scheduleInfoRespVo.setWeekNum(ScheduleWeekEnum.valueOf(teacherScheduleSet.getWeekNum()));
        }
        List<ScheduleDateVo> dateRespVos = sets.stream().map(this::toScheduleDateVo).toList();
        scheduleInfoRespVo.setDateRespVos(dateRespVos);
        return scheduleInfoRespVo;
    }

    default TeacherScheduleSet toCreate(String userCode, String teacherId, String weekNum, String scheduleType, ScheduleDateVo dateRespVo) {
        TeacherScheduleSet teacherScheduleSet = new TeacherScheduleSet();
        teacherScheduleSet.setCreator(userCode);
        teacherScheduleSet.setCreateTime(new Date());
        teacherScheduleSet.setTeacherId(teacherId);
        teacherScheduleSet.setBeginTime(dateRespVo.getBeginTime());
        teacherScheduleSet.setEndTime(dateRespVo.getEndTime());
        teacherScheduleSet.setWeekNum(weekNum);
        teacherScheduleSet.setScheduleType(scheduleType);
        return teacherScheduleSet;
    }

    default TeacherScheduleSet toCreateOff(String userCode, String teacherId, String weekNum, String scheduleType, Date offDate) {
        TeacherScheduleSet teacherScheduleSet = new TeacherScheduleSet();
        teacherScheduleSet.setCreator(userCode);
        teacherScheduleSet.setCreateTime(new Date());
        teacherScheduleSet.setTeacherId(teacherId);
        teacherScheduleSet.setBeginTime(null);
        teacherScheduleSet.setEndTime(null);
        teacherScheduleSet.setWeekNum(weekNum);
        teacherScheduleSet.setScheduleType(scheduleType);
        teacherScheduleSet.setOffDate(offDate);
        return teacherScheduleSet;
    }


    default ScheduleDateVo toScheduleDateVo(TeacherScheduleSet scheduleSet) {
        ScheduleDateVo scheduleDateVo = new ScheduleDateVo();
        scheduleDateVo.setBeginTime(scheduleSet.getBeginTime());
        scheduleDateVo.setEndTime(scheduleSet.getEndTime());
        return scheduleDateVo;
    }


    default TeacherSchedule toCreateSchedule(TeacherScheduleSet scheduleSet) {
        TeacherSchedule schedule = new TeacherSchedule();
        schedule.setCreator(scheduleSet.getCreator());
        schedule.setCreateTime(DateUtil.date());
        schedule.setTeacherId(scheduleSet.getTeacherId());
        schedule.setBeginTime(scheduleSet.getBeginTime());
        schedule.setEndTime(scheduleSet.getEndTime());
        schedule.setWeekNum(scheduleSet.getWeekNum());
        return schedule;
    }


}
