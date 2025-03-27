package org.example.meetlearning.vo.manager;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ManagerFinanceStudentRecordRespVo {

    @Schema(name = "recordId", description = "记录ID")
    private String recordId;

    @Schema(name = "userEmail", description = "用户邮箱")
    private String userEmail;

    @Schema(name = "userName", description = "用户名称")
    private String userName;

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private String currencyName;

    @Schema(name = "paymentId", description = "付款方式ID")
    private String paymentId;

    @Schema(name = "paymentName", description = "付款方式名称")
    private String paymentName;

    @Schema(name = "note", description = "备注")
    private String note;

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

    @Schema(name = "createTime", description = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
