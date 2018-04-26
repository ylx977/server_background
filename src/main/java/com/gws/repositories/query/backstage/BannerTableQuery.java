package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BannerTable;
import com.gws.entity.backstage.FrontUserApplyInfo;
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
 * Created by fuzamei on 2018/4/25.
 */
@QBindEntity(entityClass = BannerTable.class)
@Data
public class BannerTableQuery extends BaseQuery {

    @QBindAttrField(fieldName = "id",where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName = "id",where = Where.in)
    private List<Long> ids;

    @QBindAttrField(fieldName = "orders",where = Where.equal)
    private Integer orders;

    @QBindAttrField(fieldName = "bannerUrl",where = Where.equal)
    private String bannerUrl;

}
