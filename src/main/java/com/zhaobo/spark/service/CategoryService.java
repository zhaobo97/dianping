package com.zhaobo.spark.service;

import com.zhaobo.spark.model.CategoryModel;

import java.util.List;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public interface CategoryService {

    /**
     * 创建品类
     * @param categoryModel
     * @return
     */
    CategoryModel create (CategoryModel categoryModel);

    /**
     * 查询品类
     * @param id
     * @return
     */
    CategoryModel get(Integer id);

    /**
     * 品类列表
     * @return
     */
    List<CategoryModel> selectAll ();

    Integer countAllCategory();
}
