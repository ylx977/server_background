package com.gws.services.backstage;

import com.gws.entity.backstage.MarketPriceBO;

/**
 * Created by fuzamei on 2018/4/21.
 */
public interface BackMarketPriceService {
    /**
     * 更新人民币对新元价格，还有点差
     * @param marketPriceBO
     */
    void updatePrice(MarketPriceBO marketPriceBO);
}
