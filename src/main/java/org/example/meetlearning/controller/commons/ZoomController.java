package org.example.meetlearning.controller.commons;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

;

@RestController
@AllArgsConstructor
@Slf4j
@EnableAsync
public class ZoomController implements BaseController {

    private final ZoomPcService zoomPcService;
    private final ZoomOAuthService zoomOAuthService;

    @GetMapping("/zoom/callback")
    public String handleCallback(@RequestParam("code") String authorizationCode) throws IOException {
        return authorizationCode;
    }

    @PostMapping("/api/zoom/event/callback")
    public ResponseEntity<String> handleZoomEvent(
            @RequestHeader(value = "authorization", required = false) String authToken,
            @RequestBody String payload) {
        return zoomPcService.handleZoomEvent(authToken, payload);
    }


    @GetMapping("/api/zoom/event/callback")
    public String verifyWebhook(@RequestParam("zoom_verification_token") String token) {
        // 返回验证令牌以验证 URL
        log.info("token:{}", token);
        return token;
    }

    @Operation(summary = "判断本地ZOOM是否存在", operationId = "isZoomInstalled")
    @PostMapping(value = "/v1/zoom/installed")
    public RespVo<Boolean> isZoomInstalled() {
        return new RespVo<>(zoomPcService.isZoomInstalled(getUserCode()));
    }


    @Operation(summary = "ZOOM回调异常处理接口", operationId = "handleZoomEventEx")
    @PostMapping("/api/zoom/callback/ex")
    public RespVo<String> handleZoomEventEx(@RequestBody List<String> recordIds) {
        zoomPcService.handleZoomEventEx(recordIds);
        return new RespVo<>("处理成功");
    }

}