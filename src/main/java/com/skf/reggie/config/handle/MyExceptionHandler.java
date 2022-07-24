package com.skf.reggie.config.handle;

import com.skf.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            return R.error("该用户名已存在");
        }
        return R.error("未知错误");
    }
}
