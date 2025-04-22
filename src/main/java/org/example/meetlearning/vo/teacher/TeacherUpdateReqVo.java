package org.example.meetlearning.vo.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.vo.common.SelectValueVo;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TeacherUpdateReqVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "avatarUrl", description = "头像路径")
    private String avatarUrl;

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

    @Schema(name = "groupPrice", description = "团体课单价")
    private BigDecimal groupPrice;

    @Schema(name = "creditsPrice", description = "积分单价")
    private BigDecimal creditsPrice;

    @Schema(name = "currencyCode", description = "币种编码")
    private String currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private String currencyName;

    @Schema(name = "currencySymbol", description = "币种符号")
    private String currencySymbol;

    @Schema(name = "rate", description = "比率")
    private BigDecimal rate;

    @Schema(name = "groupRate", description = "团体课比率")
    private BigDecimal groupRate;

    @Schema(name = "managerId", description = "管理ID")
    private String managerId;

    @Schema(name = "manager", description = "管理")
    private String manager;

    @Schema(name = "videoUrl", description = "视频路径")
    private String videoUrl;

    @Schema(name = "specialists", description = "教学特点")
    private List<String> specialists;

    @Schema(name = "about", description = "关于")
    private String about;

    @Schema(name = "gender", description = "性别 1男 2女 3其他")
    private Integer gender;


}
