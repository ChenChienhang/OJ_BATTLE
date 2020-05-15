package com.team10.ojbattle.common.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import lombok.Data;

/**
 * @author: 陈健航
 * @description: 自定义异常类
 * @since: 2020/3/22 11:53
 * @version: 1.0
 */
@Data
public class MyException extends RuntimeException {

    private IErrorCode errorCode;

    public MyException(IErrorCode errorCode) {
        this.errorCode = errorCode;
    };

}
