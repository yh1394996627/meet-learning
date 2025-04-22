package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.vo.common.FileRecordVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TeacherInfoRespVo {

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

    @Schema(name = "createTime", description = "创建时间")
    private Date createTime;

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "name", description = "姓名")
    private String name;

    @Schema(name = "enName", description = "英文名")
    private String enName;

    @Schema(name = "attendance", description = "出席率")
    private BigDecimal attendance;

    @Schema(name = "rating", description = "等级")
    private BigDecimal rating;

    @Schema(name = "price", description = "价格")
    private BigDecimal price;

    @Schema(name = "groupPrice", description = "团体价格")
    private BigDecimal groupPrice;

    @Schema(name = "creditsPrice", description = "积分价格")
    private BigDecimal creditsPrice;

    @Schema(name = "rate", description = "比例")
    private BigDecimal rate;

    @Schema(name = "groupRate", description = "团体比例")
    private BigDecimal groupRate;

    @Schema(name = "confirmedQty", description = "确认数量")
    private BigDecimal confirmedQty;

    @Schema(name = "canceledQty", description = "取消数量")
    private BigDecimal canceledQty;

    @Schema(name = "complaintsQty", description = "投诉数量")
    private BigDecimal complaintsQty;

    @Schema(name = "salaryAmount", description = "薪水")
    private BigDecimal salaryAmount;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "managerId", description = "管理人")
    private String managerId;

    @Schema(name = "manager", description = "管理人")
    private String manager;

    @Schema(name = "avatarUrl", description = "头像")
    private String avatarUrl;

    @Schema(name = "language", description = "语言")
    private String language;

    @Schema(name = "gender", description = "性别")
    private String gender;

    @Schema(name = "currencyCode", description = "货币")
    private String currencyCode;

    @Schema(name = "currencyName", description = "货币名称")
    private String currencyName;

    @Schema(name = "specialties", description = "专业")
    private List<String> specialties;

    @Schema(name = "managerStatus", description = "管理人状态")
    private Boolean managerStatus;

    @Schema(name = "testStatus", description = "测试老师状态")
    private Boolean testStatus;

    @Schema(name = "enabledStatus", description = "启用状态")
    private Boolean enabledStatus;

    @Schema(name = "videoUrl", description = "视频地址")
    private String videoUrl;

    @Schema(name = "fileRecordVos", description = "附件")
    private List<FileRecordVo> fileRecordVos;

}
