package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TeacherSchedule;

public interface TeacherScheduleMapper {

    int deleteByTeacherIdAndWeekNum(String teacherId, String weekNum);

    int insertBatch(List<TeacherSchedule> record);

    List<TeacherSchedule> selectByTeacherIdAndWeekNum(String teacherId, String weekNum);

}