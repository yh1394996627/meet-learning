package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.meetlearning.enums.RoleEnum;
import org.springframework.util.StringUtils;

@Data
public class UserLoginReqVo {

    @Schema(name = "accountCode", description = "账号")
    @NotNull(message = "账号不能为空")
    private String accountCode;

    @Schema(name = "password", description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(name = "manage", description = "是否是管理登录")
    private Boolean manage;

    @Schema(name = "domain", description = "域名")
    private String domain;


    @Schema(hidden = true)
    public RoleEnum getRole() {
        if (!StringUtils.hasText(domain)) {
            return RoleEnum.MANAGER;
        }
        return switch (domain) {
            case "www.12talk.com" -> RoleEnum.STUDENT;
            case "teacher.12talk.com" -> RoleEnum.TEACHER;
            case "agent.12talk.com" -> RoleEnum.AFFILIATE;
            default -> RoleEnum.MANAGER;
        };
    }
}
