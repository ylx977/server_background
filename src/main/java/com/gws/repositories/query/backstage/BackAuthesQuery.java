package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackAuthes;
import com.gws.entity.backstage.BackAuthgroups;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * Created by fuzamei on 2018/4/18.
 */
@QBindEntity(entityClass = BackAuthes.class)
@Data
public class BackAuthesQuery extends BaseQuery {

    @QBindAttrField(fieldName = "authId", where = Where.equal)
    private Long authId;

    @QBindAttrField(fieldName = "authId", where = Where.in)
    private List<Long> authIds;

    @QBindAttrField(fieldName = "authName",where = Where.equal)
    private String authName;

    @QBindAttrField(fieldName = "authName",where = Where.like)
    private String authNameLike;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;

}
