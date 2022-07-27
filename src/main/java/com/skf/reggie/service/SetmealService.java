package com.skf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skf.reggie.dto.SetmealDto;
import com.skf.reggie.entity.Setmeal;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
public interface SetmealService extends IService<Setmeal> {

    void addSetmeal(SetmealDto setmealDto);
}
