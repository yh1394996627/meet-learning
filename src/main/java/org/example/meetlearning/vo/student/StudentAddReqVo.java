package org.example.meetlearning.vo.student;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentAddReqVo {

    @Schema(name = "enName", description = "英文名称")
    private String enName;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "language", description = "语言")
    private String language;

    @Schema(name = "website", description = "网站")
    private String website;

}
