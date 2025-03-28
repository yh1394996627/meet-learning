package org.example.meetlearning.vo.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherEvaluationReqVo {

    @Schema(name = "recordId", description = "预约记录ID")
    private String recordId;

    @Schema(name = "rating", description = "评价 1-5")
    private BigDecimal rating;

    @Schema(name = "remark", description = "评价原因")
    private String remark;

}
