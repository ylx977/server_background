package com.gws.controllers.usdgTransfer;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.BJAddress;
import com.gws.common.constants.backstage.CoinType2;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.createRawTransaction.CreateTXUtils;
import com.gws.entity.backstage.createRawTransaction.RawTXResp;
import com.gws.entity.backstage.createRawTransaction.SendTXResp;
import com.gws.entity.backstage.transfer.TransferBO;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/8.
 */
@RequestMapping("/api/transfer")
@RestController
@PropertySource(value = {"classpath:application.properties"},encoding="utf-8")
public class UsdgTransferController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(UsdgTransferController.class);

    /**
     * 提供给王斌的打币接口
     {
        "toAddress"
        "privateKey"
        "publicKey"
        "amount"
     }
     * @return
     */
    @RequestMapping("/usdgTransfer")
    public JsonResult usdgTransfer(@RequestBody TransferBO transferBO){
        LOGGER.info("王斌调取转币接口");
        try {
            ValidationUtil.checkBlankAndAssignString(transferBO.getToAddress());
            ValidationUtil.checkBlankAndAssignString(transferBO.getPrivateKey());
            ValidationUtil.checkBlankAndAssignString(transferBO.getPublicKey());
            ValidationUtil.checkMinAndAssignDouble(transferBO.getAmount(),0d);
        }catch (Exception e){
            LOGGER.error("王斌调取转币接口校验失败",e.getMessage());
            return valiError(e);
        }
        try {
            long fee = (long)(0.001d*100000000);
            //发送4.1.1 构造交易CreateRawTransaction的接口
            String createRawTXResult = CreateTXUtils.createRawTransaction(transferBO.getToAddress(), (long)(transferBO.getAmount()*100000000), fee, "welcome to use", true, CoinType2.USDG);
            //解析返回的结果
            RawTXResp rawTXResp = JSON.parseObject(createRawTXResult, RawTXResp.class);
            if(StringUtils.isEmpty(rawTXResp.getResult())){
                LOGGER.error("(王斌调取转币接口)调取滨江第1个接口出错，错误返回信息为：{}",rawTXResp.getError());
                //如果result为空，直接将error中的错误信息返回给前端
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+rawTXResp.getError());
            }

            String unsigntx = rawTXResp.getResult();
            String sign = CreateTXUtils.getSign(unsigntx, transferBO.getPrivateKey());
            //发送4.1.2 发送交易sendRawTransaction的接口
            String sendRawTXResult = CreateTXUtils.SendRawTransaction(unsigntx,sign,transferBO.getPublicKey(),1);
            //解析返回的结果
            SendTXResp sendTXResp = JSON.parseObject(sendRawTXResult, SendTXResp.class);
            if(StringUtils.isEmpty(sendTXResp.getResult())) {
                LOGGER.error("(王斌调取转币接口)调取滨江第2个接口出错，错误返回信息为：{}",sendTXResp.getError());
                //如果result为空，直接将error中的错误信息返回给前端
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+sendTXResp.getError());
            }
            //将滨江返回的hash返还给王斌
            return success(sendTXResp.getResult());
        }catch (Exception e){
            LOGGER.error("王斌调取转币接口内部发生错误",e.getMessage());
            return sysError(e);
        }
    }

}
