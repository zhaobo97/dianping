package com.zhaobo.spark.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;


/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class RuntimeExceptionHandler {
    /**
     * @ResponseStatus(HttpStatus.FORBIDDEN) // 指定http错误码
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo<String> handle (RuntimeException e){
        log.info("error:{}",e.getMessage());
        return  ResponseVo.error(ResponseEnum.ERROR,e.getMessage());
    }



    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseVo<String> handlerSellerException(BusinessException e) {
        return ResponseVo.error(ResponseEnum.ERROR, e.getMessage());
    }

    /**
     * 统一拦截  BindingResult 对象就不需要放在参数里边
     * 参数异常校验 @NOTNULLL
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo<String> notValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult.getFieldError().getField()
                + " " + bindingResult.getFieldError().getDefaultMessage());
    }
}
