package com.skf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skf.reggie.entity.Dish;
import com.skf.reggie.mapper.DishMapper;
import com.skf.reggie.service.DishService;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}
