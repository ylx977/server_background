package com.gws.repositories.slave.backstage;

import com.gws.entity.backstage.MarketPrice;
import com.gws.entity.backstage.MarketPriceHistory;
import com.gws.utils.query.core.GwsBaseRepository;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/29.
 */
public interface BackMarketPriceHistorySlave extends GwsBaseRepository<MarketPriceHistory,Long> {
}
