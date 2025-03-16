package org.example.meetlearning.service.impl;


import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherSchedule;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;
import org.example.meetlearning.dao.mapper.TeacherScheduleMapper;
import org.example.meetlearning.dao.mapper.TeacherScheduleSetMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public int deleteSetByTeacherId(String teacherId, String weekNum, String scheduleType) {
        return teacherScheduleSetMapper.deleteByTeacherIdAndWeekNum(teacherId, weekNum, scheduleType);
    }

    public int insertSetBatch(List<TeacherScheduleSet> records) {
        return teacherScheduleSetMapper.insertBatch(records);
    }

    public List<TeacherScheduleSet> selectSetByTeacherId(String teacherId, String weekNum, String scheduleType) {
        return teacherScheduleSetMapper.selectByTeacherIdAndWeekNum(teacherId, weekNum, scheduleType);
    }

}
