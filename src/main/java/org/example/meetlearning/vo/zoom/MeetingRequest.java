package org.example.meetlearning.vo.zoom;

import lombok.Data;

import java.util.List;

@Data
public class MeetingRequest {
    private String topic;
    private Integer type; // 1=即时会议，2=预定会议
    private String start_time; // yyyy-MM-ddTHH:mm:ss
    private Integer duration;
    private String timezone;
    private List<String> participants; // 参会者邮箱列表
}