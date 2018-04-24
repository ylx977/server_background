package com.gws.dto.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Data
public class BackAuthesVO {
    private Long authId;

    private String authName;

    /**
     * 1表示选中，0表示未选中
     */
    private Integer isSelected;
}
