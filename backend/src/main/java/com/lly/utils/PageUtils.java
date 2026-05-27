package com.lly.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import java.util.List;

@Data
public class PageUtils {
    private long totalCount;
    private long pageSize;
    private long totalPage;
    private long currPage;
    private List<?> list;

    public PageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = page.getTotal();
        this.pageSize = page.getSize();
        this.currPage = page.getCurrent();
        this.totalPage = page.getPages();
    }
}
