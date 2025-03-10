package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class TeacherLastCommentRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

    @Schema(name = "comment", description = "评论")
    private String comment;

    @Schema(name = "createTime", description = "创建时间")
    private Date createTime;
}
