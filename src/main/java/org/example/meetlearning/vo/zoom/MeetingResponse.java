package org.example.meetlearning.vo.zoom;

import lombok.Data;

import java.util.List;

@Data
public class MeetingResponse {
    private String id;
    private String join_url;
    private String start_url;
    private List<Registrant> registrants;
}
