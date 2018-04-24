package com.gws.repositories.query.mess;

import com.gws.entity.xinge.NoticesRecord;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * IosBundle 查询类
 * Created by wd on 17-7-18.
 */

@QBindEntity(entityClass = NoticesRecord.class)
@Data
public class NoticesRecordQuery extends BaseQuery {

    /**
     * 比最小值大
     */
    @QBindAttrField(fieldName = "ctime", where = Where.greaterThanOrEqualTo)
    private Integer minTime;

    /**
     * 比最大值小
     */
    @QBindAttrField(fieldName = "ctime", where = Where.lessThanOrEqualTo)
    private Integer maxTime;

    /**
     * 是否已删除
     */
    @QBindAttrField(fieldName = "isDelete", where = Where.equal)
    private Integer isDelete;

    @QBindAttrField(fieldName = "osType", where = Where.equal)
    private Integer osType;
}
