package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.Feedback;
import com.gws.entity.backstage.FrontUserApplyInfo;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@QBindEntity(entityClass = Feedback.class)
@Data
public class FeedBackQuery extends BaseQuery{

    @QBindAttrField(fieldName = "problemType",where = Where.equal)
    private Integer problemType;

    @QBindAttrField(fieldName = "ctime",where = Where.greaterThanOrEqualTo)
    private Integer startTime;

    @QBindAttrField(fieldName = "ctime",where = Where.lessThanOrEqualTo)
    private Integer endTime;

}
