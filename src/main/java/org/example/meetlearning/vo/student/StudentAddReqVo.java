package org.example.meetlearning.vo.student;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.enums.LanguageEnum;

@Data
public class StudentAddReqVo {

    @Schema(name = "enName", description = "英文名称")
    private String enName;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "language", description = "语言")
    private LanguageEnum language;

    @Schema(name = "website", description = "网站")
    private String website;

}
