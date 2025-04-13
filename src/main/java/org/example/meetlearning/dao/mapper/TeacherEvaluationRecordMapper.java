package org.example.meetlearning.dao.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TeacherComplaintRecord;
import org.example.meetlearning.dao.entity.TeacherEvaluationRecord;

public interface TeacherEvaluationRecordMapper {

    int insert(TeacherEvaluationRecord record);

    List<TeacherEvaluationRecord> selectByTeacherId(String teacherId);

    List<TeacherEvaluationRecord> selectByTeacherIdLimit20(String teacherId);


    BigDecimal selectRatingByTeacherId(String teacherId);

    int updateEntity(TeacherEvaluationRecord record);

}