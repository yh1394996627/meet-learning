package org.example.meetlearning.service;

import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.util.ZoomAuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ZoomPcService {

    @Value("${zoom.api-key}")
    private String API_KEY;

    @Value("${zoom.api-secret}")
    private String API_SECRET;

    @Value("${zoom.api-url}")
    private String ZOOM_API_URL;

    public void createMeeting(String zoomUserId, String topic, Date startTime, int duration) {
        String jwt = ZoomAuthUtil.generateJWT(API_KEY, API_SECRET);
        String meetingUrl = ZoomAuthUtil.createMeeting(zoomUserId, topic, startTime, duration, ZOOM_API_URL, jwt);
        log.info("meetingUrl:{}", meetingUrl);
    }


    public String getZoomUserIdByEmail(String email) {
        String jwt = ZoomAuthUtil.generateJWT(API_KEY, API_SECRET);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                ZOOM_API_URL + "/users?email=" + email, HttpMethod.GET, request, Map.class);

        List<Map<String, Object>> users = (List<Map<String, Object>>) response.getBody().get("users");
        if (users != null && !users.isEmpty()) {
            return (String) users.get(0).get("id");
        }
        throw new RuntimeException("User not found");
    }
}
