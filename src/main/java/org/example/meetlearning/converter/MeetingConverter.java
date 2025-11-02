package org.example.meetlearning.converter;

import java.time.ZonedDateTime;
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
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(meetingObj.get("start_time").toString());
            Date date2 = Date.from(zonedDateTime.toInstant());
            studentClassMeeting.setMeetStartTime(date2);
        }
        studentClassMeeting.setMeetType(Integer.valueOf(meetingObj.get("type").toString()));
        studentClassMeeting.setMeetMainType(1);
        return studentClassMeeting;
    }


    default StudentClassMeeting toVoovCreateMeeting(String userCode, String userName, JSONObject meetingObj) {
        StudentClassMeeting studentClassMeeting = new StudentClassMeeting();
        studentClassMeeting.setDeleted(false);
        studentClassMeeting.setCreator(userCode);
        studentClassMeeting.setCreateName(userName);
        studentClassMeeting.setCreateTime(new Date());
        studentClassMeeting.setMeetEmail(meetingObj.get("hostEmail").toString());
        studentClassMeeting.setMeetUuid(meetingObj.get("meetingId").toString());
        studentClassMeeting.setMeetId(meetingObj.get("meetingId").toString());
        studentClassMeeting.setMeetJoinUrl(meetingObj.get("joinUrl").toString());
        studentClassMeeting.setCreateMeetZoomUserId(meetingObj.get("hostId").toString());
        if (meetingObj.get("startTime") != null) {
            studentClassMeeting.setMeetStartTime(DateUtil.parseDateTime(meetingObj.get("startTime").toString()));
        }
        studentClassMeeting.setMeetType(Integer.valueOf(meetingObj.get("type").toString()));
        studentClassMeeting.setMeetMainType(2);
        return studentClassMeeting;
    }


}
