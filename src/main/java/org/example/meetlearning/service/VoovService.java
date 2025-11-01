package org.example.meetlearning.service;

import com.tencentcloudapi.wemeet.Client;
import com.tencentcloudapi.wemeet.core.authenticator.AuthenticatorBuilder;
import com.tencentcloudapi.wemeet.core.authenticator.JWTAuthenticator;
import com.tencentcloudapi.wemeet.core.exception.ClientException;
import com.tencentcloudapi.wemeet.core.exception.ServiceException;
import com.tencentcloudapi.wemeet.service.user_manager.api.UserManagerApi;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.common.VoovMeetingConfig;
import org.example.meetlearning.dao.dto.VoovUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class VoovService {

    @Autowired
    private VoovMeetingConfig config;

    @Autowired
    private RestTemplate restTemplate;

    public void inviteUserToGroup(String groupId, VoovUserDto user) {
        // 1.构造 client 客户端(jwt 鉴权需要配置 appId sdkId secretID 和 secretKey)
        Client client = new Client.Builder()
                .withAppId(config.getAppId()).withSdkId(config.getEntId())
                .withSecret(config.getSecretId(),config.getSecretKey())
                .build();


        String userid= "";
        // 2.构造请求参数
        UserManagerApi.ApiV1UsersUseridInviteActivatePutRequest request =
                new UserManagerApi.ApiV1UsersUseridInviteActivatePutRequest.Builder(userid)
                        .operatorId("admin1761468493")
                        .operatorIdType("")
                        .build();

        // 3.构造 JWT 鉴权器
        // 随机数
        BigInteger nonce = BigInteger.valueOf(Math.abs((new SecureRandom()).nextInt()));
        // 当前时间戳
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        AuthenticatorBuilder<JWTAuthenticator> authenticatorBuilder =
                new JWTAuthenticator.Builder().nonce(nonce).timestamp(timestamp);

        // 4.发送对应的请求
        try {
            UserManagerApi.ApiV1UsersUseridInviteActivatePutResponse response =
                    client.user_manager().v1UsersUseridInviteActivatePut(request, authenticatorBuilder);
            // response from `v1UsersUseridInviteActivatePut`: Object
            System.out.printf("Response from `UserManagerApi.v1UsersUseridInviteActivatePut`: \nheader: %s\n%s\n",
                    response.getHeader(), response.getData());
        } catch (ClientException e) {
            System.out.printf("Error when calling `UserManagerApi.v1UsersUseridInviteActivatePut`: %s\n", e);
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            System.out.printf("Error when calling `UserManagerApi.v1UsersUseridInviteActivatePut`: %s\n", e);
            System.out.printf("Full HTTP response: %s\n", new String(e.getApiResp().getRawBody()));
            throw new RuntimeException(e);
        }
    }

//    public VoovMeeting createMeeting(String hostUserId, VoovMeeting meeting) {
//        try {
//            String url = config.getBaseUrl() + "/v1/meetings";
//
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("userid", hostUserId);
//            requestBody.put("instanceid", 1);
//            requestBody.put("subject", meeting.getTopic());
//            requestBody.put("type", meeting.getType());
//
//            if (meeting.getStartTime() != null) {
//                requestBody.put("start_time", meeting.getStartTime());
//            }
//            if (meeting.getEndTime() != null) {
//                requestBody.put("end_time", meeting.getEndTime());
//            }
//            if (meeting.getPassword() != null) {
//                requestBody.put("password", meeting.getPassword());
//            }
//
//            HttpHeaders headers = createHeaders();
//            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                Map<String, Object> responseBody = response.getBody();
//                if (responseBody != null && "0".equals(String.valueOf(responseBody.get("return_code")))) {
//                    Map<String, Object> meetingInfo = (Map<String, Object>) responseBody.get("meeting_info");
//                    meeting.setMeetingId(String.valueOf(meetingInfo.get("meeting_id")));
//                    meeting.setJoinUrl(String.valueOf(meetingInfo.get("join_url")));
//                    meeting.setStatus("created");
//                    log.info("成功创建会议: {}", meeting.getMeetingId());
//                    return meeting;
//                }
//            }
//
//            log.error("创建会议失败: {}", response.getBody());
//            return null;
//
//        } catch (Exception e) {
//            log.error("创建会议异常", e);
//            return null;
//        }
//    }

//    public boolean endMeeting(String meetingId, String hostUserId) {
//        try {
//            String url = config.getBaseUrl() + "/v1/meetings/" + meetingId + "/dismiss";
//
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("userid", hostUserId);
//            requestBody.put("instanceid", 1);
//
//            HttpHeaders headers = createHeaders();
//            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                Map<String, Object> responseBody = response.getBody();
//                if (responseBody != null && "0".equals(String.valueOf(responseBody.get("return_code")))) {
//                    log.info("成功结束会议: {}", meetingId);
//
//                    // 触发结束会议事件
//                    triggerMeetingEvent("end", meetingId, hostUserId, "host");
//                    return true;
//                }
//            }
//
//            log.error("结束会议失败: {}", response.getBody());
//            return false;
//
//        } catch (Exception e) {
//            log.error("结束会议异常", e);
//            return false;
//        }
//    }

//    public List<VoovRecording> getMeetingRecordings(String meetingId) {
//        try {
//            String url = config.getBaseUrl() + "/v1/meetings/" + meetingId + "/records";
//
//            HttpHeaders headers = createHeaders();
//            HttpEntity<Void> request = new HttpEntity<>(headers);
//
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                Map<String, Object> responseBody = response.getBody();
//                if (responseBody != null && "0".equals(String.valueOf(responseBody.get("return_code")))) {
//                    List<Map<String, Object>> records = (List<Map<String, Object>>) responseBody.get("record_files");
//                    List<VoovRecording> recordings = new ArrayList<>();
//
//                    for (Map<String, Object> record : records) {
//                        VoovRecording recording = new VoovRecording();
//                        recording.setRecordingId(String.valueOf(record.get("record_file_id")));
//                        recording.setMeetingId(meetingId);
//                        recording.setRecordingUrl(String.valueOf(record.get("download_address")));
//                        recording.setStartTime(Long.valueOf(String.valueOf(record.get("record_start_time"))));
//                        recording.setEndTime(Long.valueOf(String.valueOf(record.get("record_end_time"))));
//                        recording.setFileSize(Long.valueOf(String.valueOf(record.get("total_size"))));
//                        recording.setFileName(String.valueOf(record.get("filename")));
//                        recording.setStatus(String.valueOf(record.get("status")));
//                        recordings.add(recording);
//                    }
//
//                    log.info("成功获取会议 {} 的录像，共 {} 个", meetingId, recordings.size());
//                    return recordings;
//                }
//            }
//
//            log.error("获取会议录像失败: {}", response.getBody());
//            return Collections.emptyList();
//
//        } catch (Exception e) {
//            log.error("获取会议录像异常", e);
//            return Collections.emptyList();
//        }
//    }

//    public void addMeetingEventListener(String eventType, VoovMeetingEventListener listener) {
//        eventListeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
//        log.info("添加 {} 事件监听器", eventType);
//    }
//
//    public void removeMeetingEventListener(String eventType, VoovMeetingEventListener listener) {
//        List<VoovMeetingEventListener> listeners = eventListeners.get(eventType);
//        if (listeners != null) {
//            listeners.remove(listener);
//        }
//    }

//    public void triggerMeetingEvent(String eventType, String meetingId, String userId, String userName) {
//        VoovMeetingEvent event = new VoovMeetingEvent();
//        event.setEventType(eventType);
//        event.setMeetingId(meetingId);
//        event.setUserId(userId);
//        event.setUserName(userName);
//        event.setTimestamp(System.currentTimeMillis());
//
//        List<VoovMeetingEventListener> listeners = eventListeners.get(eventType);
//        if (listeners != null) {
//            for (VoovMeetingEventListener listener : listeners) {
//                try {
//                    listener.onMeetingEvent(event);
//                } catch (Exception e) {
//                    log.error("处理会议事件异常", e);
//                }
//            }
//        }
//
//        log.info("触发会议事件: {} - {}", eventType, meetingId);
//    }

    /**
     * 创建请求头
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("AppId", config.getAppId());
        headers.set("SecretId", config.getSecretId());

        // 生成签名
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String signature = generateSignature(timestamp, nonce);

        headers.set("Timestamp", timestamp);
        headers.set("Nonce", nonce);
        headers.set("Signature", signature);

        return headers;
    }

    /**
     * 生成签名
     */
    private String generateSignature(String timestamp, String nonce) {
        try {
            String stringToSign = config.getSecretId() + timestamp + nonce;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(config.getSecretKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            log.error("生成签名异常", e);
            return "";
        }
    }
}