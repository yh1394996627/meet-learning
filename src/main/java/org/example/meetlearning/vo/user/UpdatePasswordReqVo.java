package org.example.meetlearning.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePasswordReqVo {

    @Schema(name = "recordId", description = "用户id")
    private String recordId;

    @Schema(name = "accountCode", description = "登陆账号")
    private String accountCode;

    @Schema(name = "verifyCode", description = "验证码")
    private String verifyCode;

    @Schema(name = "password", description = "新密码")
    private String password;


}
