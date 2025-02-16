package org.example.meetlearning.vo.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherUpdateReqVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "avatarUtl", description = "头像路径")
    private String avatarUtl;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "language", description = "语言")
    private String language;

    @Schema(name = "price", description = "单价")
    private BigDecimal price;

    @Schema(name = "creditsPrice", description = "积分单价")
    private BigDecimal creditsPrice ;

    @Schema(name = "currencyCode", description = "币种编码")
    private BigDecimal currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private BigDecimal currencyName;

    @Schema(name = "currencySymbol", description = "币种符号")
    private BigDecimal currencySymbol;

    @Schema(name = "rate", description = "比率")
    private BigDecimal rate;

    @Schema(name = "managerId", description = "管理ID")
    private String managerId;

    @Schema(name = "specialties", description = "特点")
    private String specialties;

    @Schema(name = "viewUtl", description = "视频路径")
    private String viewUtl;


}
