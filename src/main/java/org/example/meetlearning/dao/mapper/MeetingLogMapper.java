package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.MeetingLog;

import java.util.List;

public interface MeetingLogMapper {

    int insert(MeetingLog record);

    List<MeetingLog> selectByMeetingId(String meetingId);

}