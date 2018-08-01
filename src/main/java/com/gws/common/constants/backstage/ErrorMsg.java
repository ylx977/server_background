package com.gws.common.constants.backstage;

import lombok.Getter;

/**
 *  @author ylx
 * Created by fuzamei on 2018/5/11.
 */
@Getter
public enum ErrorMsg {

    LESS_THAN("最大值应该小于","maximum should be lesser than "),
    LESS_THAN_OR_EQUAL("最大值应该小于等于","maximum should be lesser than or equal "),
    GREATER_THAN("最小值应该大于","minimum should be greater than "),
    GREATER_THAN_OR_EQUAL("最小值应该大于等于","minimum should be greater than or equal "),
    NULL_POINT("对象为null","the object is null"),
    NUMBER_PARSE_EXCEPTION("数据类型解析异常","parsing data occurs exception"),
    WRONG_FORMAT("与指定格式不符","wrong input format"),
    CAST_STRING_EXCEPTION("强转String异常","cast to String type occurs exception"),
    EMPTY_STRING("字符串不能为空","string can not be empty"),
    CAST_INTEGER_EXCEPTION("强转Inetegr异常","cast to integer type occurs exception"),
    CAST_LONG_EXCEPTION("强转Long异常","cast to long type occurs exception"),
    NULL_INTEGER("integer为空","integer can not be null"),
    PARAMETER_LOST("请求参数丢失","parameter in request lost"),


    INPUT_GOLD_CODE("请输入黄金编号","please enter gold codes"),
    WRONG_PASSWORD("原始密码输入错误","wrong original current password"),
    PASSWORD_INCONSISTENT("两次输入的密码不一致","new password and confirmed password is inconsistent"),
    SAME_PASSWORD("新密码不能和原密码相同","new password and current password is the same"),
    CANT_ASSIGN_AUTH_FOR_ADMINS("不能给管理员或超级管理员分配权限信息","you are not allowed to assign any authorities for to admin or super admin"),
    CANT_ASSIGN_CORE_AUTHES("不能分配核心权限","you are not allowed to assign any CORE authorities to users"),
    CANT_QUERY_SUPERADMIN_AUTHES("无法查看超级管理员的权限信息","you are not allowed to query super admin's authority info"),
    INPUT_DELETED_ROLEID("请输入要删除的角色id","please enter roleId that you want to delete"),
    CANT_DELETE_ADMIN_ROLES("不能删除管理员和超级管理员角色","you are not allowed to delete the role of admin or super admin"),
    CANT_MODIFY_ADMIN_ROLES("不能修改管理员角色信息","you are not allowed to modify the role info of admin or super admin"),
    CANT_USER_ADMIN_ROLENAME("不能命名为管理员或超级管理员","you are not allowed to use the role name of admin"),
    CANT_CREATE_ADMIN_ROLES("不能创建超级管理员和管理员角色","you are not allowed to create the role of admin or super admin"),
    CANT_ASSIGN_SUPERADMIN_ROLE("无法分配超级管理员角色","you are not allowed to assign the role of super admin"),
    CANT_MODIFY_YOUR_ROLEINFO("无法修改自己的角色信息","you are not allowed to modify your own info"),
    CANT_MODIFY_SUPERADMIN_ROLEINFO("无法修改超级管理员的角色信息","you are not allowed to modify super admin role info"),
    CANT_ASSIGN_ROLE_OF_ADMIN("无权分配管理员角色","you are not allowed to assign admin role to users"),
    CANT_QUERY_OWN_ROLEINFO("无法查看自己的角色信息","you are not allowed to view your own role info"),
    CANT_QUERY_SUPERADMIN_ROLEINFO("无法查看超级管理员的角色信息","you are not allowed to view super admin role info"),
    CANT_MODIFY_YOUR_ACCOUNT_INFO("不能修改自己的账户信息","you are not allowed to modify your own account info"),
    CANT_MODIFY_SUPERADMIN_ACCOUNT_INFO("不能修改超级管理员的账户信息","you are not allowed to modify super admin account info"),
    CANT_DELETE_YOUR_ACCOUNT("不能删除自己的账号","you are not allowed to delete your account"),
    CANT_DELETE_SUPERADMIN_ACCOUNT("不能删除超级管理员的账号","you are not to delete super admin account"),
    TOKEN_FAIL("超时未操作，请重新登录","token validation failed, please login again"),
    NULL_AUTH("Authorization为空","you have lost your Authorization in your request header"),
    BLOCK_CHIAN_ERROR("区块链发生错误: ","blockchain occurs error: "),
    SM_ERROR("短信服务出错: ","short message service(SMS) occurs error: "),
    CANT_UPDATE_YOUR_STATUS("不能修改自己账户的状态","you can't freeze or unfreeze your account"),
    CANT_UPDATE_SUPERADMIN_STATUS("不能修改超级管理员的状态","you can't freeze or unfreeze super admin's account"),
    USER_AND_CONTACT_NOT_FOUNT("该用户的账号信息和联系方式可能不匹配，请确认预留联系方式是否正确","the contact of this user is inconsistent with the contact you set when you create your account, or you have to contact administrator to reset your contact"),
    UPDATE_USER_INFO_FAIL("修改用户信息失败","fail to update users info"),
    WRONG_CONTACT("该用户的联系方式不是手机号","the contact of this user is not the format of phone number"),
    MONEY_NOT_ENOUGH("资金数量不足","Insufficient captail"),
    DUPLICATE_USERNAME("用户名重复","username already exists"),
    DUPLICATE_CONTACT("联系方式重复","contact number already exists"),
    CANT_DELETE_ADMIN_ACCOUNT("不能删除同级别的管理员用户","you are not allowed to delete account of admin since you are the role of admin"),
    REDIS_DELETE_UID("redis删除时必须传入uid信息","lost uid while deleting the cache in redis"),
    CANT_UPDATE_ADMIN_STATUS("不能修改同级别的管理员用户的状态","you are not allowed to update the status of admin since you are the role of admin"),
    CANT_ASSIGN_ADMIN_ROLE("不能分配同级别的管理员用户的角色信息","you are not allowed to assign roles for admin since you are the role of admin"),
    CANT_UPDATE_ADMIN_ACCOUNT("不能修改同级别的管理员用户","you are not allowed to update the information of admin account since you are the role of admin"),
    NO_AUTH("用户无权操作","users have no authority to operate"),
    TOKEN_TIMEOUT("token信息超时，请重新登录","token is now unavailable, please login again"),
    DUPLICATE_ROLENAME("角色名重复了","role name already exists"),
    UPDATE_NOTIC_FAIL("更新公告内容失败","fail to update the announcement content"),
    UPDATE_NOTIC_LIST_FAIL("更新公告列表失败","fail to update the announcement list"),
    DELETE_NOTIC_FAIL("公告删除失败","fail to delete the announcement content"),
    UPDATE_NOTICE_TOP_FAIL("更新置顶状态失败","fail to promote the announcement to top"),
    UPDATE_NOTICE_SHOW_FAIL("更新显示状态失败","fail to update the status of the announcement's display"),
    GOLDE_CODES_NUM_INCONSISTENT("黄金编号的数量和提取的份数不一致","the number of the gold you withdraw is inconsistent with the number of golden codes"),
    WRONG_WITHDRAW_TIME("提取黄金时间必须大于当前时间","the time you withdraw gold must be greater than the current time"),
    GOLD_ORDER_IN_TO_WITHDRAW("该黄金提取单还未处于待提取状态","gold order in the status TO_WITHDRAW"),
    GOLD_ORDER_EXPIRE("您的黄金提取单已经过期","gold order has expired"),
    USER_DELETED("该用户已被删除","the user has been deleted"),
    INPUT_ESSENTIAL_INFOS("请填写必要的参数信息","please enter essential parameter"),
    APPLY_ORDER_NOT_EXIST("该申请单不存在","this application does not exist"),
    APPLY_ORDER_FINISHED("该申请单已经审核过了","this application has already been audited"),
    UPDATE_STATUS_FAIL("更新状态失败","fail to update the status"),
    AT_LEAST_ONE_PARAMETER("播放顺序和播放间隔参数必须至少设置一个","either play interval or order should be set"),
    UPDATE_BASIC_CONFIG_FAIL("更新基本配置失败","fail to update basic configuration"),
    FIRST_BASIC_CONFIG("初次设置参数，播放顺序和播放间隔参数必须都设置","both parameters of display play interval and display order must be set at first time"),
    UPLOAD_NUM_INCONSISTENT("文件上传数量与实际不一致","the number of files uploaded is inconsistent with real amount number"),
    NULL_PARAMETER("参数不能全为空","parameters should not all be empty or null"),
    APPLY_NOT_IN_TO_CHECK("该申请信息不为待审核状态","this application status is not in status of 'TO_CHECK'"),
    UPDATE_USER_APPLY_INFO_FAIL("用户申请信息更新失败","fail to update the application information of front user"),
    APPLY_NOT_IN_FIRST_PASS("该申请信息不为初审通过状态","this application status is not in status of 'FIRST_PASS'"),
    UPDATE_GOLD_TABLE_FAIL("平台总黄金表更新失败","fail to update gold table information"),
    UPDATE_USDG_TABLE_FAIL("平台总usdg表更新失败","fail to update usdg table information"),
    PLATFORM_USDG_ACCOUNT_LOST("平台usdg账户信息丢失","USDG account information is missing"),
    UPDATE_USDG_ACCOUNT_FAIL("平台usdg账户信息更新失败","fail to update the platform account information of USDG"),
    UPDATE_USER_ACCOUNT_FAIL("用户账户信息更新失败","fail to update the account information of front user"),
    PRIVATEKEY_MISSING("该用户合约公私钥不存在","user's private key and public key are missing"),
    SET_PRICE_NETWORK_ERROR("设置最新市价网络出错","network occurs error while setting market price"),
    GET_PRICE_NETWORK_ERROR("获取最新市价网络出错","network occurs error while getting market price"),
    ORDER_NOT_IN_AUDIT("黄金提取申请单不为后台审核状态","gold withdraw order is not in the state of 'TO_AUDIT'"),
    ORDER_NOT_IN_APPLY("黄金提取申请单不为申请中状态","gold withdraw order is not in the state of 'APPLY'"),
    FAIL_CREATE_ADDRESS("通过uid创建BTY/USDG地址失败: ","fail to create BTY/USDG address by uid: "),
    FAIL_WITHDRAW_COIN("出币失败: ","fail to withdraw coin: "),
    FAIL_REWITHDRAW_COIN("重试出币失败: ","fail to re-withdraw coin: "),

    UPDATE_PROBLEM_FAIL("更新常见内容失败","fail to update the common problem content"),
    UPDATE_PROBLEM_LIST_FAIL("更新常见问题列表失败","fail to update the common problem list"),
    DELETE_PROBLEM_FAIL("常见问题删除失败","fail to delete the common problem content"),

    CNY_SGD_ASK_BID_ERROR("人民币兑新加坡元的卖出价必须大于买入价","the CNY/SGD ask must be greater than CNY/SGD bid"),
    SGD_USD_ASK_BID_ERROR("新加坡元兑美元的卖出价必须大于买入价","the SGD/USD ask must be greater than SGD/USD bid"),

    DUPLICATE_GOLD_CODE("黄金编号重复","duplicate gold code"),

    UPDATE_COINFEE_FAIL("更新币提取手续费失败","fail to update coin withdraw fee"),
    UPDATE_GOLDFEE_FAIL("更新黄金提取手续费失败","fail to update gold withdraw fee"),

    DUPLICATE_PHONE("手机号重复","more than one phone numbers were found"),
    DUPLICATE_EMAIL("邮箱重复","more than one emails were found"),
    ERROR_BITADDRESS_FORMAT("提币地址格式错误","wrong withdraw address format"),







            ;

    private String messageCN;
    private String messageEN;
    private ErrorMsg(String messageCN,String messageEN){
        this.messageCN = messageCN;
        this.messageEN = messageEN;
    }

}
