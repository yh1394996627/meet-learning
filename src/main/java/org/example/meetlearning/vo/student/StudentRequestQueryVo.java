package org.example.meetlearning.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import lombok.EqualsAndHashCode;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.vo.common.PageRequestQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentRequestQueryVo extends PageRequestQuery<Student> {

    @Schema(name = "keyword", description = "模糊匹配")
    private String keyword;

    @Schema(name = "country", description = "国家")
    private String country;


    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(keyword)) {
            String keywordStr = "%" + keyword.toLowerCase() + "%";
            params.put("keyword", keywordStr);
        }
        if (StringUtils.hasText(country)) {
            params.put("country", country);
        }
        return params;
    }
}
