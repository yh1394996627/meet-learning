package org.example.meetlearning.vo.common;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailSendVo {

    @Schema(name = "mail", description = "邮箱")
    private String mail;

}
