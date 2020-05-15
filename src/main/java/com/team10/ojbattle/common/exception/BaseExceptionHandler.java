package com.team10.ojbattle.common.exception;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @package: com.changgou.controller
 * @author: 陈健航
 * @description: 全局异常处理器
 * @date: 2020/3/21 17:33
 * @version: 1.0
 */
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(MyException.class)
    @ResponseBody
    public R<String> myError(MyException e) {
        e.printStackTrace();
        //处理的是自定义异常
        return R.failed(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R<String> error(Exception e) {
        e.printStackTrace();
        //处理的是自定义异常
        return R.failed(e.getMessage());
    }


}
