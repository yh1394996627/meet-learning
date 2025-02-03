package org.example.meetlearning.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentStatementRecordReqVo {

    @Schema(name = "recordId", description = "业务ID")
    private Date recordId;

    @Schema(name = "createTime", description = "日期")
    private Date createTime;

    @Schema(name = "quantity", description = "数量")
    private BigDecimal quantity;

    @Schema(name = "balance", description = "剩余金额")
    private BigDecimal balance;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "createName", description = "操作人")
    private String createName;


}
