package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.FrontUser;
import com.gws.entity.backstage.MarketPrice;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;

/**
 * Created by fuzamei on 2018/4/21.
 */
@QBindEntity(entityClass = MarketPrice.class)
@Data
public class MarketPriceQuery extends BaseQuery{

    @QBindAttrField(fieldName = "id",where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName = "cnysgd",where = Where.equal)
    private Double cnysgd;

    /**
     * 买入点差
     */
    @QBindAttrField(fieldName = "buySpread",where = Where.equal)
    private Double buySpread;

    /**
     * 买入点差
     */
    @QBindAttrField(fieldName = "sellSpread",where = Where.equal)
    private Double sellSpread;


}
