package org.example.meetlearning.controller.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseHandler;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.vo.common.EmailSendVo;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "邮件服务")
@RestController
@AllArgsConstructor
public class MailHandler implements BaseHandler {

    private final EmailPcService emailPcService;

    @Operation(summary = "邮件服务", operationId = "sharedTeacherList")
    @PostMapping(value = "v1/mail/send")
    public RespVo<String> sendVerificationEmail(@RequestBody EmailSendVo emailSendVo) throws Exception {
        return emailPcService.sendVerificationEmail(getUserCode(), emailSendVo.getMail());
    }
}
