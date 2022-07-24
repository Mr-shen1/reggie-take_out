package com.skf.reggie.controller.employee;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skf.reggie.common.R;
import com.skf.reggie.entity.Employee;
import com.skf.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping()
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public R addEmployee(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        // 初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        // 获取当前登录用户的id, 只有后台的管理员才有添加员工的权限, 记录操作人
        int count = employeeService.addEmployee(employee);
        if (count != 1) {
            return R.error("添加失败");
        }
        return R.success("添加成功");
    }

    @GetMapping("/employee/page")
    public R queryEmployees(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 过滤
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping("/employee")
    public R<String> update(@RequestBody Employee employee) {
        log.info(employee.toString());
        boolean flag = employeeService.updateById(employee);
        if (flag) {
            return R.success("修改状态成功");
        }
        return R.error("修改状态失败");
    }

    @GetMapping("/employee/{id}")
    public R<Employee> queryEmployeeById(@PathVariable Long id) {
        if (Objects.isNull(id)) {
            return R.error("该用户不存在");
        }
        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return R.error("该用户不存在");
        }
        return R.success(employee);
    }
}
