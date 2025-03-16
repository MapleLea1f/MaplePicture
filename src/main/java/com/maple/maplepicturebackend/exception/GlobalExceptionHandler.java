package com.maple.maplepicturebackend.exception;

import com.maple.maplepicturebackend.common.BaseResponse;
import com.maple.maplepicturebackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BussinessException.class)
    public BaseResponse<?> bussinessExceptionHandler(BussinessException e) {
        log.error("业务异常: ", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
