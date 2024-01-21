package com.zhaobo.spark.common;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(ResponseEnum resultEnum) {
        super(resultEnum.getDesc());

        this.code = resultEnum.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
