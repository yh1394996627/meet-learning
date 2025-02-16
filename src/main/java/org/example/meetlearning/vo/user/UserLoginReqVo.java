package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLoginReqVo {

    @Schema(name = "accountCode", description = "账号")
    private String accountCode;

    @Schema(name = "password", description = "密码")
    private String password;

}
