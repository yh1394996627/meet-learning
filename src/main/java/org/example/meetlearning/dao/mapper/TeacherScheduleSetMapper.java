package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;

public interface TeacherScheduleSetMapper {

    int deleteByTeacherIdAndWeekNum(String teacherId, String weekNum, String scheduleType);

    int insertBatch(List<TeacherScheduleSet> record);

    List<TeacherScheduleSet> selectByTeacherIdAndWeekNum(String teacherId, String weekNum, String scheduleType);

}