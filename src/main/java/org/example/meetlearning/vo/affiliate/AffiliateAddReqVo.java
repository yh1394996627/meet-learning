package org.example.meetlearning.vo.affiliate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AffiliateAddReqVo {

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "verifyCode", description = "验证码")
    private String verifyCode;

    @Schema(name = "enName", description = "英文名")
    private String enName;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "recommenderId", description = "推荐人ID")
    private String recommenderId;

    @Schema(name = "recommender", description = "推荐人")
    private String recommender;

}
