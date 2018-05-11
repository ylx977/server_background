package com.gws.controllers.backstage;

import com.gws.controllers.BaseApiController;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.MarketPriceBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackMarketPriceService;
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
     *     "cnysgd"
     *     "buySpread"
     *     "sellSpread"
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
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},更新平台人民币对新元价格",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        marketPriceBO.setLang(lang);
        try {
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getCnysgd(),lang);
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getBuySpread(),lang);
            ValidationUtil.checkAndAssignDouble(marketPriceBO.getSellSpread(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            backMarketPriceService.updatePrice(marketPriceBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

}
