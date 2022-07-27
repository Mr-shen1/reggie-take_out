package com.skf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skf.reggie.dto.DishDto;
import com.skf.reggie.entity.Dish;
import com.skf.reggie.entity.DishFlavor;
import com.skf.reggie.mapper.DishMapper;
import com.skf.reggie.service.DishFlavorService;
import com.skf.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void addDish(DishDto dishDto) {
        log.info("addDish() called with parameters => 【dishDto = {}】", dishDto);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        baseMapper.insert(dish);
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        // mp id会自动回填
        dishDtoFlavors.forEach(e -> e.setDishId(dish.getId()));
        dishFlavorService.saveBatch(dishDtoFlavors);
        log.info("addDish() end");
    }

    @Override
    @Transactional
    public void updateDish(DishDto dishDto) {
        log.info("updateDish() called with parameters => 【dishDto = {}】", dishDto);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        baseMapper.updateById(dish);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(e -> {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, e.getDishId());
            dishFlavorService.update(e, queryWrapper);
        });

    }

}
