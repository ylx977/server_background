package com.gws.repositories.query.mess;

import com.gws.entity.xinge.MessagesRecord;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * IosBundle 查询类
 * Created by wd on 17-7-18.
 */

@QBindEntity(entityClass = MessagesRecord.class)
@Data
public class MessagesRecordQuery extends BaseQuery {

    @QBindAttrField(fieldName = "uid", where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName = "isDelete", where = Where.equal)
    private Integer isDelete;

    @QBindAttrField(fieldName = "osType", where = Where.equal)
    private Integer osType;

}
