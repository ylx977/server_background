package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.FrontUserApplyInfo;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import javax.persistence.Column;
import javax.swing.*;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
@QBindEntity(entityClass = FrontUserApplyInfo.class)
@Data
public class FrontUserApplyInfoQuery extends BaseQuery{

    @QBindAttrField(fieldName = "id",where = Where.equal)
    private Long id;

    @QBindAttrField(fieldName = "uid", where = Where.equal)
    private Long uid;

    @QBindAttrField(fieldName = "phoneNumber", where = Where.equal)
    private String phoneNumber;

    @QBindAttrField(fieldName = "phoneNumber", where = Where.like)
    private String phoneNumberLike;

    @QBindAttrField(fieldName = "applyStatus", where = Where.like)
    private Integer applyStatus;

    @QBindAttrField(fieldName = "applyStatus", where = Where.in)
    private List<Integer> applyStatuses;

    @QBindAttrField(fieldName = "ctime", where = Where.greaterThanOrEqualTo)
    private Integer cstartTime;

    @QBindAttrField(fieldName = "ctime", where = Where.lessThanOrEqualTo)
    private Integer cendTime;

}
