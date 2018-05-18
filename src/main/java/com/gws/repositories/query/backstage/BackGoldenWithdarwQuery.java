package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackUsersAuthgroups;
import com.gws.entity.backstage.GoldenWithdraw;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@QBindEntity(entityClass = GoldenWithdraw.class)
@Data
public class BackGoldenWithdarwQuery extends BaseQuery{

    @QBindAttrField(fieldName = "id", where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName ="uid",where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName ="personName",where = Where.equal)
    private String personName;

    @QBindAttrField(fieldName ="personName",where = Where.like)
    private String personNameLike;

    @QBindAttrField(fieldName ="phoneNumber",where = Where.equal)
    private String phoneNumber;

    @QBindAttrField(fieldName ="withdrawUnit",where = Where.equal)
    private Integer withdrawUnit;

    @QBindAttrField(fieldName ="withdrawAmount",where = Where.equal)
    private Integer withdrawAmount;

    @QBindAttrField(fieldName ="ctime",where = Where.equal)
    private Integer ctime;

    @QBindAttrField(fieldName ="utime",where = Where.equal)
    private Integer utime;

    @QBindAttrField(fieldName ="status",where = Where.equal)
    private Integer status;

    @QBindAttrField(fieldName ="withdrawTime",where = Where.equal)
    private Integer withdrawTime;

    @QBindAttrField(fieldName ="withdrawTime",where = Where.lessThan)
    private Integer overWithdrawTime;

    @QBindAttrField(fieldName ="ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName ="ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;


}
