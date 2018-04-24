package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUsersAuthgroups;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/19.
 */
@QBindEntity(entityClass = BackUser.class)
@Data
public class BackUsersQuery extends BaseQuery {

    @QBindAttrField(fieldName = "uid", where = Where.in)
    private List<Long> uids;

    @QBindAttrField(fieldName = "uid", where = Where.notEqual)
    private Long uidNotIn;

    @QBindAttrField(fieldName = "username", where = Where.equal)
    private String username;

    @QBindAttrField(fieldName = "password", where = Where.equal)
    private String password;

    @QBindAttrField(fieldName = "username", where = Where.like)
    private String usernameLike;

    @QBindAttrField(fieldName = "personName", where = Where.equal)
    private String personName;

    @QBindAttrField(fieldName = "contact", where = Where.equal)
    private String contact;

    @QBindAttrField(fieldName = "isDelete", where = Where.equal)
    private Integer isDelete;

    @QBindAttrField(fieldName = "ctime", where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime", where = Where.lessThanOrEqualTo)
    private Integer cendTime;

}
