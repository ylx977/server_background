package com.gws.entity.backstage.wallet_lbz;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/21.
 */
@Data
public class CreateAddress {

    private Integer id = 1;
    private String method = "Exchange.CreateAddress";
    private Object[] params = new Object[1];

    @Data
    private static class Inner{
        private String cointype = "BTY";
        private String uid;

        public Inner(String cointype,String uid){
            this.cointype = cointype;
            this.uid = uid;
        }
        public Inner(String uid){
            this.uid = uid;
        }
    }

    private CreateAddress(Integer id,String cointype,String uid){
        this.id = id;
        Inner inner = new Inner(cointype,uid);
        this.params[0] = inner;
    }
    private CreateAddress(String cointype,String uid){
        Inner inner = new Inner(cointype,uid);
        this.params[0] = inner;
    }
    private CreateAddress(String uid){
        Inner inner = new Inner(uid);
        this.params[0] = inner;
    }

    public static final CreateAddress getInstance(Integer id,String cointype,String uid){
        return new CreateAddress(id,cointype,uid);
    }
    public static final CreateAddress getInstance(String cointype,String uid){
        return new CreateAddress(cointype,uid);
    }
    public static final CreateAddress getInstance(String uid){
        return new CreateAddress(uid);
    }

    public static void main(String[] args) {
        CreateAddress instance = getInstance("111");
        System.out.println(JSON.toJSONString(instance,true));
    }

}
