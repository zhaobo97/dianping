package com.zhaobo.spark.controller.admin;

import com.bin.spark.common.*;
import com.zhaobo.spark.form.PageQuery;
import com.zhaobo.spark.form.ShopCreateForm;
import com.zhaobo.spark.model.ShopModel;
import com.zhaobo.spark.service.ShopService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.zhaobo.spark.common.AdminPermission;
import com.zhaobo.spark.common.BaseException;
import com.zhaobo.spark.common.BusinessException;
import com.zhaobo.spark.common.ResponseEnum;
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
@Controller("/admin/shop")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    //门店列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<ShopModel> shopModelList = shopService.selectAll();
        PageInfo<ShopModel> shopModelPageInfo = new PageInfo<>(shopModelList);
        ModelAndView modelAndView = new ModelAndView("/admin/shop/index.html");
        modelAndView.addObject("data",shopModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView = new ModelAndView("/admin/shop/create.html");
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid ShopCreateForm shopCreateForm, BindingResult bindingResult) throws BaseException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(ResponseEnum.PARAM_ERROR.getCode(),
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        ShopModel shopModel = new ShopModel();
        shopModel.setIconUrl(shopCreateForm.getIconUrl());
        shopModel.setAddress(shopCreateForm.getAddress());
        shopModel.setCategoryId(shopCreateForm.getCategoryId());
        shopModel.setEndTime(shopCreateForm.getEndTime());
        shopModel.setStartTime(shopCreateForm.getStartTime());
        shopModel.setLongitude(shopCreateForm.getLongitude());
        shopModel.setLatitude(shopCreateForm.getLatitude());
        shopModel.setName(shopCreateForm.getName());
        shopModel.setPricePerMan(shopCreateForm.getPricePerMan());
        shopModel.setSellerId(shopCreateForm.getSellerId());

        shopService.create(shopModel);
        return "redirect:/admin/shop/index";


    }

}