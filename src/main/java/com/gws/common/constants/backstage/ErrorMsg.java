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
    WRONG_FORMAT("与指定格式不符","you have input wrong data format"),
    CAST_STRING_EXCEPTION("强转String异常","cast to String type occurs exception"),
    EMPTY_STRING("字符串不能为空","string can not be empty"),
    CAST_INTEGER_EXCEPTION("强转Inetegr异常","cast to integer type occurs exception"),
    CAST_LONG_EXCEPTION("强转Long异常","cast to long type occurs exception"),
    NULL_INTEGER("integer为空","integer can not be null"),


    INPUT_GOLD_CODE("请输入黄金编号","please input gold codes"),
    WRONG_PASSWORD("原始密码输入错误","wrong original password"),
    PASSWORD_INCONSISTENT("两次输入的密码不一致","new password and confirmed password is inconsistent"),
    SAME_PASSWORD("新密码不能和原密码相同","new password and original password is the same"),
    CANT_ASSIGN_AUTH_FOR_ADMINS("不能给管理员或超级管理员分配权限信息","you are not allowed to assign any authorities for admin or super admin"),
    CANT_ASSIGN_CORE_AUTHES("不能分配核心权限","you are not allowed to assign any CORE authorities for users"),
    CANT_QUERY_SUPERADMIN_AUTHES("无法查看超级管理员的权限信息","you are not allowed to query super admin's authority info"),
    INPUT_DELETED_ROLEID("请输入要删除的角色id","please enter roleId that you want to delete"),
    CANT_DELETE_ADMIN_ROLES("不能删除管理员和超级管理员角色","you are not allowed to delete the role of admin or super admin"),
    CANT_MODIFY_ADMIN_ROLES("不能修改管理员角色信息","you are not allowed to modify the role info of admin or super admin"),
    CANT_USER_ADMIN_ROLENAME("不能命名为管理员或超级管理员","you are not allowed to use the role name of admin"),
    CANT_CREATE_ADMIN_ROLES("不能创建超级管理员和管理员角色","you are not allowed to create the role of admin or super admin"),
    CANT_ASSIGN_SUPERADMIN_ROLE("无法分配超级管理员角色","you are not allowed to assign the role of super admin"),
    CANT_MODIFY_YOUR_ROLEINFO("无法修改自己的角色信息","you are not allowed to modify your own info"),
    CANT_MODIFY_SUPERADMIN_ROLEINFO("无法修改超级管理员的角色信息","you are not allowed to modify super admin role info"),
    CANT_ASSIGN_ROLE_OF_ADMIN("无权分配管理员角色","you are not allowed to assign admin role for users"),
    CANT_QUERY_OWN_ROLEINFO("无法查看自己的角色信息","you are not allowed to query your own role info"),
    CANT_QUERY_SUPERADMIN_ROLEINFO("无法查看超级管理员的角色信息","you are not allowed to query super admin role info"),
    CANT_MODIFY_YOUR_ACCOUNT_INFO("不能修改自己的账户信息","you are not allowed to modify your own account info"),
    CANT_MODIFY_SUPERADMIN_ACCOUNT_INFO("不能修改超级管理员的账户信息","you are not allowed to modify super admin account info"),
    CANT_DELETE_YOUR_ACCOUNT("不能删除自己的账号","you are not allowed to delete your account"),
    CANT_DELETE_SUPERADMIN_ACCOUNT("不能删除超级管理员的账号","you are not allowed to delete super admin account"),
    TOKEN_FAIL("TOKEN验证失败，请重新失败","token validation fail, please login again"),
    NULL_AUTH("Authorization为空","you have lost your Authorization in your request header"),
    BLOCK_CHIAN_ERROR("区块链发生错误: ","blockChain occurs error: "),
    SM_ERROR("短信服务出错: ","short message service occurs error: "),
    CANT_UPDATE_YOUR_STATUS("不能修改自己账户的状态","you can't freeze or unfreeze your account"),
    CANT_UPDATE_SUPERADMIN_STATUS("不能修改超级管理员的状态","you can't freeze or unfreeze super admin's account"),
    USER_AND_CONTACT_NOT_FOUNT("该用户的账号信息和联系方式可能不匹配，请确认预留联系方式是否正确","the contact of this user is inconsistent with the contact you set when you create your account, or you have to contact administrator to reset your contact"),

    UPDATE_USER_INFO_FAIL("修改用户信息失败","fail to update users info"),
    WRONG_CONTACT("该用户的联系方式不是手机号","the contact of this user is not the format of phone number"),
    MONEY_NOT_ENOUGH("资金数量不足","the leftover money is not enough"),






    ;

    private String messageCN;
    private String messageEN;
    private ErrorMsg(String messageCN,String messageEN){
        this.messageCN = messageCN;
        this.messageEN = messageEN;
    }

}
