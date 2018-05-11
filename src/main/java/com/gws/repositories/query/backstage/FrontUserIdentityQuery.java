package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.FrontUser;
import com.gws.entity.backstage.FrontUserIdentity;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/10.
 */
@QBindEntity(entityClass = FrontUserIdentity.class)
@Data
public class FrontUserIdentityQuery extends BaseQuery {

    @QBindAttrField(fieldName = "uid",where = Where.in)
    private List<Long> uids;

}
