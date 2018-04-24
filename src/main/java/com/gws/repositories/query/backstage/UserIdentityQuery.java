package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.UsdgUserAccount;
import com.gws.entity.backstage.UserIdentity;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@QBindEntity(entityClass = UserIdentity.class)
@Data
public class UserIdentityQuery {

    @QBindAttrField(fieldName = "realName",where = Where.equal)
    private String realName;

    @QBindAttrField(fieldName = "realName",where = Where.like)
    private String realNameLike;

}
