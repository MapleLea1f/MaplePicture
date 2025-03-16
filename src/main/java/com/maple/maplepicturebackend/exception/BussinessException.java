package com.maple.maplepicturebackend.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 */
@Getter
public class BussinessException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    public BussinessException(int code, String message) {
        super(message); // 继承RuntimeException的错误码消息
        this.code = code;
    }

    public BussinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BussinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
