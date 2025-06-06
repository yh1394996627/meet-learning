package org.example.meetlearning.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TeacherSchedule;

public interface TeacherScheduleMapper {

    int deleteByTeacherIdAndWeekNum(String teacherId, String weekNum);

    int insertBatch(List<TeacherSchedule> record);

    List<TeacherSchedule> selectByTeacherIdAndWeekNum(String teacherId, String weekNum);

    List<String> selectTeacherIdByWeekNumAndTime(String weekNum, String beginTime, String endTime);

    List<TeacherSchedule> selectByTeacherIdWeekNumGroupByTime(String weekNum, List<String> teacherIds);

    List<TeacherSchedule> selectGroupTimeByParams(@Param("params") Map<String,Object> params);

}