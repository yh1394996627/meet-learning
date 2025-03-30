package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.StudentClassMeeting;

public interface StudentClassMeetingMapper {

    int deleteByPrimaryKey(Long id);

    int insertEntity(StudentClassMeeting record);

    List<StudentClassMeeting> selectByRecordIds(List<String> recordIds);

    StudentClassMeeting selectByMeetingId(String meetingId);

    int updateEntity(StudentClassMeeting record);

}