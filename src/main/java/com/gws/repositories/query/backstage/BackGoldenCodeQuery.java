package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackGoldenCode;
import com.gws.entity.backstage.GoldenWithdraw;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/15.
 */
@QBindEntity(entityClass = BackGoldenCode.class)
@Data
public class BackGoldenCodeQuery extends BaseQuery {

    @QBindAttrField(fieldName = "id", where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName = "goldenWithdrawId", where = Where.equal)
    private Long goldenWithdrawId;

}
