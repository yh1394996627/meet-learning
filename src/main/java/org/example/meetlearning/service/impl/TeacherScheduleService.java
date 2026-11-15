package org.example.meetlearning.service.impl;


import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherSchedule;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;
import org.example.meetlearning.dao.mapper.TeacherScheduleMapper;
import org.example.meetlearning.dao.mapper.TeacherScheduleSetMapper;
import org.example.meetlearning.enums.ScheduleTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TeacherScheduleService {

    private final TeacherScheduleMapper teacherScheduleMapper;
    private final TeacherScheduleSetMapper teacherScheduleSetMapper;


    public int deleteByTeacherId(String teacherId, String weekNum) {
        return teacherScheduleMapper.deleteByTeacherIdAndWeekNum(teacherId, weekNum);
    }

    public int insertBatch(List<TeacherSchedule> record) {
        return teacherScheduleMapper.insertBatch(record);
    }

    public List<TeacherSchedule> selectByTeacherId(String teacherId, String weekNum) {
        return teacherScheduleMapper.selectByTeacherIdAndWeekNum(teacherId, weekNum);
    }

    public List<String> selectTeacherIdByWeekNumAndTime(String weekNum, String beginTime, String endTime) {
        return teacherScheduleMapper.selectTeacherIdByWeekNumAndTime(weekNum, beginTime, endTime);
    }

    public List<TeacherSchedule> selectByTeacherIdWeekNumGroupByTime(String weekNum, List<String> teacherIds) {
        return teacherScheduleMapper.selectByTeacherIdWeekNumGroupByTime(weekNum, teacherIds);
    }

    public List<TeacherSchedule> selectGroupTimeByParams(Map<String, Object> params) {
        return teacherScheduleMapper.selectGroupTimeByParams(params);
    }

    public int deleteSetByTeacherId(String teacherId, String weekNum, String scheduleType) {
        return teacherScheduleSetMapper.deleteByTeacherIdAndWeekNum(teacherId, weekNum, scheduleType);
    }

    public void insertSetBatch(List<TeacherScheduleSet> records) {
        teacherScheduleSetMapper.insertBatch(records);
    }

    public List<TeacherScheduleSet> selectSetByTeacherId(String teacherId, String weekNum, String scheduleType) {
        return teacherScheduleSetMapper.selectByTeacherIdAndWeekNum(teacherId, weekNum, scheduleType);
    }

    /**
     * 校验老师指定日期是否为假期（OFF类型，offDate为具体日期）
     */
    public boolean isTeacherOff(String teacherId, Date courseDate) {
        if (courseDate == null) {
            return false;
        }
        List<TeacherScheduleSet> offSets = teacherScheduleSetMapper.selectByTeacherIdAndWeekNum(teacherId, null, ScheduleTypeEnum.OFF.name());
        String date = DateUtil.formatDate(courseDate);
        return offSets.stream().anyMatch(item -> item.getOffDate() != null && DateUtil.formatDate(item.getOffDate()).equals(date));
    }


}
