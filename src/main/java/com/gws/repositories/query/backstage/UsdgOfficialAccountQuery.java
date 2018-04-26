package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.UsdgOfficialAccount;
import com.gws.entity.backstage.UsdgUserAccount;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/25.
 */
@QBindEntity(entityClass = UsdgOfficialAccount.class)
@Data
public class UsdgOfficialAccountQuery extends BaseQuery{

    @QBindAttrField(fieldName = "id",where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName = "type",where = Where.equal)
    private Integer type;

}
