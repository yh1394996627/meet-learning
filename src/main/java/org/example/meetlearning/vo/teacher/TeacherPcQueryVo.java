package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.enums.ScheduleWeekEnum;
import org.example.meetlearning.vo.common.PageRequestQuery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Data
public class TeacherPcQueryVo extends PageRequestQuery<Teacher> {

    @Schema(name = "date", description = "选择日期")
    private String date;

    @Schema(name = "nameKeyword", description = "名称模糊匹配")
    private String nameKeyword;

    @Schema(name = "beginTime", description = "开始时间")
    private String beginTime;

    @Schema(name = "endTime", description = "结束时间")
    private String endTime;

    @Schema(name = "gender", description = "性别")
    private Integer gender;

    @Schema(name = "specialists", description = "擅长")
    private String specialists;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "courseType", description = "课程类型")
    private String courseType;


    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (gender != null) {
            params.put("gender", gender);
        }
        if (!StringUtils.isEmpty(country)) {
            params.put("country", this.country);
        }
        if (!StringUtils.isEmpty(courseType)) {
            if (StringUtils.equals("GROUP", courseType)) {
                params.put("groupStatus", true);
            } else if (StringUtils.equals("TEST", courseType)) {
                params.put("testStatus", true);
            }
        }
        return params;
    }

    @Schema(hidden = true)
    public ScheduleWeekEnum getWeek() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return ScheduleWeekEnum.valueOf(dayOfWeek.toString());
    }

    public String getBeginTime() {
        return StringUtils.isEmpty(beginTime) ? "00:00:00" : beginTime;
    }

    public String getEndTime() {
        return StringUtils.isEmpty(endTime) ? " 23:59:59" : endTime;
    }

}
