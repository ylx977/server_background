package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.MarketPrice;
import com.gws.entity.backstage.MarketPriceBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackMarketPriceService;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/17.
 */
@RestController
@RequestMapping("/api/backstage/marketPrice")
public class BackMarketPriceController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackMarketPriceController.class);

    private final BackMarketPriceService backMarketPriceService;

    private final HttpServletRequest request;

    @Autowired
    public BackMarketPriceController(BackMarketPriceService backMarketPriceService, HttpServletRequest request) {
        this.backMarketPriceService = backMarketPriceService;
        this.request = request;
    }

    /**
     * 修改平台CNY/SGD汇率价格和点差值
     * {
     *     "buyCnySgd"
     *     "sellCnySgd"
     *     "buySgdUsd"
     *     "sellSgdUsd"
     *     "buySpread"
     *     "sellSpread"
     *     "buyBtyUsdg"
     *     "sellBtyUsdg"
     * }
     * @return
     */
    @RequestMapping("/updatePrice")
    public JsonResult updatePrice(@RequestBody MarketPriceBO marketPriceBO){
        /*
         * 1：前台传入CNY/SGD汇率价格和点差值等参数
         * 2：基本参数校验
         * 3：修改平台汇率价格和点差值
         */
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},更新平台人民币对新元价格",uid);
        try {
            double buyCnySgd = ValidationUtil.checkMinAndAssignDouble(marketPriceBO.getBuyCnySgd(),0d);
            double sellCnySgd = ValidationUtil.checkMinAndAssignDouble(marketPriceBO.getSellCnySgd(),0d);
            if(sellCnySgd < buyCnySgd){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CNY_SGD_ASK_BID_ERROR));
            }
            double buySgdUsd = ValidationUtil.checkMinAndAssignDouble(marketPriceBO.getBuySgdUsd(),0d);
            double sellSgdUsd = ValidationUtil.checkMinAndAssignDouble(marketPriceBO.getSellSgdUsd(),0d);
            if(sellSgdUsd < buySgdUsd){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.SGD_USD_ASK_BID_ERROR));
            }
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getSellSpread());
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getBuySpread());
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getBuyBtyUsdg());
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getSellBtyUsdg());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backMarketPriceService.updatePrice(marketPriceBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 查询平台CNY/SGD汇率价格，SGD/USD汇率价格和点差值
     * @return
     */
    @RequestMapping("/queryPrice")
    public JsonResult queryPrice(@RequestBody MarketPriceBO marketPriceBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询平台CNY/SGD汇率价格，SGD/USD汇率价格和点差值",uid);
        try {
            MarketPrice marketPrice = backMarketPriceService.queryPrice(marketPriceBO);
            return success(marketPrice);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 查看平台报价的历史记录信息
     * {
     *     "page"
     *     "rowNum"
     * }
     * @param marketPriceBO
     * @return
     */
    @RequestMapping("/queryPriceHistory")
    public JsonResult queryPriceHistory(@RequestBody MarketPriceBO marketPriceBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查看平台历史报价信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(marketPriceBO.getPage(),1);
            ValidationUtil.checkMinAndAssignInt(marketPriceBO.getRowNum(),1);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO pageDTO = backMarketPriceService.queryPriceHistory(marketPriceBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

}
