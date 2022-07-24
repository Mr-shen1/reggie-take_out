package com.skf.reggie.controller.employee;

import com.skf.reggie.common.R;
import com.skf.reggie.entity.Employee;
import com.skf.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@RestController
@RequestMapping()
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public R addEmployee(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        // 初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        // 获取当前登录用户的id, 只有后台的管理员才有添加员工的权限, 记录操作人
        Long id = (Long) httpServletRequest.getSession().getAttribute("employee");
        employee.setCreateUser(id);
        employee.setUpdateUser(id);
        int count = employeeService.addEmployee(employee);
        if (count != 1) {
            return R.error("添加失败");
        }
        return R.success("添加成功");
    }
}
