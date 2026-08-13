package org.example.meetlearning.vo.classes;


import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.vo.common.PageRequestQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
public class StudentClassQueryVo extends PageRequestQuery<StudentClass> {

    @Schema(name = "teacherKeyword", description = "老师精准匹配（忽略大小写，匹配名称/邮箱，自动去掉前后空格）")
    private String teacherKeyword;

    @Schema(name = "studentKeyword", description = "学生模糊匹配")
    private String studentKeyword;

    @Schema(name = "beginDate", description = "起始日期")
    private String beginDate;

    @Schema(name = "endDate", description = "结束日期")
    private String endDate;

    @Schema(name = "studentClassStatus", description = "课程状态 0未上课 1已上课")
    private Integer studentClassStatus;

    @Schema(name = "studentCountry", description = "学生国家")
    private String studentCountry;

    @Schema(name = "teacherCountry", description = "老师国家")
    private String teacherCountry;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        String normalizedTeacherKeyword = teacherKeyword == null ? null : teacherKeyword.trim();
        if (StringUtils.hasText(normalizedTeacherKeyword)) {
            params.put("teacherKeyword", normalizedTeacherKeyword.toLowerCase(Locale.ROOT));
        }
        if (StringUtils.hasText(studentKeyword)) {
            params.put("studentKeyword", "%" + studentKeyword + "%");
        }
        if (StringUtils.hasText(beginDate) && StringUtils.hasText(endDate)) {
            params.put("beginDate", DateUtil.parse(beginDate, "yyyy-MM-dd"));
            params.put("endDate", DateUtil.parse(endDate, "yyyy-MM-dd"));
        }
        if (studentClassStatus != null) {
            params.put("isUnClass", studentClassStatus == 0);
        }
        if (StringUtils.hasText(studentCountry)) {
            params.put("studentCountry", studentCountry);
        }
        if (StringUtils.hasText(teacherCountry)) {
            params.put("teacherCountry", teacherCountry);
        }
        return params;
    }
}
