package org.example.meetlearning.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "balance", description = "余额")
    private BigDecimal balance;

    @Schema(name = "expirationTime", description = "课时币最近到期时间及数量，格式：日期/数量")
    private String expirationTime;

    @Schema(name = "lastActivities", description = "最近活动")
    private String lastActivities;

    @Schema(name = "website", description = "网站")
    private String website;

    @Schema(name = "affiliateId", description = "代理商ID")
    private String affiliateId;

    @Schema(name = "affiliateName", description = "代理商名称")
    private String affiliateName;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "isDeleted", description = "是否可以删除")
    private Boolean isDeleted;

    @Schema(name = "version", description = "版本号")
    private Long version;


}
