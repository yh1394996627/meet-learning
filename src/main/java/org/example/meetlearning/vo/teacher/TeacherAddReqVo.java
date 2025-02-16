package org.example.meetlearning.vo.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TeacherAddReqVo {

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "verifyCode", description = "验证码")
    private String verifyCode;

    @Schema(name = "enName", description = "英文名称")
    private String enName;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "managerId", description = "管理者ID")
    private String managerId;

}
