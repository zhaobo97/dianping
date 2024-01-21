package com.zhaobo.spark.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Data
public class ShopCreateForm {
    @NotBlank(message = "服务名不能为空")
    private String name;

    @NotNull(message = "人均价格不能为空")
    private Integer pricePerMan;

    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @NotNull(message = "服务类目不能为空")
    private Integer categoryId;

    private String tags;

    @NotBlank(message = "营业开始时间不能为空")
    private String startTime;

    @NotBlank(message = "营业结束时间不能为空")
    private String endTime;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotNull(message = "商家ID不能为空")
    private Integer sellerId;

    @NotBlank(message = "图标不能为空")
    private String iconUrl;
}