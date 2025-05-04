package org.example.meetlearning.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.TeacherCourseTime;
import org.example.meetlearning.dao.mapper.TeacherCourseTimeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
            insert(teacherCourseTime);
        }
    }

}
