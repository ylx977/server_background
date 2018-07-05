package com.gws.utils.transfer;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.*;
import com.gws.entity.backstage.createRawTransaction.CreateTXUtils;
import com.gws.entity.backstage.createRawTransaction.RawTXResp;
import com.gws.entity.backstage.createRawTransaction.SendTXResp;
import com.gws.utils.blockchain.BlockUtils;
import com.gws.utils.blockchain.Protobuf4EdsaUtils;
import com.gws.utils.blockchain.ProtobufBean;
import com.gws.utils.decimal.DecimalUtil;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.LangReadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/9.
 */
@Component
public class TransferCoin {

    public static final String bankAddress = ConfReadUtil.getProperty("blockchain.bankAddress");
    public static final String bankPubkey = ConfReadUtil.getProperty("blockchain.bankPubkey");
    public static final String bankPrikey = ConfReadUtil.getProperty("blockchain.bankPrikey");

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferCoin.class);

    private final ExecutorService executorService;

    @Autowired
    public TransferCoin(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 定时器使用的，调用滨江打币
     */
    public static final void transferCoinBJ(String fromPrivateKey,String fromPublicKey,double money,double f,String toAddress,String coinType){

        long amount = (long)(DecimalUtil.multiply(money,100000000,8, RoundingMode.HALF_EVEN));
        long fee = (long)(DecimalUtil.multiply(f,100000000,8, RoundingMode.HALF_EVEN));

        boolean flag = CoinType2.USDG.equals(coinType);

        //发送4.1.1 构造交易CreateRawTransaction的接口
        String createRawTXResult = CreateTXUtils.createRawTransaction(toAddress, amount, fee, "welcome to use", flag, coinType);
        //解析返回的结果
        RawTXResp rawTXResp = JSON.parseObject(createRawTXResult, RawTXResp.class);
        if(StringUtils.isEmpty(rawTXResp.getResult())){
            LOGGER.error("(定时器)调取滨江第1个接口出错，错误返回信息为：{}",rawTXResp.getError());
            //如果result为空，直接将error中的错误信息返回给前端
            throw new RuntimeException();
        }

        String unsigntx = rawTXResp.getResult();
        String sign = CreateTXUtils.getSign(unsigntx, BJAddress.PRIKEY);
        //发送4.1.2 发送交易sendRawTransaction的接口
        String sendRawTXResult = CreateTXUtils.SendRawTransaction(unsigntx,sign,BJAddress.PUBKEY,1);
        //解析返回的结果
        SendTXResp sendTXResp = JSON.parseObject(sendRawTXResult, SendTXResp.class);
        if(StringUtils.isEmpty(sendTXResp.getResult())) {
            LOGGER.error("(定时器)调取滨江第2个接口出错，错误返回信息为：{}",sendTXResp.getError());
            //如果result为空，直接将error中的错误信息返回给前端
            throw new RuntimeException();
        }
    }



    public static final void transferCoin2Account(){

    }

    public static final void transferCoinFromBank(){

    }

    /**
     * 专门供定时器使用的
     * @param fromPubkey
     * @param fromPrikey
     * @param toPubkey
     * @param money
     * @param symbolId
     */
    public static final void transferCoin(String fromPubkey, String fromPrikey, String toPubkey, double money, int symbolId){

        long amount = (long)(DecimalUtil.multiply(money,100000000,8, RoundingMode.HALF_EVEN));

        //====================================================》todo 第1次调用区块链接口，用户打给银行
        ProtobufBean protobufBean = Protobuf4EdsaUtils.requestTransfer(fromPrikey, symbolId, fromPubkey, bankPubkey, amount);
        String jsonResult = BlockUtils.sendPostParam(protobufBean);
        boolean flag = BlockUtils.vilaResult(jsonResult);
        if(!flag){
            String errorMessage = BlockUtils.getErrorMessage(jsonResult);
            LOGGER.error("区块链出错:"+errorMessage);
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage);
        }
        LOGGER.info("第1次调用区块链接口：从用户a转币给银行成功");

        //====================================================》todo 第2次调用区块链接口，银行打给平台账户
        ProtobufBean protobufBean2 = Protobuf4EdsaUtils.requestTransfer(bankPrikey, symbolId, bankPubkey, toPubkey, amount);
        String jsonResult2 = BlockUtils.sendPostParam(protobufBean2);
        boolean flag2 = BlockUtils.vilaResult(jsonResult2);
        if(!flag2){
            String errorMessage = BlockUtils.getErrorMessage(jsonResult2);
            LOGGER.error("第2次调用区块链接口出现错误：从用户银行转币给用户b失败");
            LOGGER.error("区块链出错:"+errorMessage);

            //-------》todo 如果银行打给平台出错这里还要银行返回刚才用户打给银行的钱
            ProtobufBean protobufBean3 = Protobuf4EdsaUtils.requestTransfer(bankPrikey, symbolId, bankPubkey, fromPrikey, amount);
            String jsonResult3 = BlockUtils.sendPostParam(protobufBean3);
            boolean flag3 = BlockUtils.vilaResult(jsonResult3);
            if(!flag3){
                String errorMessage3 = BlockUtils.getErrorMessage(jsonResult3);
                LOGGER.error("第2次调用区块链接口出现错误后的异常处理，从银行将币打回用户a的账号时调用区块链又出错,本该打回用户a的钱是"+amount);
                LOGGER.error("区块链出错:"+errorMessage3);
                //====>todo 这里再出错就麻烦了(只能在日志中记录下这个问题)
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage3);
            }
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage);
        }
        LOGGER.info("第2次调用区块链接口：从银行转币给用户b成功");
    }


}
