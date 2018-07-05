package com.gws.controllers.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.BackAuthes;
import com.gws.entity.backstage.BackAuthgroups;
import com.gws.entity.backstage.createRawTransaction.CreateTXUtils;
import com.gws.entity.backstage.createRawTransaction.RawTXResp;
import com.gws.entity.backstage.createRawTransaction.SendTXResp;
import com.gws.repositories.query.backstage.BackAuthgroupsQuery;
import com.gws.repositories.slave.backstage.BackAuthgroupsSlave;
import com.gws.repositories.slave.backstage.BackUserTokenSlave;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.wallet.utils.Bip32Util;
import org.hibernate.internal.QueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@RestController
@RequestMapping("/api")
public class TestController1 extends BaseController{

    @Autowired
    private BackAuthgroupsSlave backAuthgroupsSlave;

    @Autowired
    private LangReadUtil langReadUtil;

    @PersistenceContext
    private EntityManager em;

    @RequestMapping("/test")
    public List<BackAuthgroups> test(@RequestBody BackAuthes backAuthes){
        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupIds(Arrays.asList(new Long[]{1L,2L}));
        List<BackAuthgroups> all = backAuthgroupsSlave.findAll(backAuthgroupsQuery);
        System.out.println(backAuthes);
        return all;
    }

    @RequestMapping("/go")
    public String test2(@RequestBody BackAuthes backAuthes){
        Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id not in (1,2)");
        List<Long> resultList = (List<Long>) query.getResultList();
        System.out.println(resultList);
        return "ok";
    }

    @RequestMapping("/go2")
    public JsonResult test3(@RequestBody BackAuthes backAuthes){
        try {
            if(1==1){
                throw new RuntimeException(LangReadUtil.getProperty("WRONG_FORMAT"));
            }
            System.out.println("测试方法");
            return new JsonResult("200",LangReadUtil.getProperty("LESS_THAN"),null);
        }catch (Exception e){
            System.out.println("测试方法");
            return new JsonResult("200",LangReadUtil.getProperty("LESS_THAN")+":"+e.getMessage(),null);
        }
    }



    public static void main(String[] args) {
        //王栋生成的有问题的地址和私钥
//        String from = "1Er453h2X63ut5rqWaY7gqNKekZiXVUMeN";//uid=1528191507033034
//        String fromPrivateKey = "3038949cfd2111eec506b646f5d78eead8fa51af3f2defbd217301f4583a571f";
//        String fromPublicKey = "02cf2e1d0038d2c2eceef8729d1fc7f4e28a520ec97d5d9d0893111c3c90d80019";

        //正常的普通地址
        String from = "1EM22831bVJSfjpzfPZ32Kph3suyo2bcTW";
        String fromPrivateKey = "bb64e94f4e5859668dd85ccc8a139f58a9b6bc843ca1ee50edc2d7d15532d309";
        String fromPublicKey = "0298b7942062e7514e7a5202e85f04eabab3a5c6775b24eed0cedeb107ea39f3c1";

        //*****************官方地址*******************
//        String from = "1CKUFcmCE3qxtdMwSYpv4tgJZ2igDuR7i1";
//        String fromPrivateKey = "f26290220a8e9e4f9ff17dcb798978113d19ff05f57f26cae07b614027a7e2b3";
//        String fromPublicKey = "03115960dbee9e90a88966809223bf790c9014afdc396ef96d6d3138a4c2252618";
        //*****************官方地址*******************


        double amountX = 0.5d;
        long amount = (long)(amountX*100000000);
        double feeX = 0.001d;
        long fee = (long)(feeX *100000000);


        String toAddress = "1CKUFcmCE3qxtdMwSYpv4tgJZ2igDuR7i1";


        //发送4.1.1 构造交易CreateRawTransaction的接口
        String createRawTXResult = CreateTXUtils.createRawTransaction(toAddress, amount, fee, "welcome to use", false, "BTY");
        //解析返回的结果
        RawTXResp rawTXResp = JSON.parseObject(createRawTXResult, RawTXResp.class);
        if(StringUtils.isEmpty(rawTXResp.getResult())){
            //如果result为空，直接将error中的错误信息返回给前端
            throw new RuntimeException(ErrorMsg.BLOCK_CHIAN_ERROR.getMessageCN());
        }
        System.out.println(createRawTXResult);

        String unsigntx = rawTXResp.getResult();
        String sign = Bip32Util.sign(unsigntx,fromPrivateKey);
//        ECKey key = ECKey.fromPrivate(HexUtil.hexString2Bytes(fromPrivateKey));
//        String sign = key.signMessage(unsigntx);
//        System.out.println(sign);


        //发送4.1.2 发送交易sendRawTransaction的接口
        String sendRawTXResult = CreateTXUtils.SendRawTransaction(unsigntx,sign,fromPublicKey,1);
        //解析返回的结果
        SendTXResp sendTXResp = JSON.parseObject(sendRawTXResult, SendTXResp.class);
        if(StringUtils.isEmpty(sendTXResp.getResult())){
            //如果result为空，直接将error中的错误信息返回给前端
            System.err.println(sendRawTXResult);
            throw new RuntimeException(ErrorMsg.BLOCK_CHIAN_ERROR.getMessageCN());
        }
        System.out.println(sendRawTXResult);
    }

}
