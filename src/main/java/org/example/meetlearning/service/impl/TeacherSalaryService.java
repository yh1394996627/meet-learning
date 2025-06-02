package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TeacherSalaryConverter;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherSalary;
import org.example.meetlearning.dao.mapper.TeacherSalaryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherSalaryService {


    private final TeacherSalaryMapper teacherSalaryMapper;


    public List<TeacherSalary> selectByTeacherId(String recordId) {
        return teacherSalaryMapper.selectByTeacherId(recordId);
    }

    public int updateEntity(TeacherSalary record) {
        return teacherSalaryMapper.updateEntity(record);
    }

    public int insertEntity(TeacherSalary record) {
        return teacherSalaryMapper.insert(record);
    }

    public List<TeacherSalary> selectByUnVerTeacherIds(List<String> teacherIds) {
        return teacherSalaryMapper.selectByUnVerTeacherIds(teacherIds);
    }

    public TeacherSalary selectByUnVerTeacherId(String teacherId) {
        return teacherSalaryMapper.selectByUnVerTeacherId(teacherId);
    }

    public void updateByGltDateTeacherId(){
        teacherSalaryMapper.updateByGltDateTeacherId();
    }


}
