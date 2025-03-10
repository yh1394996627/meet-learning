package org.example.meetlearning.vo.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class StudentClassCommonQueryVo {

    @Schema(name = "price", description = "选择的价格")
    private BigDecimal price;

    //todo 塞选出有空的老师
    @Schema(name = "courseDate", description = "课程时间")
    private String courseDate;

    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (price != null) {
            params.put("tokenPrice", price);
        }
        return params;
    }


}
