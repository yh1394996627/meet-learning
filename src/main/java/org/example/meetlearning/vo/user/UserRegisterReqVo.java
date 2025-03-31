package org.example.meetlearning.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRegisterReqVo {

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "verifyCode", description = "验证码")
    private String verifyCode;

    @Schema(name = "enName", description = "英文名")
    private String enName;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "affiliateId", description = "代理商Id")
    private String affiliateId;

    @Schema(name = "role", description = "注册权限 学生：STUDENT")
    private String role;
}
