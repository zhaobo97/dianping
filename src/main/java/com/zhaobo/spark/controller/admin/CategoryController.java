package com.zhaobo.spark.controller.admin;

import com.zhaobo.spark.common.AdminPermission;
import com.zhaobo.spark.common.BaseException;
import com.zhaobo.spark.common.EmBusinessError;
import com.zhaobo.spark.form.CategoryCreateFrom;
import com.zhaobo.spark.form.PageQuery;
import com.zhaobo.spark.model.CategoryModel;
import com.zhaobo.spark.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Controller("/admin/category")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //品类列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        PageInfo<CategoryModel> categoryModelPageInfo = new PageInfo<>(categoryModelList);
        ModelAndView modelAndView = new ModelAndView("/admin/category/index.html");
        modelAndView.addObject("data",categoryModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView = new ModelAndView("/admin/category/create.html");
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid CategoryCreateFrom categoryCreateForm, BindingResult bindingResult) throws BaseException {
        if(bindingResult.hasErrors()){
            throw new BaseException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(categoryCreateForm.getName());
        categoryModel.setIconUrl(categoryCreateForm.getIconUrl());
        categoryModel.setSort(categoryCreateForm.getSort());

        categoryService.create(categoryModel);

        return "redirect:/admin/category/index";


    }
}
