package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserStudentPayInfoVo {

    @Schema(name = "userId", description = "用户ID 学生/老师/代理商")
    private String userId;

    @Schema(name = "name", description = "用户名")
    private String name;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "balanceQty", description = "可用余额")
    private BigDecimal balanceQty;


}
