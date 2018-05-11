package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by fuzamei on 2018/4/19.
 */
@Data
public class BackUserBO {

    private Long operatorUid;

    private Long uid;

    private List<Long> uids;

    private List<Long> authgroupIds;

    private String username;

    private String password;

    private String personName;

    private String contact;

    private Integer ctime;

    private Integer utime;

    private Integer isDelete;

    private Integer page;

    private Integer rowNum;

    private Integer startTime;

    private Integer endTime;

    private Long roleId;

    private List<Long> roleIds;

    private String roleName;

    private List<Long> authIds;

    private String authName;

    private String originalPassword;

    private String newPassword;

    private String newConfirmedPassword;

    private Integer lang;

}
