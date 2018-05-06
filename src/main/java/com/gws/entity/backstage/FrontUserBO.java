package com.gws.entity.backstage;

import com.gws.dto.backstage.UserDetailDTO;
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

    private Long firstCheckUid;

    private String firstCheckName;

    private Long secondCheckUid;

    private String secondCheckName;

    private Integer tradeType;

    private UserDetailDTO userDetailDTO;

}
