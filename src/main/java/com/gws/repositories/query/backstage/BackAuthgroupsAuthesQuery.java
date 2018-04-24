package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackAuthgroups;
import com.gws.entity.backstage.BackAuthgroupsAuthes;
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
@QBindEntity(entityClass = BackAuthgroupsAuthes.class)
@Data
public class BackAuthgroupsAuthesQuery extends BaseQuery{

    @QBindAttrField(fieldName = "authgroupId", where = Where.equal)
    private Long authgroupId;

    @QBindAttrField(fieldName = "authId", where = Where.equal)
    private Long authId;

    @QBindAttrField(fieldName = "authgroupId", where = Where.in)
    private List<Long> authgroupIds;

}
