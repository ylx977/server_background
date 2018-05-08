package com.gws.repositories.query.backstage;

import com.gws.entity.backstage.MarketPrice;
import com.gws.entity.backstage.Notice;
import com.gws.utils.query.Where;
import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import com.gws.utils.query.core.BaseQuery;
import lombok.Data;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@QBindEntity(entityClass = Notice.class)
@Data
public class NoticeQuery extends BaseQuery{

    @QBindAttrField(fieldName = "id",where = Where.equal)
    private Long id;
    @QBindAttrField(fieldName = "title",where = Where.like)
    private String title;
    @QBindAttrField(fieldName = "author",where = Where.like)
    private String author;
    @QBindAttrField(fieldName = "uid",where = Where.equal)
    private Long uid;
    @QBindAttrField(fieldName = "uid",where = Where.in)
    private List<Long> uids;
    @QBindAttrField(fieldName = "show",where = Where.equal)
    private Integer show;
    @QBindAttrField(fieldName = "top",where = Where.equal)
    private Integer top;
    @QBindAttrField(fieldName = "deleted",where = Where.equal)
    private Integer deleted;

}
