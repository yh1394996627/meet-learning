package org.example.meetlearning.service.impl;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.BooleanUtil;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.common.ZoomUseRedisSetCommon;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.StudentClassMeeting;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.enums.ClassStatusEnum;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.vo.zoom.Registrant;
import org.example.meetlearning.vo.zoom.ZoomBaseVerifyRespVo;
import org.example.meetlearning.vo.zoom.ZoomWebhookPayload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ZoomOAuthService {

    @Value("${zoom.api-url}")
    private String zoomApiUrl;

    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ZoomUseRedisSetCommon zoomUseRedisSetCommon;

    @Autowired
    private ZoomBaseService zoomBaseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private StudentClassMeetingService studentClassMeetingService;


    private OkHttpClient client = new OkHttpClient();


    private final Gson gson = new Gson();

    private static final okhttp3.MediaType JSON = okhttp3.MediaType.get("application/json; charset=utf-8");

    /**
     * 獲取生效的token
     */
    public String getValidAccessToken(String clientId, String clientSecret, String accountId) {
        Object redis = redisTemplate.opsForValue().get(accountId);
        if (redis == null) {
            refreshToken(clientId, clientSecret, accountId);
        } else {
            accessToken = redis.toString();
        }
        return accessToken;
    }

    /**
     * 刷新token
     */
    private synchronized void refreshToken(String clientId, String clientSecret, String accountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "account_credentials");
            body.add("account_id", accountId);

            ResponseEntity<Map> response = new RestTemplate().exchange(
                    "https://zoom.us/oauth/token",
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class);
            zoomUseRedisSetCommon.dailyIncrement(clientId);
            this.accessToken = (String) Objects.requireNonNull(response.getBody()).get("access_token");
            redisTemplate.opsForValue().set(accountId, accessToken, 3590, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh Zoom access token", e);
        }
    }

    /**
     * 获取ZOOM状态
     */
    public ZoomBaseVerifyRespVo isTokenValid(String accountId, String accessToken) {
        String url = "https://api.zoom.us/v2/users/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );
            dailyIncrement(accountId);
            return new ZoomBaseVerifyRespVo(response.getStatusCode() == HttpStatus.OK, response.getStatusCode().value(), "Verification passed", null);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) { // 401 表示无效
                new ZoomBaseVerifyRespVo(false, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
            }
            new ZoomBaseVerifyRespVo(false, e.getStatusCode().value(), e.getStatusCode().toString(), null);
            throw e;
        }
    }

    /**
     * 获取ZOOM用户ID
     */
    public String getZoomUserIdByEmail(String accountId, String email, String accessToken) {
        String url = "https://api.zoom.us/v2/users/" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        dailyIncrement(accountId);
        log.info("user：{}", response.getBody());
        JSONObject userObj = new JSONObject(response.getBody());
        return userObj.getString("id");
    }

    public String getAccountPlanType(String accountId, String accessToken) {
        String url = "https://api.zoom.us/v2/users/accounts/" + accountId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        );
        dailyIncrement(accountId);
        return Objects.requireNonNull(response.getBody()).toString();
    }

    /**
     * 创建会议
     */
    public String createMeeting(Teacher teacher, String topic, String startTime, CourseTypeEnum courseType) throws IOException {
        //获取ZOOM生效的账号
        Integer zoomType = courseType == CourseTypeEnum.GROUP ? 2 : 1;
        Assert.isTrue(StringUtils.isNotEmpty(teacher.getZoomUserId()) && StringUtils.isNotEmpty(teacher.getZoomAccountId()), "Zoom userId not found");
        ZoomAccountSet zoomAccountSet = zoomBaseService.selectByAccountId(teacher.getZoomAccountId());
        Assert.isTrue(!BooleanUtil.isTrue(zoomAccountSet.getIsException()), "Zoom account is exception");
        // 获取token
        getValidAccessToken(zoomAccountSet.getZoomClientId(), zoomAccountSet.getZoomClientSecret(), zoomAccountSet.getZoomAccountId());
        String url = zoomApiUrl + "/users/" + teacher.getZoomUserId() + "/meetings";
        String json = "{\"topic\":\"" + topic + "\",\"start_time\":\"" + startTime + "\"}";
        RequestBody body = RequestBody.create(json, okhttp3.MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    /**
     * 添加参会者自动审批
     */
    public void addAndApproveParticipants(String meetingId, List<String> emails, String accessToken) {
        // 1. 添加参会者
        List<Registrant> registrants = addRegistrants(meetingId, emails, accessToken);

        // 2. 自动审批（如果配置开启）
        approveRegistrants(meetingId, registrants, accessToken);
    }

    private List<Registrant> addRegistrants(String meetingId, List<String> emails, String accessToken) {
        String url = zoomApiUrl + "/meetings/" + meetingId + "/registrants";

        List<Map<String, String>> registrants = emails.stream()
                .map(email -> {
                    Map<String, String> r = new HashMap<>();
                    r.put("email", email);
                    r.put("firstName ", email.split("@")[0]);
                    r.put("lastName", email.split("@")[1]);
                    // 可以添加更多信息如姓名等
                    return r;
                })
                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("registrants", registrants);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class);

        // 返回添加的参会者信息
        return ((List<Map<String, Object>>) response.getBody().get("registrants")).stream()
                .map(r -> {
                    Registrant reg = new Registrant();
                    reg.setEmail((String) r.get("email"));
                    return reg;
                })
                .collect(Collectors.toList());
    }

    private void approveRegistrants(String meetingId, List<Registrant> registrants, String accessToken) {
        String url = zoomApiUrl + "/meetings/" + meetingId + "/registrants/status";

        Map<String, Object> body = new HashMap<>();
        body.put("action", "approve");
        body.put("registrants", registrants.stream()
                .map(r -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("email", r.getEmail());
                    return m;
                })
                .collect(Collectors.toList()));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                Void.class);
    }

    private void dailyIncrement(String accountId) {
        log.info("accountId: {}", accountId);
        ZoomAccountSet zoomAccountSet = zoomBaseService.selectByAccountId(accountId);
        Assert.isTrue(!BooleanUtils.isTrue(zoomAccountSet.getIsException()), "AccountId exception");
        zoomUseRedisSetCommon.dailyIncrement(accountId + ":" + zoomAccountSet.getZoomType());
    }

    // 1. 检查用户是否存在并获取激活链接
    public JsonObject checkUserAndGetActivationLink(String userEmail) throws IOException {
        // 首先检查用户是否已存在
        boolean userExists = checkUserExists(userEmail);
        if (!userExists) {
            // 用户不存在，创建用户并获取激活链接
            createUserAndGetActivationLink(userEmail);
        }
        JsonObject userObj = getUserInfo(userEmail);
        resendInvitationToPendingUser(userObj.get("id").toString().replace("\"", ""));
        return getUserInfo(userEmail);
    }


    public String resendInvitationToPendingUser(String userId) {
        try {
            String url = "https://api.zoom.us/v2/users/" + userId + "/status";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("action", "resendInvitation");

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PUT, entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return "User is already active or doesn't require activation";
            }
            return "Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    // 检查用户是否存在
    private boolean checkUserExists(String email) throws IOException {
        Request request = new Request.Builder()
                .url(zoomApiUrl + "/users/" + email)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    // 创建用
    private JsonObject createUserAndGetActivationLink(String email) throws IOException {
        JSONObject userInfo = new JSONObject();
        userInfo.put("email", email);
        userInfo.put("type", 1); // 2 表示普通用户
        userInfo.put("first_name", "New");
        userInfo.put("last_name", "User");

        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "create");
        requestBody.put("user_info", userInfo);

        RequestBody body = RequestBody.create(requestBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(zoomApiUrl + "/users")
                .header("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to create user: " + response.body().string());
            }
            return gson.fromJson(response.body().string(), JsonObject.class);
        }
    }

    /**
     * 发送新用户邀请邮件
     */
    public void sendUserInvitation(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = zoomApiUrl + "/users/" + userId + "/invite";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("邀请发送成功");
        } else {
            System.out.println("邀请发送失败: " + response.getBody());
        }
    }

    // 获取用户ID
    public String getUserIdByEmail(String email, String zoomApiToken) throws IOException {
        Request request = new Request.Builder()
                .url(zoomApiUrl + "/users/" + email)
                .header("Authorization", "Bearer " + zoomApiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return null;
            }
            JsonObject responseJson = gson.fromJson(response.body().string(), JsonObject.class);

            String id = responseJson.get("id").getAsString();
            return id;
        }
    }


    public JsonObject getConsistentUserId(String email) throws IOException {
        // 第一步：从列表API获取所有用户
        String endpoint = zoomApiUrl + "/users?status=all"; // 包含所有状态用户
        Request request = new Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        // 第二步：遍历匹配邮箱
        try (Response response = client.newCall(request).execute()) {
            JsonObject result = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonArray users = result.getAsJsonArray("users");

            for (JsonElement user : users) {
                JsonObject userObj = user.getAsJsonObject();
                if (email.equalsIgnoreCase(userObj.get("email").getAsString())) {
                    return userObj; // 返回列表API中的ID
                }
            }
        }
        return null;
    }


    public JsonObject getUserInfo(String email) throws IOException {
        Request request = new Request.Builder()
                .url(zoomApiUrl + "/users/" + email)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get user info: " + response.body().string());
            }
            return gson.fromJson(response.body().string(), JsonObject.class);
        }
    }


    /**
     * 开始会议
     */
    public void handleMeetingStarted(ZoomWebhookPayload payload) {
        // 处理会议开始逻辑
        log.info("会议已开始: {}", payload.getPayload().getObject().getUuid());
        // 查询会议的预约课程
        String meetingId = payload.getPayload().getObject().getUuid();
        StudentClass studentClass = studentClassService.selectByMeetUuId(meetingId);
        if (studentClass != null) {
            StudentClass newStudentClass = new StudentClass();
            newStudentClass.setId(studentClass.getId());
            newStudentClass.setClassStatus(ClassStatusEnum.PROCESS.getStatus());
            studentClassService.updateEntity(newStudentClass);
        } else {
            log.error("No scheduled courses found");
        }
    }

    /**
     * 结束会议
     */
    public void handleMeetingEnded(ZoomWebhookPayload payload) {
        // 处理会议结束逻辑
        log.info("会议已结束: {}", payload.getPayload().getObject().getUuid());
        // 查询会议的预约课程
        String meetingId = payload.getPayload().getObject().getUuid();
        StudentClass studentClass = studentClassService.selectByMeetUuId(meetingId);
        if (studentClass != null) {
            StudentClass newStudentClass = new StudentClass();
            newStudentClass.setId(studentClass.getId());
            newStudentClass.setClassStatus(ClassStatusEnum.FINISH.getStatus());
            studentClassService.updateEntity(newStudentClass);
            //获取会议详情



        } else {
            log.error("No scheduled courses found");
        }
    }


    /**
     * 处理成员加入会议事件
     */
    private void handleParticipantJoined(ZoomWebhookPayload payload) {
        ZoomWebhookPayload.Payload.Object.Participant participant = payload.getPayload().getObject().getParticipant();

    }


    /**
     * 会议视频获取
     */
    public ResponseEntity<String> getMeetingRecordings(String accountId, String meetingUuId) {
        StudentClassMeeting studentClassMeeting = studentClassMeetingService.selectByMeetingId(meetingUuId);
        Assert.notNull(studentClassMeeting, "Meeting information not obtained");
        ZoomAccountSet zoomAccountSet = zoomBaseService.selectByAccountId(accountId);
        Assert.notNull(zoomAccountSet, "Zoom account not found");
        getValidAccessToken(zoomAccountSet.getZoomClientId(), zoomAccountSet.getZoomClientSecret(), zoomAccountSet.getZoomAccountId());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://api.zoom.us/v2/meetings/" + studentClassMeeting.getMeetId() + "/recordings";
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    /**
     * 下载视频
     */
    public ResponseEntity<Resource> downloadRecording(String downloadUrl, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                entity,
                Resource.class
        );
    }
}