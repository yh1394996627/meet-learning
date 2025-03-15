package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class TeacherLastCommentRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "teacherId", description = "老师ID")
    private String teacherId;

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

    @Schema(name = "courseId", description = "关联课程")
    private String courseId;

    @Schema(name = "courseName", description = "课程名称")
    private String courseName;

    @Schema(name = "createTime", description = "创建时间")
    private Date createTime;

    @Schema(name = "comment", description = "评论")
    private String comment;

}
