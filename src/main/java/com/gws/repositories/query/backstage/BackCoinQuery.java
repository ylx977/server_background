package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackAuthgroups;
import com.gws.entity.backstage.CoinFee;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * Created by fuzamei on 2018/6/27.
 */
@QBindEntity(entityClass = CoinFee.class)
@Data
public class BackCoinQuery extends BaseQuery {

    @QBindAttrField(fieldName = "name", where = Where.equal)
    private String name;

}
