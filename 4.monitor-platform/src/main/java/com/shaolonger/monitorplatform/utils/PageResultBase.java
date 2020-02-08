package com.shaolonger.monitorplatform.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResultBase<T> {
    // 总记录数
    private long totalNum;
    // 总页数
    private long totalPage;
    // 当前页数
    private long pageNum;
    // 每页数目
    private long pageSize;
}
