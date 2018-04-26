package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.FrontUserRecharge;
import com.gws.entity.backstage.FrontUserWithdrawApply;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/26.
 */
@QBindEntity(entityClass = FrontUserWithdrawApply.class)
@Data
public class FrontUserWithdrawApplyQuery extends BaseQuery{

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

    @QBindAttrField(fieldName = "coinType",where = Where.equal)
    private Integer coinType;

    @QBindAttrField(fieldName = "applyStatus",where = Where.equal)
    private Integer applyStatus;

    @QBindAttrField(fieldName = "applyStatus",where = Where.in)
    private List<Integer> applyStatuses;

    @QBindAttrField(fieldName = "firstCheckUid",where = Where.equal)
    private Long firstCheckUid;

    @QBindAttrField(fieldName = "firstCheckName",where = Where.like)
    private String firstCheckNameLike;

    @QBindAttrField(fieldName = "firstCheckName",where = Where.equal)
    private String firstCheckName;

    @QBindAttrField(fieldName = "secondCheckUid",where = Where.equal)
    private Long secondCheckUid;

    @QBindAttrField(fieldName = "secondCheckName",where = Where.like)
    private String secondCheckNameLike;

    @QBindAttrField(fieldName = "secondCheckName",where = Where.equal)
    private String secondCheckName;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;

}
