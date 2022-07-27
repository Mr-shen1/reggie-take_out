package com.skf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skf.reggie.entity.DishFlavor;
import com.skf.reggie.mapper.DishFlavorMapper;
import com.skf.reggie.service.DishFlavorService;
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
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
