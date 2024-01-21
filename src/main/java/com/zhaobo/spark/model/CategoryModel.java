package com.zhaobo.spark.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Data
@Table(name = "category")
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date createdAt;

    private Date updatedAt;

    private String name;

    private String iconUrl;

    private Integer sort;
}