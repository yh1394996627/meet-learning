package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TeacherComplaintRecord;

public interface TeacherComplaintRecordMapper {

    int insert(TeacherComplaintRecord record);

    List<TeacherComplaintRecord> selectByTeacherId(String teacherId);

    int updateByEntity(TeacherComplaintRecord record);

    int deletedEntity(String recordId);

}