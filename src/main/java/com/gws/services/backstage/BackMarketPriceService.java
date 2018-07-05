package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.MarketPrice;
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

    /**
     * 查询平台历史报价信息
     * @param marketPriceBO
     * @return
     */
    PageDTO queryPriceHistory(MarketPriceBO marketPriceBO);

    /**
     * 用于回显平台报价信息
     * @param marketPriceBO
     * @return
     */
    MarketPrice queryPrice(MarketPriceBO marketPriceBO);
}
