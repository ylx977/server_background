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
    //todo 核心权限，不显示
    CREATE_USER(1L,"创建后台用户","/api/backstage/userManagement/users/createUser"),
    //todo 核心权限，不显示
    QUERY_USERS(2L,"查询后台用户信息","/api/backstage/userManagement/users/queryUserInfo"),
    //todo 核心权限，不显示
    UPDATE_USER(3L,"修改后台用户信息","/api/backstage/userManagement/users/updateUserInfo"),
    //todo 核心权限，不显示
    DELETE_USERS(4L,"删除后台用户","/api/backstage/userManagement/users/deleteUsers"),
    //todo 核心权限，不显示
    SHOW_USER_ROLES(5L,"查看后台用户的角色信息","/api/backstage/userManagement/users/showRoleInfoUnderUser"),
    //todo 核心权限，不显示
    ASSIGN_USER_ROLES(6L,"分配后台用户的角色信息","/api/backstage/userManagement/users/assignRoles4User"),
    //todo 核心权限，不显示
    UPDATE_USER_STATUS(7L,"修改后台用户的状态","/api/backstage/userManagement/users/updateUserStatus"),
    //todo 核心权限，不显示
    CREATE_ROLE(8L,"创建后台角色","/api/backstage/userManagement/role/createRole"),
    //todo 核心权限，不显示
    QUERY_ROLES(9L,"查询后台角色信息","/api/backstage/userManagement/role/queryRoles"),
    //todo 核心权限，不显示
    UPDATE_ROLE(10L,"修改后台角色信息","/api/backstage/userManagement/role/updateRole"),
    //todo 核心权限，不显示
    DELETE_ROLES(11L,"删除后台角色","/api/backstage/userManagement/role/deleteRoles"),
    //todo 核心权限，不显示
    SHOW_ROLE_AUTHES(12L,"查看后台角色的权限信息","/api/backstage/userManagement/role/showAuthoritiesUnderRole"),
    //todo 核心权限，不显示
    ASSIGN_ROLE_AUTHES(13L,"查看后台用户的角色信息","/api/backstage/userManagement/role/assignAuthorities4Role"),
    //todo 核心权限，不显示
    QUERY_AUTHES(14L,"查询后台权限信息","/api/backstage/userManagement/auth/queryAuthorities"),

    MODIFY_PASSWORD(15L,"修改个人密码","/api/backstage/userCenter/modifyPassword"),
    QUERY_GOLDEN_WITHDRAW(16L,"查询黄金提取记录信息","/api/backstage/goldenWithdraw/queryGoldenWithdraw"),
    INSERT_COLEDN_CODE(17L,"录入黄金编号","/api/backstage/goldenWithdraw/insertGoldenCode"),
    QUERY_COLEDN_CODE(18L,"查看黄金编号","/api/backstage/goldenWithdraw/queryGoldenCode"),

    QUERY_FRONT_USERS(19L,"查询前台用户信息","/api/backstage/frontUserManagement/users/queryFrontUserInfo"),
    QUERY_FRONT_APPLY_INFO(20L,"查询前台用户修改申请","/api/backstage/frontUserManagement/review/queryApplyInfo"),
    QUERY_FRONT_APPLY_INFO_HISTORY(21L,"查询前台用户修改历史记录","/api/backstage/frontUserManagement/review/queryApplyHistory"),
    APPROVE_FRONT_APPLY(22L,"同意前台用户修改申请","/api/backstage/frontUserManagement/review/approveApply"),
    REJECT_FRONT_APPLY(23L,"拒绝前台用户修改申请","/api/backstage/frontUserManagement/review/rejectApply"),

    //todo 核心权限，不显示
    UPDATE_PRICE(24L,"修改平台市价","/api/backstage/marketPrice/updatePrice"),

    QUERY_PRICE(25L,"查询平台CNY/SGD汇率，SGD/USD汇率报价","/api/backstage/marketPrice/queryPrice "),
    QUERY_PRICE_HISTORY(26L,"查看平台市价历史记录","/api/backstage/marketPrice/queryPriceHistory"),

    UPDATE_FRONT_USERS(27L,"修改前台用户信息","/api/backstage/frontUserManagement/users/updateFrontUserInfo"),

    BANNER_BASIC_CONFIG(28L,"后台banner轮播图基本参数配置","/api/backstage/bannerConfig/basicBannerConfig"),
    UPDATE_BANNER(29L,"更新轮播图信息","/api/backstage/bannerConfig/updateBanner"),
    QUERY_BANNER(30L,"查询轮播图","/api/backstage/bannerConfig/queryBanners"),

    QUERY_USER_ASSET(31L,"查询前台用户的资产","/api/backstage/assetManagement/account/queryFrontUserAccount"),
    QUERY_USER_RECHAREG(32L,"查询前台用户的充币记录","/api/backstage/assetManagement/topUp/queryFrontUserRecharge"),
    QUERY_USER_WITHDRAW(33L,"查询前台用户的提币申请","/api/backstage/assetManagement/withdraw/queryFrontUserWithdraw"),
    FIRST_PASS_USER_WITHDRAW(34L,"初审同意前台用户的提币申请","/api/backstage/assetManagement/withdraw/firstPass"),
    SECOND_PASS_USER_WITHDRAW(35L,"复审同意前台用户的提币申请","/api/backstage/assetManagement/withdraw/secondPass"),
    REJECT_USER_WITHDRAW(36L,"拒绝前台用户的提币申请","/api/backstage/assetManagement/withdraw/rejectWithdraw"),
    QUERY_USER_WITHDRAW_HISTORY(37L,"查询前台用户的提币申请记录","/api/backstage/assetManagement/withdraw/queryFrontUserWithdrawHistory"),
    ADD_USDG(38L,"增加平台usdg总量","/api/backstage/assetManagement/balance/addUsdg"),
    QUERY_PLATFORM_ASSETS(39L,"查询平台的资产余额","/api/backstage/assetManagement/balance/queryAssetBalance"),
    QUERY_EXCHANGE(40L,"查询平台的兑换信息","/api/backstage/assetManagement/exchange/queryExchange"),
    QUERY_PLATFORM_ADDRESS(41L,"点击充币查询平台的币地址","/api/backstage/assetManagement/balance/queryPlatformAddress"),
    QUERY_PLATFORM_MANAGED_ADDRESSES(42L,"点击地址管理查询所有地址","/api/backstage/assetManagement/balance/queryAddresses"),
    DELETE_MANAGED_ADDRESSES(43L,"删除管理地址","/api/backstage/assetManagement/balance/deleteAddress"),
    ADD_MANAGED_ADDRESSES(44L,"添加管理地址","/api/backstage/assetManagement/balance/addAddress"),
    SET_DEFAULT_MANAGED_ADDRESSES(45L,"设置管理的默认地址","/api/backstage/assetManagement/balance/setDefaultAddress"),

    //---------------------->超级管理员权限
    //todo 核心权限，不显示
    BTY_WITHDRAW(46L,"提取比特元","/api/backstage/assetManagement/balance/withdrawBTY"),


    QUERY_FEEDBACK(47L,"查询前台用户反馈信息","/api/backstage/feedback/queryFeedbacks"),

    QUERY_NOTICES(48L,"查询公告信息","/api/backstage/notice/queryNotices"),
    QUERY_NOTICE_CONTENT(49L,"查询公告内容","/api/backstage/notice/queryNoticeContent"),
    UPDATE_NOTICE_CONTENT(50L,"修改公告内容","/api/backstage/notice/updateNotice"),
    DELETE_NOTICE(51L,"删除公告","/api/backstage/notice/deleteNotice"),
    PUBLISH_NOTICE(52L,"新建公告","/api/backstage/notice/publishNotice"),
    UPDATE_NOTICE_STATUS(53L,"修改公告状态","/api/backstage/notice/updateNoticeStatus"),

    QUERY_PROBLEMS(54L,"查询常见问题","/api/backstage/problem/queryProblems"),
    QUERY_PROBLEM_CONTENT(55L,"查询常见问题内容","/api/backstage/problem/queryProblemContent"),
    UPDATE_PROBLEM_CONTENT(56L,"修改常见问题内容","/api/backstage/problem/updateProblem"),
    DELETE_PROBLEM(57L,"删除常见问题","/api/backstage/problem/deleteProblem"),
    PUBLISH_PROBLEM(58L,"新建常见问题","/api/backstage/problem/publishProblem"),


    SHOW_BTYUSDG_TRADE_FEE(59L,"查询BTY兑USDG交易手续费","/api/backstage/feeConfigure/trade/showBTYUSDGTradeFee"),
    SET_BTYUSDG_TRADE_FEE(60L,"设置BTY兑USDG交易手续费","/api/backstage/feeConfigure/trade/setBTYUSDGTradeFee"),
    SHOW_DRAW_FEE(61L,"显示提币手续费","/api/backstage/feeConfigure/draw/showDrawFee"),
    SET_DRAW_FEE(62L,"设置提币手续费","/api/backstage/feeConfigure/draw/setDrawFee"),
    SHOW_GOLDDRAW_FEE(63L,"显示提黄金手续费","/api/backstage/feeConfigure/goldDraw/showGoldDrawFee"),
    SET_GOLDDRAW_FEE(64L,"设置提黄金手续费","/api/backstage/feeConfigure/goldDraw/setGoldDrawFee"),


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
