package com.gws.utils.blockchain;

import lombok.Data;
import lombok.ToString;

/**
 * @author WangBin
 */
@Data
@ToString
public class RequestBean {
    private final String jsonrpc = "2.0";
    private final String method = "broadcast_tx_commit";
    private final Object id = null;
    private String[] params;

    public RequestBean(String sign) {
        this.params = new String[]{sign};
    }
}