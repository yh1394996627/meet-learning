package org.example.meetlearning.controller.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.vo.common.EmailSendVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "邮件服务")
@RestController
@AllArgsConstructor
public class MailController implements BaseController {

    private final EmailPcService emailPcService;

    @Operation(summary = "邮件服务", operationId = "sharedTeacherList")
    @PostMapping(value = "v1/mail/send")
    public RespVo<String> sendVerificationEmail(@RequestBody EmailSendVo emailSendVo) throws Exception {
        return emailPcService.sendVerificationEmail(getUserCode(), emailSendVo.getMail());
    }
}
