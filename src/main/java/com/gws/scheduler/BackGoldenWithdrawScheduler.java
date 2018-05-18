package com.gws.scheduler;

import com.gws.common.constants.backstage.GoldenUnit;
import com.gws.entity.backstage.GoldenWithdraw;
import com.gws.entity.backstage.GoldenWithdrawBO;
import com.gws.services.backstage.BackGoldenWithdrawService;
import com.gws.utils.decimal.DecimalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
@Component
@EnableScheduling
@Configurable
public class BackGoldenWithdrawScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackGoldenWithdrawScheduler.class);

    @Autowired
    private BackGoldenWithdrawService backGoldenWithdrawService;

    @Value("${goldenwithdraw.servicecharge}")
    private String servicecharge;
    /**
     * 处理超期限黄金提取的处理
     */
    @Scheduled(cron = "0 0 0-6 * * ? ")
    public void overdueWithdraw(){
        /*
            1.查询除所有超期（当前时间大于withdraw_time）的申请单，状态是【待提货】
            2.
         */

        //根据条件获取所有超期的订单
        List<GoldenWithdraw> overdues = null;
        try {
            overdues = backGoldenWithdrawService.queryAllOverdues();
        } catch (Exception e) {
            LOGGER.error("定时器获取超期订单失败："+e.getMessage());
            return;
        }

        //没有数据就不处理了
        if(overdues == null || overdues.size()==0){
            return;
        }

        //遍历处理每条超期的数据
        for (GoldenWithdraw goldenWithdraw : overdues) {
            Long id = goldenWithdraw.getId();
            Long uid = goldenWithdraw.getUid();
            //提取的黄金份数
            Integer withdrawAmount = goldenWithdraw.getWithdrawAmount();
            //黄金单位1表示100g 2表示1000g
            Integer unit = goldenWithdraw.getWithdrawUnit();
            //手续费的费率，手续费（usdg） = rate * 提取用到总的usdg数量
            double rate = Double.parseDouble(servicecharge);

            //支付的usdg费用
            Integer payUSDG = null;
            if(GoldenUnit.HUNDRED.equals(unit)){
                //换算成总共需要支付的usdg，100usdg = 1g 黄金
                payUSDG = withdrawAmount*100*100;
            }else if(GoldenUnit.THOUSAND.equals(unit)){
                //换算成总共需要支付的usdg，100usdg = 1g 黄金
                payUSDG = withdrawAmount*1000*100;
            }else{
                LOGGER.error("定时器获取黄金单位出错");
                return;
            }
            //换算人工费用，或者叫手续费（超期的订单扣除用户50%的手续费）
            Double chargeUSDG = DecimalUtil.multiply(payUSDG,rate,8,RoundingMode.HALF_EVEN);
            try {
                GoldenWithdrawBO goldenWithdrawBO = new GoldenWithdrawBO();
                goldenWithdrawBO.setId(id);
                goldenWithdrawBO.setUid(uid);
                goldenWithdrawBO.setPayUSDG(payUSDG);
                goldenWithdrawBO.setChargeUSDG(chargeUSDG);
                backGoldenWithdrawService.dealWithOverdues(goldenWithdrawBO);
            } catch (Exception e) {
                LOGGER.error("定时器处理单个单据信息"+"(id="+id+",uid="+uid+")"+"出错:"+e.getMessage());
            }
        }

        System.out.println("老子我6小时执行一次，哈哈");
    }

}
