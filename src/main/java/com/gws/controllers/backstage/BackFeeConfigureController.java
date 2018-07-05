package com.gws.controllers.backstage;

import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.CoinFee;
import com.gws.entity.backstage.FeeBO;
import com.gws.services.backstage.BackFeeService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/27.
 */
@RestController
@RequestMapping("/api/backstage/feeConfigure")
public class BackFeeConfigureController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackFeeConfigureController.class);

    @Autowired
    private BackFeeService backFeeService;


    //=======================================以下是交易费的子模块==================================================

    /**
     * 显示交易的手续费（买和卖的都有）
     * @return
     */
    @RequestMapping("/trade/showBTYUSDGTradeFee")
    public JsonResult showBTYUSDGTradeFee(){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查看后台的BTYUSDGT交易手续费",uid);
        try {
            CoinFee coinFee = backFeeService.showBTYUSDGTradeFee();
            return success(coinFee);
        }catch (Exception e){
            return sysError(e);
        }
    }

    /**
     * 设置bty/usdg交易的手续费（买和卖的都有）
      {
        buyTradeFee:"",
        sellTradeFee:""
      }
     *
     * @return
     */
    @RequestMapping("/trade/setBTYUSDGTradeFee")
    public JsonResult setBTYUSDGTradeFee(@RequestBody FeeBO feeBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},设置后台的BTYUSDGT交易手续费",uid);
        try {
            ValidationUtil.checkMinAndAssignDouble(feeBO.getBuyTradeFee(),0d);
            ValidationUtil.checkMinAndAssignDouble(feeBO.getSellTradeFee(),0d);
        }catch (Exception e){
            return valiError(e);
        }
        try {
            backFeeService.setBTYUSDGTradeFee(feeBO);
            return success(null);
        }catch (Exception e){
            return sysError(e);
        }
    }

    //=======================================以下是提币手续费的子模块==================================================

    /**
     * 查询各个币种的提币手续费
     * @return
     */
    @RequestMapping("/draw/showDrawFee")
    public JsonResult showDrawFee(){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询各个币种的提币手续费",uid);
        try {
            Map<String,Object> map = backFeeService.showDrawFee();
            return success(map);
        }catch (Exception e){
            return sysError(e);
        }
    }

    /**
     * 设置各个币的提币手续费
     {
        "coinType":string类型的BTY 和 USDG
        "draw":"提币手续费"
     }
     * @return
     */
    @RequestMapping("/draw/setDrawFee")
    public JsonResult setDrawFee(@RequestBody FeeBO feeBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},设置各个币种的提币手续费",uid);
        try {
            ValidationUtil.checkBlankAndAssignString(feeBO.getCoinType(),"BTY","USDG");
            ValidationUtil.checkMinAndAssignDouble(feeBO.getDraw(),0d);
        }catch (Exception e){
            return valiError(e);
        }
        try {
            backFeeService.setDrawFee(feeBO);
            return success(null);
        }catch (Exception e){
            return sysError(e);
        }
    }


    //=======================================以下是黄金提取人工费的子模块==================================================

    /**
     * 显示黄金提取费的提取费率
     * 目前是100g的手续费 一个是1000g的手续费
     * @param feeBO
     * @return
     */
    @RequestMapping("/goldDraw/showGoldDrawFee")
    public JsonResult showGoldDrawFee(@RequestBody FeeBO feeBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询黄金提取的手续费",uid);
        try {
            Map<String,Object> map = backFeeService.showGoldDrawFee(feeBO);
            return success(map);
        }catch (Exception e){
            return sysError(e);
        }
    }

    /**
     * 设置黄金提取费的提取费率
     {
        "type":"1,2" 1表示100g 2表示1000g
        "fee":""
     }
     * 目前是100g的手续费 一个是1000g的手续费
     * @param feeBO
     * @return
     */
    @RequestMapping("/goldDraw/setGoldDrawFee")
    public JsonResult setGoldDrawFee(@RequestBody FeeBO feeBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},设置黄金提取的手续费",uid);
        try {
            ValidationUtil.checkRangeAndAssignInt(feeBO.getType(),1,2);
            ValidationUtil.checkMinAndAssignDouble(feeBO.getFee(),0d);
        }catch (Exception e){
            return valiError(e);
        }
        try {
            backFeeService.setGoldDrawFee(feeBO);
            return success(null);
        }catch (Exception e){
            return sysError(e);
        }
    }

}
