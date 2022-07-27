package com.skf.reggie.controller.setmeal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skf.reggie.common.R;
import com.skf.reggie.dto.SetmealDto;
import com.skf.reggie.entity.Category;
import com.skf.reggie.entity.Setmeal;
import com.skf.reggie.service.CategoryService;
import com.skf.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/27
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R addSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.addSetmeal(setmealDto);
        return R.success("");
    }

    @GetMapping("page")
    public R<Page<SetmealDto>> pageSetmeal(@RequestParam("page") Integer page,
                         @RequestParam("pageSize") Integer pageSize,
                         @RequestParam(value = "name", required = false) String name) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            setmealLambdaQueryWrapper.like(Setmeal::getName, name);
        }
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        setmealService.page(setmealPage, setmealLambdaQueryWrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> setmealList = setmealPage.getRecords();
        Set<Long> categoryIds = setmealList.stream().map(Setmeal::getCategoryId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(categoryIds)) {
            List<Category> categories = categoryService.listByIds(categoryIds);
            List<SetmealDto> setmealDtoList = setmealList.stream().map(e -> {
                SetmealDto setmealDto = new SetmealDto();
                BeanUtils.copyProperties(e, setmealDto);
                return setmealDto;
            }).collect(Collectors.toList());
            for (SetmealDto setmealDto : setmealDtoList) {
                for (Category category : categories) {
                    if (setmealDto.getCategoryId().equals(category.getId())) {
                        setmealDto.setCategoryName(category.getName());
                    }
                }
            }
            setmealDtoPage.setRecords(setmealDtoList);
            return R.success(setmealDtoPage);
        } else {
            return R.error("数据为空");
        }
    }

    @PostMapping("/status/{status}")
    public R updateDish(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids) && ids.size() == 1) {
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Setmeal::getId, ids.get(0));
            updateWrapper.set(Setmeal::getStatus, status);
            setmealService.update(null, updateWrapper);
        } else {
            ids.forEach(e -> {
                LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Setmeal::getId, e);
                updateWrapper.set(Setmeal::getStatus, status);
                setmealService.update(null, updateWrapper);
            });
        }
        return R.success("");
    }

    @DeleteMapping()
    public R deleteDish(@RequestParam("ids") List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids) && ids.size() == 1) {
            setmealService.removeById(ids.get(0));
        } else {
            ids.forEach(e -> setmealService.removeByIds(ids));
        }
        return R.success("");
    }
}
