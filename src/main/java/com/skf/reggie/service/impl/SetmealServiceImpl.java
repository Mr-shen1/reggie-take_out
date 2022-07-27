package com.skf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skf.reggie.dto.SetmealDto;
import com.skf.reggie.entity.Setmeal;
import com.skf.reggie.entity.SetmealDish;
import com.skf.reggie.mapper.SetmealMapper;
import com.skf.reggie.service.SetmealDishService;
import com.skf.reggie.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void addSetmeal(SetmealDto setmealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        baseMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(e -> e.setSetmealId(setmealId));
        setmealDishService.saveBatch(setmealDishes);
    }
}
