package org.example.meetlearning.util;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class LoginUser {
    /**
     * 游客ID
     */
    public static final Long VISITOR_ID = 0L;
    public static final int CATEGORY_VISITOR = 0;
    public static final int CATEGORY_REGISTER = 1;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户类型
     */
    private Integer category;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头象
     */
    private String avatar;
    /**
     * 设备id
     */
    private String deviceId;

    private Integer days;

    private Long expireAt;

    private Map<String, Object> extensions = new LinkedHashMap<>();

    public static LoginUser create(Long userId,
                                   String tenantId,
                                   Integer category,
                                   String userName,
                                   String nickName,
                                   String avatar,
                                   String deviceId,
                                   Integer days) {
        LoginUser login = new LoginUser();
        login.tenantId = tenantId;
        login.userId = userId;
        login.category = category;
        login.userName = userName;
        login.nickName = nickName;
        login.avatar = avatar;
        login.days = days;
        login.expireAt = System.currentTimeMillis() + days * 24 * 60 * 60 * 1000;
        login.deviceId = deviceId;
        return login;
    }

    public boolean isVisitor() {
        return VISITOR_ID.equals(this.userId) || CATEGORY_VISITOR == this.userId;
    }
}