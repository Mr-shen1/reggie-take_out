package com.skf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.skf.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
