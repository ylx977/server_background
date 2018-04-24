package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.GoldenWithdrawBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
public interface BackGoldenWithdrawService {

    /**
     * 查询黄金提取记录信息
     * @param goldenWithdrawBO
     * @return
     */
    PageDTO queryGoldenWithdrawInfo(GoldenWithdrawBO goldenWithdrawBO);

    /**
     * 插入黄金编号
     * @param goldenWithdrawBO
     */
    void insertGoldenCode(GoldenWithdrawBO goldenWithdrawBO);

    /**
     * 确认提货操作
     * @param goldenWithdrawBO
     */
    void confirmCheckout(GoldenWithdrawBO goldenWithdrawBO);
}
