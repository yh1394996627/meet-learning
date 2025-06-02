package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TeacherSalary;

public interface TeacherSalaryMapper {

    int insert(TeacherSalary record);

    List<TeacherSalary> selectByTeacherId(String teacherId);

    int updateEntity(TeacherSalary record);


    List<TeacherSalary> selectByUnVerTeacherIds(List<String> teacherIds);

    TeacherSalary selectByUnVerTeacherId(String teacherId);
}