package com.gws.entity.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Data
public class FrontUserBO {

    private Long id;

    private Long uid;

    private String username;

    private String personName;

    private String phoneNumber;

    private String email;

    private Integer page;

    private Integer rowNum;

    private Integer startTime;

    private Integer endTime;

    private Integer coinType;

}
