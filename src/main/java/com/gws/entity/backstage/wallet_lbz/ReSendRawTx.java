package com.gws.entity.backstage.wallet_lbz;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/21.
 */
@Data
public class ReSendRawTx {

    private Integer id = 1;

    private String method = "Exchange.ReSendRawTx";

    private Object[] params = new Object[1];

    @Data
    private static class Inner{
        private String cointype = "BTY";
        private String txid;
        public Inner(String cointype,String txid){
            this.cointype = cointype;
            this.txid = txid;
        }
        public Inner(String txid){
            this.txid = txid;
        }

    }

    private ReSendRawTx(Integer id,String cointype,String txid){
        this.id = id;
        Inner inner = new Inner(cointype,txid);
        this.params[0] = inner;
    }
    private ReSendRawTx(String cointype,String txid){
        Inner inner = new Inner(cointype,txid);
        this.params[0] = inner;
    }
    private ReSendRawTx(String txid){
        Inner inner = new Inner(txid);
        this.params[0] = inner;
    }

    public static final ReSendRawTx getInstance(Integer id,String cointype,String txid){
        return new ReSendRawTx(id,cointype,txid);
    }
    public static final ReSendRawTx getInstance(String cointype,String txid){
        return new ReSendRawTx(cointype,txid);
    }
    public static final ReSendRawTx getInstance(String txid){
        return new ReSendRawTx(txid);
    }

    public static void main(String[] args) {
        ReSendRawTx hash = getInstance("hash");
        System.out.println(JSON.toJSONString(hash));
    }

}
