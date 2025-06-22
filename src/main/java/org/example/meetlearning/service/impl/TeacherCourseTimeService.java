package org.example.meetlearning.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.TeacherCourseTime;
import org.example.meetlearning.dao.mapper.TeacherCourseTimeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherCourseTimeService {

    private TeacherCourseTimeMapper teacherCourseTimeMapper;

    public void deleteByRegularId(String teacherId, String regularId) {
        teacherCourseTimeMapper.deleteByRegularId(teacherId, regularId);
    }

    public void deleteByTeacherCourseDateType(String teacherId, String courseType, Date courseTime, String beginTime, String endTime) {
        int value = teacherCourseTimeMapper.deleteByCourseDateType(teacherId, courseType, courseTime, beginTime, endTime);
        log.info("删除状态:{}",value);
    }

    public void insert(TeacherCourseTime record) {
        teacherCourseTimeMapper.insert(record);
    }

    public List<TeacherCourseTime> selectByTeacherIdTime(String teacherId, Date courseTime) {
        return teacherCourseTimeMapper.selectByTeacherIdTime(teacherId, courseTime);
    }

    public List<TeacherCourseTime> selectByTeacherIdDateTime(String teacherId, Date courseTime, String beginTime, String endTime) {
        return teacherCourseTimeMapper.selectByTeacherIdDateTime(teacherId, DateUtil.format(courseTime, "yyyy-MM-dd"), beginTime, endTime);
    }


    public void studentClassTimeSet(List<StudentClass> studentClasses) {
        for (StudentClass studentClass : studentClasses) {
            List<TeacherCourseTime> teacherCourseTimes = teacherCourseTimeMapper.selectByTeacherIdDateTime(studentClass.getTeacherId(), DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd"), studentClass.getBeginTime(), studentClass.getEndTime());
            Assert.isTrue(CollectionUtils.isEmpty(teacherCourseTimes), "Teacher【" + studentClass.getTeacherName() + "】time 【" + DateUtil.format(studentClass.getCourseTime(), "yyyy-MM-dd") + " " + studentClass.getBeginTime() + "-" + studentClass.getEndTime() + "】,  there is already an appointment available");
            TeacherCourseTime teacherCourseTime = new TeacherCourseTime();
            teacherCourseTime.setRecordId(UUID.randomUUID().toString());
            teacherCourseTime.setTeacherId(studentClass.getTeacherId());
            teacherCourseTime.setCourseTime(studentClass.getCourseTime());
            teacherCourseTime.setBeginTime(studentClass.getBeginTime());
            teacherCourseTime.setEndTime(studentClass.getEndTime());
            teacherCourseTime.setRegularId(studentClass.getRecordId());
            teacherCourseTime.setCourseType(studentClass.getCourseType());
            insert(teacherCourseTime);
        }
    }

}
