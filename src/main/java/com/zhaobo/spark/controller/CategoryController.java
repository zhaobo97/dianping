package com.zhaobo.spark.controller;


import com.zhaobo.spark.common.ResponseVo;
import com.zhaobo.spark.model.CategoryModel;
import com.zhaobo.spark.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Controller("/category")
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping("/list")
    public ResponseVo<List<CategoryModel>> list(){
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        return ResponseVo.success(categoryModelList);

    }

}
