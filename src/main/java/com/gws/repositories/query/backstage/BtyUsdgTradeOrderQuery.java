package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BtyUsdgTradeOrder;
import com.gws.entity.backstage.FrontUserApplyInfo;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/26.
 */
@QBindEntity(entityClass = BtyUsdgTradeOrder.class)
@Data
public class BtyUsdgTradeOrderQuery extends BaseQuery{

    @QBindAttrField(fieldName = "uid",where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName = "personName",where = Where.equal)
    private String personName;

    @QBindAttrField(fieldName = "personName",where = Where.like)
    private String personNameLike;

    @QBindAttrField(fieldName = "phoneNumber",where = Where.equal)
    private String phoneNumber;

    @QBindAttrField(fieldName = "phoneNumber",where = Where.like)
    private String phoneNumberLike;

    @QBindAttrField(fieldName = "email",where = Where.equal)
    private String email;

    @QBindAttrField(fieldName = "email",where = Where.like)
    private String emailLike;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;

    @QBindAttrField(fieldName = "tradeType",where = Where.equal)
    private Integer tradeType;

}
