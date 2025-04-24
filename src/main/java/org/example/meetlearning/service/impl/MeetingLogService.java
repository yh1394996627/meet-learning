package org.example.meetlearning.service.impl;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.MeetingLog;
import org.example.meetlearning.dao.mapper.MeetingLogMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetingLogService {

    private final MeetingLogMapper meetingLogMapper;


    /**
     * 插入会议日志
     */
    public int insert(String userCode, String userName, String meetingId, String remark) {
        MeetingLog meetingLog = new MeetingLog();
        meetingLog.setCreator(userCode);
        meetingLog.setCreateName(userName);
        meetingLog.setCreateTime(new Date());
        meetingLog.setMeetingId(meetingId);
        meetingLog.setRemark(remark);
        return meetingLogMapper.insert(meetingLog);
    }


    /**
     * 根据会议ID查询会议日志
     */
    public List<MeetingLog> selectByMeetingId(String meetingId) {
        return meetingLogMapper.selectByMeetingId(meetingId);
    }

}
