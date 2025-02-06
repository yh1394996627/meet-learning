package org.example.meetlearning.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentListRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "phone", description = "电话")
    private String phone;

    @Schema(name = "email", description = "邮件")
    private String email;

    @Schema(name = "balance", description = "余额")
    private BigDecimal balance;

    @Schema(name = "expirationTime", description = "课时币到期")
    private Date expirationTime;

    @Schema(name = "lastActivities", description = "最近活动")
    private String lastActivities;

    @Schema(name = "website", description = "网站")
    private String website;

    @Schema(name = "recommender", description = "推荐人")
    private String recommender;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "isDeleted", description = "是否可以删除")
    private Boolean isDeleted;


}
