package com.zhaobo.spark.form;


/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public class PageQuery {

    private Integer page = 1;

    private Integer size = 20;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
