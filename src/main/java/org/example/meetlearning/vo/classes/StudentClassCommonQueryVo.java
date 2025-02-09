package org.example.meetlearning.vo.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentClassCommonQueryVo {

    @Schema(name = "priceContent", description = "选择价格")
    private String priceContent;


}
