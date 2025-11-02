package org.example.meetlearning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tencentcloudapi.wemeet.Client;
import com.tencentcloudapi.wemeet.core.authenticator.AuthenticatorBuilder;
import com.tencentcloudapi.wemeet.core.authenticator.JWTAuthenticator;
import com.tencentcloudapi.wemeet.core.exception.ClientException;
import com.tencentcloudapi.wemeet.core.exception.ServiceException;
import com.tencentcloudapi.wemeet.service.meetings.api.MeetingsApi;
import com.tencentcloudapi.wemeet.service.meetings.model.V1MeetingsMeetingIdCancelPostRequest;
import com.tencentcloudapi.wemeet.service.meetings.model.V1MeetingsPost200Response;
import com.tencentcloudapi.wemeet.service.meetings.model.V1MeetingsPost200ResponseMeetingInfoListInner;
import com.tencentcloudapi.wemeet.service.meetings.model.V1MeetingsPostRequest;
import com.tencentcloudapi.wemeet.service.user_manager.api.UserManagerApi;
import com.tencentcloudapi.wemeet.service.user_manager.model.V1UsersListGet200Response;
import com.tencentcloudapi.wemeet.service.user_manager.model.V1UsersPostRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.common.TencentMeetingConfig;
import org.example.meetlearning.dao.dto.VoovUserDto;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class VoovService {

    @Autowired
    private TencentMeetingConfig config;

    @Autowired
    private Gson gson;

    static String BODY_JSON = "{\n"
            + "  \"operator_id\": \"admin1761468493\",\n"
            + "  \"user_account_type\": 3,\n"
            + "  \"userid\": \"0e4ffcd4-2302-4b1d-84bc-787f1063da43\",\n"
            + "  \"email\": \"yuh9527@aliyun.com\",\n"
            + "  \"operator_id_type\": 1,\n"
            + "  \"username\": \"yuhang1\",\n"
            + "  \"iphone\": \"\"\n"
            + "}";

    /**
     * 创建用户
     * }
     *
     * @param user
     */
    public void createMeetUser(VoovUserDto user) {
        // 1.构造 client 客户端(jwt 鉴权需要配置 appId sdkId secretID 和 secretKey)
        Client client = new Client.Builder().withAppId(config.getAppId()).withSdkId(config.getEntId()).withSecret(config.getSecretId(), config.getSecretKey()).build();
        String operatorId = config.getAdminUserId();
        String userEmail = user.getEmail();
        String userName = user.getUsername();
        log.info("BODY_JSON:{}", BODY_JSON);
        V1UsersPostRequest body = JSON.parseObject(BODY_JSON, V1UsersPostRequest.class);

        // 2.构造请求参数
        UserManagerApi.ApiV1UsersPostRequest request =
                new UserManagerApi.ApiV1UsersPostRequest.Builder()
                        .body(body).build();

        // 3.构造 JWT 鉴权器
        // 随机数
        BigInteger nonce = BigInteger.valueOf(Math.abs((new SecureRandom()).nextInt()));
        // 当前时间戳
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        AuthenticatorBuilder<JWTAuthenticator> authenticatorBuilder =
                new JWTAuthenticator.Builder().nonce(nonce).timestamp(timestamp);

        // 4.发送对应的请求
        try {
            UserManagerApi.ApiV1UsersPostResponse response =
                    client.user_manager().v1UsersPost(request, authenticatorBuilder);
            // response from `v1UsersPost`: V1UsersPost200Response
            System.out.printf("Response from `UserManagerApi.v1UsersPost`: \nheader: %s\n%s\n",
                    response.getHeader(), response.getData());
        } catch (ClientException e) {
            System.out.printf("Error when calling `UserManagerApi.v1UsersPost`: %s\n", e);
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            System.out.printf("Error when calling `UserManagerApi.v1UsersPost`: %s\n", e);
            System.out.printf("Full HTTP response: %s\n", new String(e.getApiResp().getRawBody()));
            throw new RuntimeException(e);
        }
    }


    /**
     * 邀请用户
     *
     * @param userId
     */
    public void inputMeetUser(String userId) {
        Client client = new Client.Builder().withAppId(config.getAppId()).withSdkId(config.getEntId()).withSecret(config.getSecretId(), config.getSecretKey()).build();

        String operatorId = config.getAdminUserId();
        // 2.构造请求参数
        UserManagerApi.ApiV1UsersUseridInviteActivatePutRequest request =
                new UserManagerApi.ApiV1UsersUseridInviteActivatePutRequest.Builder(userId)
                        .operatorId(operatorId)
                        .operatorIdType("1")
                        .build();
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

    public org.json.JSONObject createMeeting(String userId, String subject, Date startTime, CourseTypeEnum courseType) {
        String operatorId = config.getAdminUserId();
        int duration = courseType == CourseTypeEnum.GROUP ? 60 : 30;
        // 计算结束时间：startTime 时间 duration 分钟后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, duration);
        Date endTime = calendar.getTime();
        JSONObject params = new JSONObject();
        params.put("subject", subject);
        //startTime秒级时间戳
        String startLongTime = String.valueOf(startTime.getTime() / 1000);
        params.put("start_time", startLongTime);
        //endTime秒级时间戳
        String endLongTime = String.valueOf(endTime.getTime() / 1000);
        params.put("end_time", endLongTime);
        params.put("user_id", operatorId);
        params.put("instanceid", "1");
        params.put("type", 0);
        String BODY_JSON = params.toJSONString();
        Client client = new Client.Builder().withAppId(config.getAppId()).withSdkId(config.getEntId()).withSecret(config.getSecretId(), config.getSecretKey()).build();
        V1MeetingsPostRequest body = JSON.parseObject(BODY_JSON, V1MeetingsPostRequest.class);
        // 2.构造请求参数
        MeetingsApi.ApiV1MeetingsPostRequest request =
                new MeetingsApi.ApiV1MeetingsPostRequest.Builder()
                        .body(body).build();
        // 随机数
        BigInteger nonce = BigInteger.valueOf(Math.abs((new SecureRandom()).nextInt()));
        // 当前时间戳
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        AuthenticatorBuilder<JWTAuthenticator> authenticatorBuilder =
                new JWTAuthenticator.Builder().nonce(nonce).timestamp(timestamp);
        org.json.JSONObject resultObj = new org.json.JSONObject();
        resultObj.put("joinUrl", "");
        resultObj.put("meetingId", "");
        resultObj.put("startTime", startTime);
        // 4.发送对应的请求
        try {
            MeetingsApi.ApiV1MeetingsPostResponse response =
                    client.meetings().v1MeetingsPost(request, authenticatorBuilder);
            System.out.printf("Response from `MeetingsApi.v1MeetingsPost`: \nheader: %s\n%s\n",
                    response.getHeader(), response.getData());
            V1MeetingsPost200Response post200Response = response.getData();
            V1MeetingsPost200ResponseMeetingInfoListInner meetingInfoListInner = post200Response.getMeetingInfoList().get(0);
            String joinUtl = meetingInfoListInner.getJoinUrl();
            resultObj.put("joinUrl", joinUtl);
            String meetingId = meetingInfoListInner.getMeetingId();
            resultObj.put("meetingId", meetingId);
            log.info("joinUrl:{}", joinUtl);
            log.info("meetingId:{}", meetingInfoListInner.getMeetingId());
        } catch (ClientException e) {
            System.out.printf("Error when calling `MeetingsApi.v1MeetingsPost`: %s\n", e);
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            System.out.printf("Error when calling `MeetingsApi.v1MeetingsPost`: %s\n", e);
            System.out.printf("Full HTTP response: %s\n", new String(e.getApiResp().getRawBody()));
            throw new RuntimeException(e);
        }
        return resultObj;
    }


    public void closeMeeting(String userId, String meetingId) {
        JSONObject params = new JSONObject();
        params.put("reason_code", 1);
        params.put("user_id", userId);
        params.put("instanceid", 1);
        String BODY_JSON = params.toJSONString();
        Client client = new Client.Builder().withAppId(config.getAppId()).withSdkId(config.getEntId()).withSecret(config.getSecretId(), config.getSecretKey()).build();
        V1MeetingsMeetingIdCancelPostRequest body = JSON.parseObject(BODY_JSON, V1MeetingsMeetingIdCancelPostRequest.class);

        // 2.构造请求参数
        MeetingsApi.ApiV1MeetingsMeetingIdCancelPostRequest request =
                new MeetingsApi.ApiV1MeetingsMeetingIdCancelPostRequest.Builder(meetingId)
                        .body(body).build();
        // 3.构造 JWT 鉴权器
        BigInteger nonce = BigInteger.valueOf(Math.abs((new SecureRandom()).nextInt()));
        // 当前时间戳
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        AuthenticatorBuilder<JWTAuthenticator> authenticatorBuilder =
                new JWTAuthenticator.Builder().nonce(nonce).timestamp(timestamp);
        // 4.发送对应的请求
        try {
            MeetingsApi.ApiV1MeetingsMeetingIdCancelPostResponse response =
                    client.meetings().v1MeetingsMeetingIdCancelPost(request, authenticatorBuilder);
            // response from `v1MeetingsMeetingIdCancelPost`: Object
            System.out.printf("Response from `MeetingsApi.v1MeetingsMeetingIdCancelPost`: \nheader: %s\n%s\n",
                    response.getHeader(), response.getData());
        } catch (ClientException e) {
            System.out.printf("Error when calling `MeetingsApi.v1MeetingsMeetingIdCancelPost`: %s\n", e);
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            System.out.printf("Error when calling `MeetingsApi.v1MeetingsMeetingIdCancelPost`: %s\n", e);
            System.out.printf("Full HTTP response: %s\n", new String(e.getApiResp().getRawBody()));
            throw new RuntimeException(e);
        }
    }


    public V1UsersListGet200Response searchUserList(int page, int pageSize) {
        String operatorId = config.getAdminUserId();
        // 1.构造 client 客户端(jwt 鉴权需要配置 appId sdkId secretID 和 secretKey)
        Client client = new Client.Builder().withAppId(config.getAppId()).withSdkId(config.getEntId()).withSecret(config.getSecretId(), config.getSecretKey()).build();
        // 2.构造请求参数
        UserManagerApi.ApiV1UsersListGetRequest request =
                new UserManagerApi.ApiV1UsersListGetRequest.Builder()
                        .page("" + page)
                        .pageSize("+" + pageSize)
                        .operatorId(operatorId)
                        .operatorIdType("1")
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
            UserManagerApi.ApiV1UsersListGetResponse response =
                    client.user_manager().v1UsersListGet(request, authenticatorBuilder);
            // response from `v1UsersListGet`: V1UsersListGet200Response
            V1UsersListGet200Response response200 = response.getData();
            log.info("查询voov列表成功:{}", response200.getUsers().size());
            return response200;
        } catch (ClientException e) {
            System.out.printf("Error when calling `UserManagerApi.v1UsersListGet`: %s\n", e);
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            System.out.printf("Error when calling `UserManagerApi.v1UsersListGet`: %s\n", e);
            System.out.printf("Full HTTP response: %s\n", new String(e.getApiResp().getRawBody()));
            throw new RuntimeException(e);
        }
    }


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