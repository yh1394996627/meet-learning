package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.enums.RoleEnum;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserInfoRespVo {

    @Schema(name = "recordId", description = "用户ID")
    private String recordId;

    @Schema(name = "recordId", description = "名称")
    private String name;

    @Schema(name = "enName", description = "英文名")
    private String enName;

    @Schema(name = "avatarUrl", description = "头像路径")
    private String avatarUrl;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "menus", description = "菜单集合")
    private List<String> menus;

    @Schema(name = "role", description = "角色")
    private String role;

    @Schema(name = "creditsBalance", description = "积分金额")
    private BigDecimal creditsBalance;

    @Schema(name = "balanceQty", description = "代币余额")
    private BigDecimal balanceQty;

    @Schema(name = "zfbQrCode", description = "支付宝二维码")
    private String zfbQrCode;

    @Schema(name = "wxQrCode", description = "微信二维码")
    private String wxQrCode;

    @Schema(name = "token", description = "token")
    private String token;

    @Schema(name = "qfUserId", description = "奇峰用户ID")
    private Long qfUserId;

}
