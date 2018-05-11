package com.gws.utils.blockchain;

import lombok.Data;
import lombok.ToString;

/**
 * @author WangBin
 */
@Data
@ToString
public class ProtobufBean {
    private Long instructionId;
    private String signature;

    public ProtobufBean(Long instructionId, String signature) {
        this.instructionId = instructionId;
        this.signature = signature;
    }
}
