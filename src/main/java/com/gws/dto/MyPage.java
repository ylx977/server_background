package com.gws.dto;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 【功能描述】
 *
 * @author wangdong  28/07/2017.
 */
@Data
public class MyPage<T> {

    private Long total;

    private List<T> list;

    public MyPage(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public MyPage() {
        this.list = Collections.EMPTY_LIST;
        this.total = 0L;
    }
}
