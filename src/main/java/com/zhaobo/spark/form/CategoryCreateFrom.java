package com.zhaobo.spark.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Data
public class CategoryCreateFrom {
    @NotBlank(message = "名字不能为空")
    private String name;

    @NotBlank(message = "iconUrl不能为空")
    private String iconUrl;

    @NotNull(message = "权重不能为空")
    private Integer sort;
}
