package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.StudentClassRegularRecord;

import java.util.List;

public interface StudentClassRegularRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StudentClassRegularRecord record);

    List<StudentClassRegularRecord> selectByRegularId(List<String> regularIds);

    int updateEntity(StudentClassRegularRecord record);

}