package org.example.meetlearning.vo.remark;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.TeacherClassRemark;
import org.example.meetlearning.vo.common.PageRequestQuery;

@Data
public class TeacherClassRemarkQueryVo extends PageRequestQuery<TeacherClassRemark> {

    @Schema(name = "studentId", description = "学生ID")
    private String studentId;

}
