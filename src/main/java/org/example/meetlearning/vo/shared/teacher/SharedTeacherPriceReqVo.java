package org.example.meetlearning.vo.shared.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SharedTeacherPriceReqVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "price", description = "设置的单价")
    private BigDecimal price;

}
