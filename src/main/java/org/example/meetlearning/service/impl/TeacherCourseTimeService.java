package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherCourseTime;
import org.example.meetlearning.dao.mapper.TeacherCourseTimeMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TeacherCourseTimeService {

    private TeacherCourseTimeMapper teacherCourseTimeMapper;

    public void deleteByTeacherIdTime(String teacherId, Date courseTime, String beginTime, String endTime) {
        teacherCourseTimeMapper.deleteByTeacherIdTime(teacherId, courseTime, beginTime, endTime);
    }

    public void insert(TeacherCourseTime record) {
        teacherCourseTimeMapper.insert(record);
    }

    public List<TeacherCourseTime> selectByTeacherIdTime(String teacherId, Date courseTime) {
        return teacherCourseTimeMapper.selectByTeacherIdTime(teacherId, courseTime);
    }










}
