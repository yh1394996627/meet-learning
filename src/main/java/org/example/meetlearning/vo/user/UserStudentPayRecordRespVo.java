package org.example.meetlearning.vo.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserStudentPayRecordRespVo {

    @Schema(name = "userId", description = "用户ID")
    private String userId;

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "quantity", description = "数量")
    private BigDecimal quantity;

    @Schema(name = "usedQty", description = "已使用数量")
    private BigDecimal usedQty;

    @Schema(name = "canQty", description = "可用数量")
    private BigDecimal canQty;

    @Schema(name = "expirationTime", description = "失效日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expirationTime;


}
