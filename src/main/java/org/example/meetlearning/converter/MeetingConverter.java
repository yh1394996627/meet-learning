package org.example.meetlearning.converter;

import java.util.Date;

import cn.hutool.core.date.DateUtil;
import org.json.JSONObject;
import org.example.meetlearning.dao.entity.StudentClassMeeting;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeetingConverter {

    MeetingConverter INSTANCE = Mappers.getMapper(MeetingConverter.class);

    default StudentClassMeeting toCreateMeeting(String userCode, String userName, JSONObject meetingObj) {
        StudentClassMeeting studentClassMeeting = new StudentClassMeeting();
        studentClassMeeting.setDeleted(false);
        studentClassMeeting.setCreator(userCode);
        studentClassMeeting.setCreateName(userName);
        studentClassMeeting.setCreateTime(new Date());
        studentClassMeeting.setMeetEmail(meetingObj.get("host_email").toString());
        studentClassMeeting.setMeetUuid(meetingObj.get("uuid").toString());
        studentClassMeeting.setMeetId(meetingObj.get("id").toString());
        studentClassMeeting.setMeetJoinUrl(meetingObj.get("join_url").toString());
        studentClassMeeting.setCreateMeetZoomUserId(meetingObj.get("host_id").toString());
        studentClassMeeting.setMeetStatus(meetingObj.get("status").toString());
        if (meetingObj.get("start_time") != null) {
            studentClassMeeting.setMeetStartTime(DateUtil.parse(meetingObj.get("start_time").toString(), "yyyy-MM-dd HH:mm:ss"));
        }
        studentClassMeeting.setMeetType(Integer.valueOf(meetingObj.get("type").toString()));
        return studentClassMeeting;
    }


}
