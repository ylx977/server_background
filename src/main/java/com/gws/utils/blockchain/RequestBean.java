package com.gws.utils.blockchain;

import lombok.Data;
import lombok.ToString;

/**
 * @author WangBin
 */
@Data
@ToString
public class RequestBean {
    private String jsonrpc = "2.0";
//    private final String method = "broadcast_tx_commit";
    private String method;
    private Integer id;
    private Object[] params;

    public RequestBean(Object sign) {
        this.params = new Object[]{sign};
    }
    public RequestBean(Object sign,String method) {
        this.params = new Object[]{sign};
        this.method = method;
    }

    public RequestBean(Object sign,String method,Integer id){
        this.params = new Object[]{sign};
        this.method = method;
        this.id = id;
    }
}