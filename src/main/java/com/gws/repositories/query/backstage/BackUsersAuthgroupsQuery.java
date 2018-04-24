package com.gws.repositories.query.backstage;

import com.gws.entity.CertIdMessage;
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
 * Created by fuzamei on 2018/4/18.
 */
@QBindEntity(entityClass = BackUsersAuthgroups.class)
@Data
public class BackUsersAuthgroupsQuery extends BaseQuery {

    @QBindAttrField(fieldName = "id", where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName ="uid",where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName ="authgroupId",where = Where.equal)
    private Long authgroupId;

    @QBindAttrField(fieldName ="authgroupId",where = Where.in)
    private List<Long> authgroupIds;


}

