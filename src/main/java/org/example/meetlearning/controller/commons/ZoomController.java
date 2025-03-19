package org.example.meetlearning.controller.commons;

import lombok.AllArgsConstructor;
import org.example.meetlearning.service.ZoomPcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class ZoomController {

    private final ZoomPcService zoomPcService;


    @GetMapping("/zoom/callback")
    public String handleCallback(@RequestParam("code") String authorizationCode) throws IOException {
        // 使用授权码获取 Access Token
        String accessToken = getAccessToken(authorizationCode);
        return "Authorization code: " + authorizationCode + ", Access Token: " + accessToken;
    }

    private String getAccessToken(String authorizationCode) throws IOException {
        return zoomPcService.getAccessToken(authorizationCode);
    }
}