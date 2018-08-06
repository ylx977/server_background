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

//    @Value("${goldenwithdraw.servicecharge}")
//    private String servicecharge;
    /**
     * 处理超期限黄金提取的处理
     */
    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void overdueWithdraw(){
        /*
            首先王斌在发起这个黄金提取单的时候只是在数据库冻结了用户的usdg的数量而已(包含了手续费)，没有调用任何的区块链接口
            但是后台复审同意后，才调用黄学忠的区块链接口

            1.查询除所有超期（当前时间大于withdraw_time）的申请单，状态是【待提货】
            2.将一半的手续费交给平台账户
            3.将一半的手续费 + 黄金对应的usdg返还给用户账户
            4.将该提取单的状态改成【提取失败】

            以上4步的本质就是扣除用户一半手续费给平台账户

            5.最后调用黄学忠那边的区块链就是要将这一半的手续费打给平台的地址
         */

        //根据条件获取所有超期的订单
        List<GoldenWithdraw> overdues;
        try {
            overdues = backGoldenWithdrawService.queryAllOverdues();
        } catch (Exception e) {
            LOGGER.error("定时器获取超期订单失败："+e.getMessage());
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
            //手续费,每份黄金需要支付的usdg数量，而且100g一份的黄金手续费要大于1000g一份的黄金手续费
            double fee = goldenWithdraw.getFee();

            //支付的usdg费用
            Long payUSDG;
            if(GoldenUnit.HUNDRED.equals(unit)){
                //换算成总共需要支付的usdg，100usdg = 1g 黄金
                payUSDG = 1L*withdrawAmount*100*100;
            }else if(GoldenUnit.THOUSAND.equals(unit)){
                //换算成总共需要支付的usdg，100usdg = 1g 黄金
                payUSDG = 1L*withdrawAmount*1000*100;
            }else{
                LOGGER.error("定时器获取黄金单位出错");
                return;
            }
            //换算成总的人工费用(份数*fee)，或者叫手续费（超期的订单扣除用户50%的手续费）
            Double chargeUSDG = DecimalUtil.multiply(withdrawAmount,fee,8,RoundingMode.HALF_EVEN);
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
        System.out.println("老子我2小时执行一次，哈哈");
    }


    /**
     * 处理后台一直没有处理的黄金单子，将用户冻结的资金返还给可用部分
     */
    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void overdueUnhandle(){
        /*
            1.查询除所有超期（当前时间大于withdraw_time）的申请单，状态是【申请中】
            2.将用户冻结的那部分钱返还给可用部分即可
            3.将提取单的状态改成提取失败(因为这是后台一直没有处理的单子，所以不扣除任何费用)
         */
        //根据条件获取所有超期但是后台一直未处理的订单
        List<GoldenWithdraw> overdues;
        try {
            overdues = backGoldenWithdrawService.queryAllOverduesUnhandled();
        } catch (Exception e) {
            LOGGER.error("定时器获取超期【后台未处理】订单失败："+e.getMessage());
            return;
        }

        //遍历处理每条超期【后台一直未处理】的数据
        for (GoldenWithdraw goldenWithdraw : overdues) {
            Long id = goldenWithdraw.getId();
            Long uid = goldenWithdraw.getUid();
            //提取的黄金份数
            Integer withdrawAmount = goldenWithdraw.getWithdrawAmount();
            //黄金单位1表示100g 2表示1000g
            Integer unit = goldenWithdraw.getWithdrawUnit();
            //手续费,每份黄金需要支付的usdg数量，而且100g一份的黄金手续费要大于1000g一份的黄金手续费
            double fee = goldenWithdraw.getFee();

            //支付的usdg费用
            Long payUSDG;
            if(GoldenUnit.HUNDRED.equals(unit)){
                //换算成总共需要支付的usdg，100usdg = 1g 黄金
                payUSDG = 1L*withdrawAmount*100*100;
            }else if(GoldenUnit.THOUSAND.equals(unit)){
                //换算成总共需要支付的usdg，100usdg = 1g 黄金
                payUSDG = 1L*withdrawAmount*1000*100;
            }else{
                LOGGER.error("定时器获取黄金单位出错");
                return;
            }
            //换算成用户总的被冻结资金 = 总的人工费用(份数*fee)+真正要支付的usdg
            Double totalUSDG = DecimalUtil.add(payUSDG,DecimalUtil.multiply(withdrawAmount,fee,8,RoundingMode.HALF_EVEN),8,RoundingMode.HALF_EVEN);
            try {
                GoldenWithdrawBO goldenWithdrawBO = new GoldenWithdrawBO();
                goldenWithdrawBO.setId(id);
                goldenWithdrawBO.setUid(uid);
                goldenWithdrawBO.setTotalUSDG(totalUSDG);
                goldenWithdrawBO.setPayUSDG(payUSDG);
                backGoldenWithdrawService.dealWithOverduesUnhandled(goldenWithdrawBO);
            } catch (Exception e) {
                LOGGER.error("定时器处理单个单据信息"+"(id="+id+",uid="+uid+")"+"出错:"+e.getMessage());
            }
        }
        System.out.println("老子我2小时执行一次，哈哈");

    }

}
