package com.gws.repositories.query.test;

import com.gws.entity.DateTest;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

/**
 * DateTest 查询类
 */

@QBindEntity(entityClass = DateTest.class)
@Data
public class DateTestQuery extends BaseQuery {

}
