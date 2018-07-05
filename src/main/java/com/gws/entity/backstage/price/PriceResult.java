package com.gws.entity.backstage.price;

import lombok.Data;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/5.
 */
@Data
public class PriceResult {

    private Integer code;
    private String message;
    private List<Trade> data;

    @Data
    public static class Trade{
        private Double buy;
        private Double sell;
        private String symbol;
    }
}
