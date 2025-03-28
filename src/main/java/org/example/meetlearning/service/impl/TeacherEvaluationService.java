package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherEvaluationRecord;
import org.example.meetlearning.dao.mapper.TeacherEvaluationRecordMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class TeacherEvaluationService {

    private final TeacherEvaluationRecordMapper teacherEvaluationRecordMapper;

    public List<TeacherEvaluationRecord> selectByTeacherId(String teacherId) {
        return teacherEvaluationRecordMapper.selectByTeacherId(teacherId);
    }

    public List<TeacherEvaluationRecord> selectByTeacherIdLimit10(String teacherId) {
        return teacherEvaluationRecordMapper.selectByTeacherIdLimit10(teacherId);
    }

    public BigDecimal selectRatingByTeacherId(String teacherId) {
        return teacherEvaluationRecordMapper.selectRatingByTeacherId(teacherId);
    }

    public int updateByEntity(TeacherEvaluationRecord record) {
        return teacherEvaluationRecordMapper.updateEntity(record);
    }

    public int insert(TeacherEvaluationRecord record) {
        return teacherEvaluationRecordMapper.insert(record);
    }

}
