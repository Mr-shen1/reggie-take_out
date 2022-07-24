package com.skf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skf.reggie.entity.Employee;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
public interface EmployeeService extends IService<Employee> {
    /**
     * 添加员工
     *
     * @param employee employee
     * @return 数量
     */
    int addEmployee(Employee employee);
}
