package org.example.meetlearning.vo.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherComplaintReqVo {

    @Schema(name = "recordId", description = "预约记录ID")
    private String recordId;

    @Schema(name = "remark", description = "评价原因")
    private String remark;

}
