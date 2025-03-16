package org.example.meetlearning.converter;

import cn.hutool.core.date.DateUtil;
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
        scheduleInfoRespVo.setWeekNum(ScheduleWeekEnum.valueOf(teacherScheduleSet.getWeekNum()));
        List<ScheduleDateVo> dateRespVos = sets.stream().map(this::toScheduleDateVo).toList();
        scheduleInfoRespVo.setDateRespVos(dateRespVos);
        return scheduleInfoRespVo;
    }

    default TeacherScheduleSet toCreate(String userCode, String teacherId, String weekNum, String scheduleType, ScheduleDateVo dateRespVo) {
        TeacherScheduleSet teacherScheduleSet = new TeacherScheduleSet();
        teacherScheduleSet.setCreator(userCode);
        teacherScheduleSet.setCreateTime(new Date());
        teacherScheduleSet.setTeacherId(teacherId);
        teacherScheduleSet.setBeginTime(DateUtil.parseTime(dateRespVo.getBeginTime()));
        teacherScheduleSet.setEndTime(DateUtil.parseTime(dateRespVo.getEndTime()));
        teacherScheduleSet.setWeekNum(weekNum);
        teacherScheduleSet.setScheduleType(scheduleType);
        return teacherScheduleSet;
    }


    default ScheduleDateVo toScheduleDateVo(TeacherScheduleSet scheduleSet) {
        ScheduleDateVo scheduleDateVo = new ScheduleDateVo();
        scheduleDateVo.setBeginTime(scheduleSet.getBeginTime().toString());
        scheduleDateVo.setEndTime(scheduleSet.getEndTime().toString());
        return scheduleDateVo;
    }


}
