package com.skf.reggie.controller.dish;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skf.reggie.common.R;
import com.skf.reggie.dto.DishDto;
import com.skf.reggie.entity.Category;
import com.skf.reggie.entity.Dish;
import com.skf.reggie.entity.DishFlavor;
import com.skf.reggie.service.CategoryService;
import com.skf.reggie.service.DishFlavorService;
import com.skf.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/26
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public R addDish(@RequestBody DishDto dishDto) {
        log.info("addDish() called with parameters => 【dishDto = {}】", dishDto);
        dishService.addDish(dishDto);
        return R.success("");
    }


    @GetMapping("page")
    public R<Page<DishDto>> queryCategory(@RequestParam(value = "name", required = false) String name,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("pageSize") Integer pageSize) {
        log.info("queryCategory() called with parameters => 【page = {}】, 【pageSize = {}】", page, pageSize);
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapperDish = new LambdaQueryWrapper<>();
        queryWrapperDish.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapperDish.orderByAsc(Dish::getSort);
        dishService.page(dishPage, queryWrapperDish);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> records = dishPage.getRecords();
        List<Long> categoryIds = records.stream().map(Dish::getCategoryId).collect(Collectors.toList());

        List<Category> categories = categoryService.listByIds(categoryIds);
        List<DishDto> dishDtos = records.stream().map(e -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(e, dishDto);
            return dishDto;
        }).collect(Collectors.toList());

        for (DishDto dishDto : dishDtos) {
            for (Category category : categories) {
                if (dishDto.getCategoryId().equals(category.getId())) {
                    dishDto.setCategoryName(category.getName());
                }
            }
        }
        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }


    @PutMapping()
    public R editDish(@RequestBody DishDto dishDto) {
        log.info("editDish() called with parameters => 【dishDto = {}】", dishDto);
        dishService.updateDish(dishDto);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable("id") Long id) {
        log.info("getDish() called with parameters => 【id = {}】", id);
        Dish dish = dishService.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavors);
        return R.success(dishDto);
    }

    @PostMapping("/status/{status}")
    public R updateDish(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids) && ids.size() == 1) {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId, ids.get(0));
            updateWrapper.set(Dish::getStatus, status);
            dishService.update(null, updateWrapper);
        } else {
          ids.forEach(e -> {
              LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
              updateWrapper.eq(Dish::getId, e);
              updateWrapper.set(Dish::getStatus, status);
              dishService.update(null, updateWrapper);
          });
        }
        return R.success("");
    }

    @DeleteMapping()
    public R deleteDish(@RequestParam("ids") List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids) && ids.size() == 1) {
            dishService.removeById(ids.get(0));
        } else {
            ids.forEach(e -> dishService.removeByIds(ids));
        }
        return R.success("");
    }
}
