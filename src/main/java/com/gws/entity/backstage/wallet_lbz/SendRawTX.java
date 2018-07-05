package com.gws.entity.backstage.wallet_lbz;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author ylx
 * bty,token出币的实体类
 * 接口详情参考下面接口地址
 * https://gitlab.33.cn/wallet/wallet/wikis/%E4%BA%A4%E6%98%93%E6%89%80%E9%92%B1%E5%8C%85%E6%8E%A5%E5%8F%A3
 * Created by fuzamei on 2018/6/21.
 */
@Data
public class SendRawTX {


    private Integer id = 1;

    //这个定死的
    private String method = "Exchange.SendRawTx";

    private Object[] params = new Object[1];

    @Data
    private static class Inner{
        private String cointype = "BTY";
        private String to;
        private Double amount;
        private String tokenSymbol;
        public Inner(String cointype,String to,Double amount,String tokenSymbol){
            this.cointype = cointype;
            this.to = to;
            this.amount = amount;
            this.tokenSymbol = tokenSymbol;
        }
        public Inner(String to,Double amount,String tokenSymbol){
            this.to = to;
            this.amount = amount;
            this.tokenSymbol = tokenSymbol;
        }
    }

    private SendRawTX(Integer id,String cointype,String to,Double amount,String tokenSymbol){
        this.id = id;
        Inner inner = new Inner(cointype,to,amount,tokenSymbol);
        this.params[0] = inner;
    }
    private SendRawTX(Integer id,String to,Double amount,String tokenSymbol){
        this.id = id;
        Inner inner = new Inner(to,amount,tokenSymbol);
        this.params[0] = inner;
    }
    private SendRawTX(String to,Double amount,String tokenSymbol){
        Inner inner = new Inner(to,amount,tokenSymbol);
        this.params[0] = inner;
    }

    public static final SendRawTX getInstance(Integer id,String cointype,String to,Double amount,String tokenSymbol){
        return new SendRawTX(id,cointype,to,amount,tokenSymbol);
    }
    public static final SendRawTX getInstance(Integer id,String to,Double amount,String tokenSymbol){
        return new SendRawTX(id,to,amount,tokenSymbol);
    }
    public static final SendRawTX getInstance(String to,Double amount,String tokenSymbol){
        return new SendRawTX(to,amount,tokenSymbol);
    }

    public static void main(String[] args) {
        SendRawTX instance = getInstance("to", 1000000000d, "USDG");
        System.out.println(JSON.toJSONString(instance,true));
    }

}
