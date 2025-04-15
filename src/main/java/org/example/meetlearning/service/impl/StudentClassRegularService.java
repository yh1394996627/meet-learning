package org.example.meetlearning.service.impl;


import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.StudentClassRegular;
import org.example.meetlearning.dao.entity.StudentClassRegularRecord;
import org.example.meetlearning.dao.mapper.StudentClassRegularMapper;
import org.example.meetlearning.dao.mapper.StudentClassRegularRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentClassRegularService {

    private final StudentClassRegularMapper studentClassRegularMapper;
    private final StudentClassRegularRecordMapper studentClassRegularRecordMapper;

    public void insert(StudentClassRegular regular) {
        studentClassRegularMapper.insert(regular);
    }

    public void updateEntity(StudentClassRegular regular) {
        studentClassRegularMapper.updateEntity(regular);
    }

    public StudentClassRegular selectByRecordId(String recordId) {
        return studentClassRegularMapper.selectByRecordId(recordId);
    }

    public void insertRecord(StudentClassRegularRecord regular) {
        studentClassRegularRecordMapper.insert(regular);
    }


    public List<StudentClassRegular> selectByTeacherId(String teacherId) {
        return studentClassRegularMapper.selectByTeacherId(teacherId);
    }

    public List<StudentClassRegularRecord> selectRecordByRegularId(List<String> regularIds) {
        return studentClassRegularRecordMapper.selectByRegularId(regularIds);
    }


}
