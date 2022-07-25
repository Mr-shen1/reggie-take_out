package com.skf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skf.reggie.common.exception.MyException;
import com.skf.reggie.entity.Category;
import com.skf.reggie.entity.Dish;
import com.skf.reggie.entity.Setmeal;
import com.skf.reggie.mapper.CategoryMapper;
import com.skf.reggie.service.CategoryService;
import com.skf.reggie.service.DishService;
import com.skf.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public boolean remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        int dishes = dishService.count(dishQueryWrapper);
        if (dishes > 0) {
            throw new MyException("该分类还与菜品关联");
        }

        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmeals = setmealService.count(setmealQueryWrapper);
        if (setmeals > 0) {
            throw new MyException("该分类还与套餐关联");
        }
        int count = baseMapper.deleteById(id);
        return count == 1;
    }
}
