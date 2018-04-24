package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.FrontUser;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@QBindEntity(entityClass = FrontUser.class)
@Data
public class FrontUserQuery extends BaseQuery{

    @QBindAttrField(fieldName = "uid",where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName = "username",where = Where.equal)
    private String username;

    @QBindAttrField(fieldName = "username",where = Where.like)
    private String usernameLike;

    @QBindAttrField(fieldName = "phoneNumber",where = Where.equal)
    private String phoneNumber;

    @QBindAttrField(fieldName = "phoneNumber",where = Where.like)
    private String phoneNumberLike;

    @QBindAttrField(fieldName = "emailAddress",where = Where.equal)
    private String emailAddress;

    @QBindAttrField(fieldName = "emailAddress",where = Where.like)
    private String emailAddressLike;

    @QBindAttrField(fieldName = "gender",where = Where.equal)
    private Integer gender;

    @QBindAttrField(fieldName = "userStatus",where = Where.equal)
    private Integer userStatus;

    @QBindAttrField(fieldName = "ctime",where = Where.equal)
    private Integer ctime;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;

    @QBindAttrField(fieldName = "utime",where = Where.equal)
    private Integer utime;
}
