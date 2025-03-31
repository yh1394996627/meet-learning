package org.example.meetlearning.service.impl;

import com.aliyun.teautil.models.RuntimeOptions;
import io.lettuce.core.internal.LettuceLists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.common.ZoomUseRedisSetCommon;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.util.RedisCommonsUtil;
import org.example.meetlearning.vo.zoom.Registrant;
import org.example.meetlearning.vo.zoom.ZoomAccount;
import org.example.meetlearning.vo.zoom.ZoomAccountInfoVo;
import org.example.meetlearning.vo.zoom.ZoomBaseVerifyRespVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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


    private OkHttpClient client = new OkHttpClient();

    private Map<Integer, List<ZoomAccount>> accountPools = new ConcurrentHashMap<>();


    /**
     * 初始化连接池
     **/
    @PostConstruct
    public void init() {
        accountPools.put(1, new ArrayList<>());
        accountPools.put(2, new ArrayList<>());
        accountPools.put(3, new ArrayList<>());
        accountPools.put(4, new ArrayList<>());

        List<ZoomAccountSet> zoomAccountSets = zoomBaseService.selectActivation();
        zoomAccountSets.forEach(zoomAccountSet -> {
            ZoomAccount zoomAccount = new ZoomAccount(zoomAccountSet.getZoomUserId(), zoomAccountSet.getZoomAccountId(), zoomAccountSet.getZoomType(), zoomAccountSet.getZoomClientId(), zoomAccountSet.getZoomClientSecret(), 0);
            Object obj = redisTemplate.opsForValue().get(zoomAccountSet.getZoomAccountId() + ":" + zoomAccountSet.getZoomType());
            if (obj != null) {
                zoomAccount.setApiCallCount(Integer.parseInt(obj.toString()));
            }
            List<ZoomAccount> zoomAccounts = accountPools.get(zoomAccount.getZoomType());
            if (CollectionUtils.isEmpty(zoomAccounts)) {
                zoomAccounts = new ArrayList<>();
            }
            zoomAccounts.add(zoomAccount);
            accountPools.put(zoomAccount.getZoomType(), zoomAccounts);
        });
    }

    private ZoomAccount getZoomAccount(Integer zoomType) {
        List<ZoomAccount> zoomAccounts = accountPools.get(zoomType);
        if (CollectionUtils.isEmpty(zoomAccounts)) {
            List<ZoomAccount> zoom1Accounts = new ArrayList<>();
            //查詢付費版
            zoom1Accounts.addAll(accountPools.get(2));
            zoom1Accounts.addAll(accountPools.get(3));
            zoom1Accounts.addAll(accountPools.get(4));
            if (CollectionUtils.isEmpty(zoom1Accounts)) {
                return null;
            }
            zoom1Accounts = zoom1Accounts.stream().sorted(Comparator.comparing(ZoomAccount::getApiCallCount)).toList();
            return zoom1Accounts.get(0);

        }
        zoomAccounts = zoomAccounts.stream().sorted(Comparator.comparing(ZoomAccount::getApiCallCount)).toList();
        return zoomAccounts.get(0);
    }

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
            redisTemplate.opsForValue().set(clientId + clientSecret + accountId, accessToken, 3590, TimeUnit.SECONDS);
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
            return new ZoomBaseVerifyRespVo(response.getStatusCode() == HttpStatus.OK, response.getStatusCode().value(), response.getBody(), null);
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
    public String createMeeting(String topic, String startTime, CourseTypeEnum courseType) throws IOException {
        //获取ZOOM生效的账号
        Integer zoomType = courseType == CourseTypeEnum.GROUP ? 2 : 1;
        ZoomAccount zoomAccount = getZoomAccount(zoomType);
        Assert.notNull(zoomAccount, "Zoom account not found");
        Assert.isTrue(StringUtils.isNotEmpty(zoomAccount.getZoomUserId()), "Zoom userId not found");
        String accessToken = getValidAccessToken(zoomAccount.getClientId(), zoomAccount.getClientSecret(), zoomAccount.getAccountId());
        String url = zoomApiUrl + "/users/" + zoomAccount.getZoomUserId() + "/meetings";
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

    public String createMeeting11(String userId, String topic, String startTime, CourseTypeEnum courseType) throws IOException {
        //获取ZOOM生效的账号
        Integer zoomType = courseType == CourseTypeEnum.GROUP ? 2 : 1;
        ZoomAccount zoomAccount = getZoomAccount(zoomType);
        Assert.notNull(zoomAccount, "Zoom account not found");
        Assert.isTrue(StringUtils.isNotEmpty(zoomAccount.getZoomUserId()), "Zoom userId not found");
        String accessToken = getValidAccessToken(zoomAccount.getClientId(), zoomAccount.getClientSecret(), zoomAccount.getAccountId());
        String url = zoomApiUrl + "/users/" + userId + "/meetings";
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



    /**
     * 创建 Zoom 用户并获取激活链接
     * @param email 用户邮箱
     * @param firstName 名
     * @param lastName 姓
     * @return 激活链接（如果用户需激活）
     */
    public String createZoomUserAndGetActivationLink(String email, String firstName, String lastName, String zoomApiToken) {
        String url = zoomApiUrl + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(zoomApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Zoom API 请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("action", "create"); // 创建用户
        requestBody.put("user_info", Map.of(
                "email", email,
                "first_name", firstName,
                "last_name", lastName,
                "type", 1 // 1=基本用户，2=授权用户
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // 检查是否需要激活（新用户会返回激活链接）
            if (responseBody.containsKey("activation_url")) {
                return (String) responseBody.get("activation_url");
            } else {
                throw new RuntimeException("用户已存在或无需激活");
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Zoom API 调用失败: " + e.getResponseBodyAsString());
        }
    }
}