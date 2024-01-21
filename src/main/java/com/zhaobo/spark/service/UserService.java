package com.zhaobo.spark.service;

import com.zhaobo.spark.model.UserModel;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public interface UserService {
    /**
     * 获取用户信息
     * @param id
     * @return
     */
    UserModel getUser (@Param(value = "id") Integer id);

    /**
     * 注册
     * @param user
     * @return
     */
    UserModel register(UserModel user);

    /**
     * 登录
     * @param userModel 登录查询
     * @return
     */
    UserModel login(UserModel userModel);

    /**
     * 查询用户总数
     * @return
     */
    Integer countAllUser();
}
