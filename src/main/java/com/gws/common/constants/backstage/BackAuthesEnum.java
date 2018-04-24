package com.gws.common.constants.backstage;

import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Getter
public enum BackAuthesEnum {

    CREATE_USER(1L,"创建后台用户","/api/backstage/userManagement/users/createUser"),
    QUERY_USERS(2L,"查询后台用户信息","/api/backstage/userManagement/users/queryUserInfo"),
    UPDATE_USER(3L,"修改后台用户信息","/api/backstage/userManagement/users/updateUserInfo"),
    DELETE_USERS(4L,"删除后台用户","/api/backstage/userManagement/users/deleteUsers"),
    SHOW_USER_ROLES(5L,"查看后台用户的角色信息","/api/backstage/userManagement/users/showRoleInfoUnderUser"),
    ASSIGN_USER_ROLES(6L,"分配后台用户的角色信息","/api/backstage/userManagement/users/assignRoles4User"),

    CREATE_ROLE(7L,"创建后台角色","/api/backstage/userManagement/role/createRole"),
    QUERY_ROLES(8L,"查询后台角色信息","/api/backstage/userManagement/role/queryRoles"),
    UPDATE_ROLE(9L,"修改后台角色信息","/api/backstage/userManagement/role/updateRole"),
    DELETE_ROLES(10L,"删除后台角色","/api/backstage/userManagement/role/deleteRoles"),
    SHOW_ROLE_AUTHES(11L,"查看后台角色的权限信息","/api/backstage/userManagement/role/showAuthoritiesUnderRole"),
    ASSIGN_ROLE_AUTHES(12L,"查看后台用户的角色信息","/api/backstage/userManagement/role/assignAuthorities4Role"),

    QUERY_AUTHES(13L,"查询后台权限信息","/api/backstage/userManagement/auth/queryAuthorities"),

    MODIFY_PASSWORD(14L,"修改个人密码","/api/backstage/userCenter/modifyPassword"),
    QUERY_GOLDEN_WITHDRAW(15L,"查询黄金提取记录信息","/api/backstage/goldenWithdraw/queryGoldenWithdraw"),
    INSERT_COLEDN_CODE(16L,"录入黄金编号","/api/backstage/goldenWithdraw/insertGoldenCode"),

    QUERY_FRONT_USERS(17L,"查询前台用户信息","/api/backstage/frontUserManagement/users/queryFrontUserInfo"),
    QUERY_FRONT_APPLY_INFO(18L,"查询前台用户修改申请","/api/backstage/frontUserManagement/review/queryApplyInfo"),
    APPROVE_FRONT_APPLY(19L,"同意前台用户修改申请","/api/backstage/frontUserManagement/review/approveApply"),
    REJECT_FRONT_APPLY(20L,"拒绝前台用户修改申请","/api/backstage/frontUserManagement/review/rejectApply"),

    UPDATE_PRICE(21L,"修改平台市价","/api/backstage/marketPrice/updatePrice"),

    UPDATE_FRONT_USERS(22L,"修改前台用户信息","/api/backstage/frontUserManagement/users/updateFrontUserInfo"),

    BANNER_BASIC_CONFIG(23L,"后台banner轮播图基本参数配置","/api/backstage/bannerConfig/basicBannerConfig"),
    UPDATE_BANNER(24L,"更新轮播图信息","/api/backstage/bannerConfig/updateBanner"),
    QUERY_BANNER(25L,"查询轮播图","/api/backstage/bannerConfig/queryBanners"),

//    CONFIRM_COLEDN_CHECKOUT(22L,"确认黄金出库","/api/backstage/goldenWithdraw/confirmCheckout"),



    ;



    private Long authId;
    private String authName;
    private String authUrl;
    BackAuthesEnum(Long authId, String authName, String authUrl){
        this.authId = authId;
        this.authName = authName;
        this.authUrl = authUrl;
    }

    /**
     * 普通管理员权限信息id号集合(还要分配修改密码的权限)
     */
    public static final List<Long> FORBIDDEN_AUTH_INDEX = Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L,13L);
    public static final List<Long> ADMIN_AUTH_INDEX = Arrays.asList(1L,2L,3L,4L,5L,6L,8L,11L,12L,13L,14L);
    public static final List<String> AUTH_URLS = new ArrayList<>();
    public static final List<String> AUTH_NAMES = new ArrayList<>();
    static{
        for (BackAuthesEnum backAuthesEnum : BackAuthesEnum.values()) {
            AUTH_URLS.add(backAuthesEnum.getAuthUrl());
            AUTH_NAMES.add(backAuthesEnum.getAuthName());
        }
    }
}
