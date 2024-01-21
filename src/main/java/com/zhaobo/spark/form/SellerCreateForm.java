package com.zhaobo.spark.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Data
public class SellerCreateForm {

    @NotBlank(message = "商户名不能为空")
    private String name;

}
