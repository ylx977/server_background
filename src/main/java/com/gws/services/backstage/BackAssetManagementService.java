package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.AssetBO;
import com.gws.entity.backstage.AssetBalanceVO;
import com.gws.entity.backstage.FrontUserBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
public interface BackAssetManagementService {

    /**
     * 查询每个uid的详细资产信息
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUserAccount(FrontUserBO frontUserBO);

    /**
     * 查询前台用户的充币记录
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUserRecharge(FrontUserBO frontUserBO);

    /**
     * 增加平台usdg总量
     * @param assetBO
     */
    void addUsdg(AssetBO assetBO);

    /**
     * 查询前台用户提币的所有非完成的审核信息(包括初审通过和待审核的信息)
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUserWithdraw(FrontUserBO frontUserBO);

    /**
     * 查询前台用户提币的所有已经完成的审核信息(包括复审通过和拒绝的信息)
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUserWithdrawHistory(FrontUserBO frontUserBO);

    /**
     * 点击初审通过的按钮操作
     * @param frontUserBO
     */
    void firstPass(FrontUserBO frontUserBO);

    /**
     * 点击复审通过的按钮操作
     * @param frontUserBO
     */
    void secondPass(FrontUserBO frontUserBO);

    /**
     * 点击拒绝的按钮操作
     * @param frontUserBO
     */
    void rejectWithdraw(FrontUserBO frontUserBO);

    /**
     * 查询兑换信息
     * @param frontUserBO
     * @return
     */
    PageDTO queryExchange(FrontUserBO frontUserBO);

    /**
     * 查询平台的资产余额，包括bty和usdg
     * @return
     */
    AssetBalanceVO queryAssetBalance();
}
