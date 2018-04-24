package com.gws.repositories.query.mess;

import com.gws.entity.CertIdMessage;
import com.gws.entity.xinge.NoticesRecord;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * CertIdMessage 查询类
 * Created by wd on 17-7-18.
 */

@QBindEntity(entityClass = CertIdMessage.class)
@Data
public class CertIdMessageQuery extends BaseQuery {

    /**
     * 根据指定信息查询只需要在Query中绑定指定参数即可
     */
    /**
     * 根据Id查询
     */
    @QBindAttrField(fieldName = "id", where = Where.equal)
    private Long id;


}
