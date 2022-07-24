package com.skf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skf.reggie.entity.Employee;
import com.skf.reggie.mapper.EmployeeMapper;
import com.skf.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public int addEmployee(Employee employee) {
        int count = this.baseMapper.insert(employee);
        log.info("EmployeeServiceImpl.addEmployee add: {}", count);
        return count;
    }
}
