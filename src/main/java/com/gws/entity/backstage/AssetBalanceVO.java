package com.gws.entity.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/28.
 */
@Data
public class AssetBalanceVO {

    /**
     * 黄金库存量（随用户的提取会逐渐减少）
     */
    private Double goldBanance;

    /**
     * 市场流通的usdg(在用户手上的usdg量)
     */
    private Double marketUsdgBalance;

    /**
     * 平台USDG库存总量(等于平台账户的usdg+市场流通usdg，这个是固定不变的，随着黄金库存量的增加而增加，这个值也等于平台历史黄金总量)
     */
    private Double totalUsdgAmount;

    /**
     * 平台账户的usdg数量
     */
    private Double usdgBalance;

    /**
     * 平台账户的bty数量
     */
    private Double btyBalance;

    /**
     * 平台的bty地址
     */
    private String platformBtyAddress;

}
