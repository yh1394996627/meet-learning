package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.StudentClassRegular;

import java.util.List;

public interface StudentClassRegularMapper {

    int deleteByRecordId(String recordId);

    int insert(StudentClassRegular record);

    StudentClassRegular selectByRecordId(String recordId);

    List<StudentClassRegular> selectByTeacherId(String teacherId);

    int updateEntity(StudentClassRegular record);

}