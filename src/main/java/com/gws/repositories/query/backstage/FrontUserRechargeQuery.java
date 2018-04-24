package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.FrontUser;
import com.gws.entity.backstage.FrontUserRecharge;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@QBindEntity(entityClass = FrontUserRecharge.class)
@Data
public class FrontUserRechargeQuery extends BaseQuery {

    @QBindAttrField(fieldName = "uid",where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName = "coinType",where = Where.equal)
    private Integer coinType;

    @QBindAttrField(fieldName = "uid",where = Where.in)
    private List<Long> uids;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer cendTime;

}
