package com.skf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skf.reggie.entity.Category;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
public interface CategoryService extends IService<Category> {
    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    boolean remove(Long id);
}
