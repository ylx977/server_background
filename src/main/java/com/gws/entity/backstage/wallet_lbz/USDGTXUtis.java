package com.gws.entity.backstage.wallet_lbz;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.configuration.backstage.LangConfig;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.LangReadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/21.
 */
@Component
public class USDGTXUtis {

    private static final String walleturl = ConfReadUtil.getProperty("blockchain.walleturl");

    private static final Logger LOGGER = LoggerFactory.getLogger(USDGTXUtis.class);

    public static final boolean verify(String jsonResult){
        TXResp txResp = JSON.parseObject(jsonResult, TXResp.class);
        if(!StringUtils.isEmpty(txResp.getError()) || StringUtils.isEmpty(txResp.getResult())){
            return false;
        }
        return true;
    }

    /**
     * 返回hash值
     * @param to
     * @param amount
     * @param tokenSymbol
     * @return
     */
    public static final String withdrawCoin(String to,double amount,String tokenSymbol){
        SendRawTX sendRawTX = SendRawTX.getInstance(to, amount, tokenSymbol);
        String jsonSendRawTX = JSON.toJSONString(sendRawTX);
        LOGGER.info(jsonSendRawTX);
        String jsonResult = HttpRequest.sendPost4LBZ(walleturl, jsonSendRawTX);
        LOGGER.info(jsonResult);

        TXResp txResp = JSON.parseObject(jsonResult, TXResp.class);
        if(!StringUtils.isEmpty(txResp.getError()) || StringUtils.isEmpty(txResp.getResult())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.FAIL_WITHDRAW_COIN)+txResp.getError());
        }

        return txResp.getResult();
    }

    /**
     * 返回hash值
     * @param txid
     * @return
     */
    public static final String reWithdrawCoin(String txid){
        ReSendRawTx reSendRawTx = ReSendRawTx.getInstance(txid);
        String jsonReSendRawTX = JSON.toJSONString(reSendRawTx);
        LOGGER.info(jsonReSendRawTX);
        String jsonResult = HttpRequest.sendPost4LBZ(walleturl, jsonReSendRawTX);
        LOGGER.info(jsonResult);

        TXResp txResp = JSON.parseObject(jsonResult, TXResp.class);
        if(!StringUtils.isEmpty(txResp.getError()) || StringUtils.isEmpty(txResp.getResult())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.FAIL_REWITHDRAW_COIN)+txResp.getError());
        }

        return txResp.getResult();
    }

    /**
     * 根据uid生成地址
     * @param uid
     * @return
     */
    public static final String createAddress(String uid){
        CreateAddress createAddress = CreateAddress.getInstance(uid);
        String jsonCreateAddress = JSON.toJSONString(createAddress);
        LOGGER.info(jsonCreateAddress);
        String jsonResult = HttpRequest.sendPost4LBZ(walleturl, jsonCreateAddress);
        LOGGER.info(jsonResult);

        TXResp txResp = JSON.parseObject(jsonResult, TXResp.class);
        if(!StringUtils.isEmpty(txResp.getError()) || StringUtils.isEmpty(txResp.getResult())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.FAIL_CREATE_ADDRESS)+txResp.getError());
        }
        return txResp.getResult();
    }

    public static void main(String[] args) {
        LangConfig.setLang(1);
//        String address = createAddress("1000000000");
//        System.out.println(address);
        String bty = withdrawCoin("1FAr69dcXyxrd4DYPWQX1djUBucBi4SV9U", 100000000d, "BTY");
        System.out.println(bty);
//        String s = reWithdrawCoin("0xb4fb9d02b43314adf1d03c4a6f129d14347ffbf6212a5aa31ca7792105631c52");
//        System.out.println(s);
    }

}
