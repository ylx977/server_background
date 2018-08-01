package com.gws.mapper;

import com.gws.entity.backstage.BtyUsdgTradeOrder;
import com.gws.entity.backstage.FrontUserBO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/7/26.
 */
@Mapper
public interface BackAssetManagementMapper {


    List<BtyUsdgTradeOrder> queryExchange(FrontUserBO frontUserBO);

    long queryExchangeCount(FrontUserBO frontUserBO);
}
