package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.TeacherDeduction;

import java.math.BigDecimal;

public interface TeacherDeductionMapper {

    int deleteByClassId(String classId);

    int insertEntity(TeacherDeduction record);

    TeacherDeduction selectByClassId(String classId);

    BigDecimal selectMonthDeductionByTeacher(String teacherId, String month);

    int updateEntity(TeacherDeduction record);

}