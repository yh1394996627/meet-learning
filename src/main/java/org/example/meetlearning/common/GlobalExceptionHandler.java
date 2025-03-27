package org.example.meetlearning.common;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public RespVo<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: {}, 请求路径: {}", e, request.getRequestURI());
        return new RespVo<>(null, false, e.getMessage());
    }

    /**
     * 处理Assert抛出的参数校验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public RespVo<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("参数校验异常: {}, 请求路径: {}", e, request.getRequestURI());
        return new RespVo<>(null, false, e.getMessage());
    }

    /**
     * 处理@Validated参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespVo<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.error("参数校验异常: {}, 请求路径: {}", message, request.getRequestURI());
        return new RespVo<>(null, false, message);
    }

    /**
     * 处理@Validated参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RespVo<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> {
                    String path = violation.getPropertyPath().toString();
                    return path.substring(path.lastIndexOf('.') + 1) + ": " + violation.getMessage();
                })
                .collect(Collectors.joining("; "));

        log.error("参数校验异常: {}, 请求路径: {}", message, request.getRequestURI());
        return new RespVo<>(null, false, message);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public RespVo<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {}, 请求路径: {}", e, request.getRequestURI(), e);
        return new RespVo<>(null, false, e.getMessage());
    }
}