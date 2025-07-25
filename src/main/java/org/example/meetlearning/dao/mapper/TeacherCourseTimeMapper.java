package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.TeacherCourseTime;

import java.util.Date;
import java.util.List;

public interface TeacherCourseTimeMapper {

    int deleteByRegularId(String teacherId, String regularId);

    int deleteByCourseDateType(String teacherId, String courseType, Date courseDate, String beginTime, String endTime);

    int insert(TeacherCourseTime record);

    List<TeacherCourseTime> selectByTeacherIdTime(String teacherId, Date courseTime);

    List<TeacherCourseTime> selectByTeacherIdDateTime(String teacherId, String courseTime, String beginTime, String endTime);

}