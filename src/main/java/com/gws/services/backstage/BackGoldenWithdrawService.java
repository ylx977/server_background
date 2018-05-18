package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackGoldenCode;
import com.gws.entity.backstage.GoldenWithdraw;
import com.gws.entity.backstage.GoldenWithdrawBO;

import java.util.List;

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

    /**
     * 根据黄金提取单的id号查询该单据下的所有黄金编号
     * @param goldenWithdrawBO
     * @return
     */
    List<BackGoldenCode> queryGoldenCode(GoldenWithdrawBO goldenWithdrawBO);

    /**
     * 针对定时器的方法
     *
     * 查询黄金提取单号中已经超期的单号，也就是查询满足以下条件的单号信息
     * 当前时间大于withdraw_time）的申请单，状态是【待提货】
     * @return
     */
    List<GoldenWithdraw> queryAllOverdues();

    /**
     * 针对定时器的方法
     *
     * 将计算好的费用还有手续费，以及要处理的单号放入GoldenWithdrawBO对象中，去service中处理
     * @param goldenWithdrawBO
     */
    void dealWithOverdues(GoldenWithdrawBO goldenWithdrawBO);
}
