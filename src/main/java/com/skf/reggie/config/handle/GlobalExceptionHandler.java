package com.skf.reggie.config.handle;

import com.skf.reggie.common.R;
import com.skf.reggie.common.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MyException.class)
    public R<String> exceptionHandler(MyException ex) {
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
