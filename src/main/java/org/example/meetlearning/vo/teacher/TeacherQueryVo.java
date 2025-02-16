package org.example.meetlearning.vo.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.vo.common.PageRequestQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class TeacherQueryVo extends PageRequestQuery<Student> {

    @Schema(name = "managerCode", description = "管理者过滤code")
    private String managerCode;

    @Schema(name = "countryCode", description = "国家")
    private String countryCode;

    @Schema(name = "teacherType", description = "老师类型 null:所有 0:测试 1:正式")
    private Integer teacherType;

    @Schema(name = "accountStatus", description = "状态过滤 null:所有 0:启用 1:禁用")
    private Integer accountStatus;

    @Schema(name = "keyword", description = "模糊匹配")
    private String keyword;


    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(keyword)) {
            String keywordStr = "%" + keyword.toLowerCase() + "%";
            params.put("keyword", keywordStr);
        }
        return params;
    }
}
