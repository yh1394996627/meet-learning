package org.example.meetlearning.vo.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class StudentClassCommonQueryVo {

    @Schema(name = "priceContent", description = "选择的价格")
    private BigDecimal priceContent;

    @Schema(name = "courseDate", description = "课程时间")
    private String courseDate;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (priceContent != null) {
            params.put("tokenPrice", priceContent);
        }
        return params;
    }
}
