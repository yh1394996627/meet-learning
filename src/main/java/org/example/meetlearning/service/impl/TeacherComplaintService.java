package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherComplaintRecord;
import org.example.meetlearning.dao.mapper.TeacherComplaintRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherComplaintService {

    private final TeacherComplaintRecordMapper teacherComplaintRecordMapper;

    public List<TeacherComplaintRecord> selectByTeacherId(String teacherId) {
        return teacherComplaintRecordMapper.selectByTeacherId(teacherId);
    }

    public int updateByEntity(TeacherComplaintRecord record) {
        return teacherComplaintRecordMapper.updateByEntity(record);
    }

    public int insert(TeacherComplaintRecord record) {
        return teacherComplaintRecordMapper.insert(record);
    }

}
