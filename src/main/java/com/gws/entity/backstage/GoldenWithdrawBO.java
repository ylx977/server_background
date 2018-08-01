package com.gws.entity.backstage;

import lombok.Data;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Data
public class GoldenWithdrawBO {

    private Long id;

    private Long uid;

    private String personName;

    private String phoneNumber;

    private String withdrawUnit;

    private Integer withdrawAmount;

    private String goldenCode;

    private List<String> goldenCodes;

    private Integer ctime;

    private Integer utime;

    private Integer status;

    private Integer withdrawTime;

    private Integer page;

    private Integer rowNum;

    private Integer startTime;

    private Integer endTime;

    private Integer lang;

    private Long payUSDG;

    private Double totalUSDG;

    private Double chargeUSDG;

}
