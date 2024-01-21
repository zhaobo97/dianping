package com.zhaobo.spark.service;

import com.zhaobo.spark.model.SellerModel;

import java.util.List;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public interface SellerService {
    /**
     * 添加商户
     * @param sellerModel
     * @return
     */
    SellerModel create(SellerModel sellerModel);

    /**
     * 查询商户
     * @param id
     * @return
     */
    SellerModel get(Integer id);

    /**
     * 商户列表
     * @return
     */
    List<SellerModel> selectAll();

    /**
     * 生效/失效
     * @param id
     * @param disabledFlag
     * @return
     */
    SellerModel changeStatus(Integer id,Integer disabledFlag);

    Integer countAllSeller();
}
