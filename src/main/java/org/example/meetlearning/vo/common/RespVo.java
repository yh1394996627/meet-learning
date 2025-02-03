package org.example.meetlearning.vo.common;

import lombok.Getter;

@Getter
public class RespVo<T> {

    private final boolean success;

    private final T data;

    private final String errMsg;


    public RespVo(T data, boolean success, String errMsg) {
        this.success = success;
        this.data = data;
        this.errMsg = errMsg;
    }

    public RespVo(T data) {
        this.data = data;
        this.success = true;
        this.errMsg = null;
    }
}
