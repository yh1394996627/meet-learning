package org.example.meetlearning.vo.classes;


import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.vo.common.PageRequestQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class StudentClassQueryVo extends PageRequestQuery<StudentClass> {

    @Schema(name = "teacherKeyword", description = "老师模糊匹配")
    private String teacherKeyword;

    @Schema(name = "studentKeyword", description = "学生模糊匹配")
    private String studentKeyword;

    @Schema(name = "beginDate", description = "起始日期")
    private String beginDate;

    @Schema(name = "endDate", description = "结束日期")
    private String endDate;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(teacherKeyword)) {
            params.put("teacherKeyword", "%" + teacherKeyword + "%");
        }
        if (StringUtils.hasText(studentKeyword)) {
            params.put("studentKeyword", "%" + studentKeyword + "%");
        }
        if (StringUtils.hasText(beginDate) && StringUtils.hasText(endDate)) {
            params.put("beginDate", DateUtil.parse(beginDate,"yyyy-MM-dd"));
            params.put("endDate", DateUtil.parse(endDate,"yyyy-MM-dd"));
        }
        return params;
    }


}
