package com.zhaobo.spark.model;

import lombok.Data;

import javax.persistence.Table;


/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Data
@Table(name = "recommend")
public class RecommendDO {
    private Integer id;

    private String recommend;

}