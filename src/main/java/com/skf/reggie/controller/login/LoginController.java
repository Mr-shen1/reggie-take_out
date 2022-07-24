package com.skf.reggie.controller.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.skf.reggie.common.R;
import com.skf.reggie.entity.Employee;
import com.skf.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class LoginController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录功能
     *
     * @param httpServletRequest httpServletRequest
     * @param employee employee
     * @return R
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        // MD5摘要
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (Objects.isNull(emp)) {
            return R.error("登录失败");
        }

        if (!Objects.equals(password, emp.getPassword())) {
            return R.error("登录失败");
        }

        if (Objects.equals(emp.getStatus(), 0)) {
            return R.error("账号已禁用");
        }
        httpServletRequest.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }


    /**
     * 注销
     *
     * @param httpServletRequest httpServletRequest
     * @return R
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

}
