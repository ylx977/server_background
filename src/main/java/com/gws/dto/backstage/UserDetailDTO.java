package com.gws.dto.backstage;

import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Data
public class UserDetailDTO {

    private Long uid;
    private String username;
    private String password;
    private String personName;
    private String contact;
    private Integer ctime;
    private Integer utime;
    private Integer isDelete;
    private String tokenId;
    private String token;
    private List<String> roleName;
    private List<String> authName;
    private List<String> authUrl;
    private Boolean ifFirstLogin;

}
