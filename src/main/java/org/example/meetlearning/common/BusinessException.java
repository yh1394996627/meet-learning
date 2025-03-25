package org.example.meetlearning.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import okhttp3.internal.http2.ErrorCode;

/**
 * 基础异常类
 */
@Getter
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;
    private Object[] args;

    public BusinessException(Integer code, String message, Object... args) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = args;
    }

}