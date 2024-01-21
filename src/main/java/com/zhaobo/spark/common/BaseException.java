package com.zhaobo.spark.common;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public class BaseException extends Exception {
    private CommonError commonError;

    public BaseException(EmBusinessError emBusinessError){
        super();
        this.commonError = new CommonError(emBusinessError);
    }

    public BaseException(EmBusinessError emBusinessError,String errMsg){
        super();
        this.commonError = new CommonError(emBusinessError);
        this.commonError.setErrMsg(errMsg);
    }

    public CommonError getCommonError() {
        return commonError;
    }

    public void setCommonError(CommonError commonError) {
        this.commonError = commonError;
    }
}
