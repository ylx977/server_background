package com.gws.dto.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by fuzamei on 2018/4/19.
 */
@Data
public class BackAuthgroupsVO {
    private Long authgroupId;

    private String authgroupName;

    /**
     * 1表示选中，0表示未选中
     */
    private Integer isSelected;
}
