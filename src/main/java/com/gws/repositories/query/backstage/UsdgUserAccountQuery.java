package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.MarketPrice;
import com.gws.entity.backstage.UsdgUserAccount;
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
 * Created by fuzamei on 2018/4/24.
 */
@QBindEntity(entityClass = UsdgUserAccount.class)
@Data
public class UsdgUserAccountQuery extends BaseQuery{

    @QBindAttrField(fieldName = "id",where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName = "uid",where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName = "uid",where = Where.in)
    private List<Long> uids;


}
