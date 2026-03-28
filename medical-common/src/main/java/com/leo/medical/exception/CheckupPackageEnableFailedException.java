package com.leo.medical.exception;

/**
 * 医疗体验套餐启用失败异常
 */
public class CheckupPackageEnableFailedException extends BaseException {

    public CheckupPackageEnableFailedException(){}

    public CheckupPackageEnableFailedException(String msg){
        super(msg);
    }
}
