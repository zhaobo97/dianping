package com.zhaobo.spark;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}