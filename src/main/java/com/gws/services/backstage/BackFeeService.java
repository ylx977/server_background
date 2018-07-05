package com.gws.services.backstage;

import com.gws.entity.backstage.CoinFee;
import com.gws.entity.backstage.FeeBO;

import java.util.Map;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/27.
 */
public interface BackFeeService {
    /**
     * 显示交易的手续费(其实还包含提币的手续费)
     * @return
     */
    CoinFee showBTYUSDGTradeFee();

    /**
     * 设置交易的手续费
     * @return
     */
    void setBTYUSDGTradeFee(FeeBO feeBO);

    /**
     * 返回不同币的提币费
     * @return
     */
    Map<String,Object> showDrawFee();

    /**
     * 设置不同币的
     * @param feeBO
     */
    void setDrawFee(FeeBO feeBO);

    /**
     * 显示黄金的手续费
     * @param feeBO
     * @return
     */
    Map<String,Object> showGoldDrawFee(FeeBO feeBO);

    /**
     * 设置黄金的手续费
     * @param feeBO
     */
    void setGoldDrawFee(FeeBO feeBO);
}
