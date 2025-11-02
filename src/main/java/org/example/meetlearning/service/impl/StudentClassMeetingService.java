package org.example.meetlearning.service.impl;


import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.MeetingConverter;
import org.example.meetlearning.dao.entity.StudentClassMeeting;
import org.example.meetlearning.dao.mapper.StudentClassMeetingMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentClassMeetingService {

    private final StudentClassMeetingMapper studentClassMeetingMapper;

    /**
     * 新增会议信息
     */
    public StudentClassMeeting insertMeeting(String userCode, String userName, JSONObject meetingObj) {
        StudentClassMeeting meeting = MeetingConverter.INSTANCE.toCreateMeeting(userCode, userName, meetingObj);
        studentClassMeetingMapper.insertEntity(meeting);
        return meeting;
    }

    public StudentClassMeeting insertVoovMeeting(String userCode, String userName, JSONObject meetingObj) {
        StudentClassMeeting meeting = MeetingConverter.INSTANCE.toVoovCreateMeeting(userCode, userName, meetingObj);
        studentClassMeetingMapper.insertEntity(meeting);
        return meeting;
    }


    public StudentClassMeeting selectByMeetingId(String meetId) {
        return studentClassMeetingMapper.selectByMeetingId(meetId);
    }


    public List<StudentClassMeeting> selectByMeetingIds(List<String> meetIds) {
        return studentClassMeetingMapper.selectByRecordIds(meetIds);
    }


}
