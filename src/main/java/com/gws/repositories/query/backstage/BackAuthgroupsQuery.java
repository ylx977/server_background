package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackAuthgroups;
import com.gws.entity.backstage.BackUsersAuthgroups;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@QBindEntity(entityClass = BackAuthgroups.class)
@Data
public class BackAuthgroupsQuery extends BaseQuery {

    @QBindAttrField(fieldName = "authgroupId", where = Where.equal)
    private Long authgroupId;

    @QBindAttrField(fieldName = "authgroupId", where = Where.in)
    private List<Long> authgroupIds;

    @QBindAttrField(fieldName = "authgroupName", where = Where.equal)
    private String authgroupName;

    @QBindAttrField(fieldName = "authgroupName", where = Where.like)
    private String authgroupNameLike;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;
}
