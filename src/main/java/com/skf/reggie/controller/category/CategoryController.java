package com.skf.reggie.controller.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skf.reggie.common.R;
import com.skf.reggie.entity.Category;
import com.skf.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/25
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        log.info("CategoryController.addCategory --> {}", category);
        boolean result = categoryService.save(category);
        if (result) {
            return R.success("新增分类成功");
        } else {
            return R.error("新增分类失败");
        }
    }

    @GetMapping("page")
    public R<Page<Category>> queryCategory(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        log.info("page: {}, pageSize: {}", page, pageSize);
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping()
    public R<String> deleteCategory(@RequestParam("ids") Long id) {
        log.info("CategoryController.deleteCategory, id: {}", id);
        boolean result = categoryService.remove(id);
        if (result) {
            return R.success("");
        } else {
            return R.error("删除失败");
        }
    }


    @PutMapping
    public R<String> editCategory(@RequestBody Category category) {
        log.info("CategoryController.editCategory --> {}", category);
        boolean result = categoryService.updateById(category);
        if (result) {
            return R.success("修改分类成功");
        } else {
            return R.error("修改分类失败");
        }
    }

    @GetMapping("/list")
    public R<List<Category>> getType(@RequestParam("type") Integer type) {
        log.info("getType() called with parameters => 【type = {}】", type);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType, type);
        List<Category> categories = categoryService.list(queryWrapper);
        return R.success(categories);
    }
}
