package com.zhaobo.spark.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Data
public class LoginForm {

    @NotBlank(message = "手机号不能为空")
    private String telephone;

    @NotBlank(message = "密码不能为空")
    private String password;
}
