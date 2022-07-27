package com.skf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skf.reggie.dto.DishDto;
import com.skf.reggie.entity.Dish;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
public interface DishService extends IService<Dish> {

    void addDish(com.skf.reggie.dto.DishDto dishDto);


    void updateDish(DishDto dishDto);
}
