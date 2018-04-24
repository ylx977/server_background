package com.gws.services.backstage;

import com.gws.dto.backstage.BackAuthgroupsVO;
import com.gws.dto.backstage.PageDTO;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
public interface BackUserService {

    /**
     * 检查用户权限的方法
     * @param uid 用户id
     * @param authUrl 权限的url地址
     * @return UserDetailDTO
     */
    UserDetailDTO checkUserAuthority(Long uid, String authUrl);

    /**
     * 检查用户的token信息是否正确
     * @param uid 用户id
     * @param token 令牌
     * @return boolean
     */
    boolean verificationToken(Long uid, String token);

    /**
     * 创建后台新用户
     * @param backUser 用户对象信息
     */
    void saveUser(BackUserBO backUser);

    /**
     * 查询后台用户信息给前端显示
     * @param backUserBO 前端传过来的参数对象
     * @return
     */
    PageDTO queryUserInfo(BackUserBO backUserBO);

    /**
     * 逻辑删除一堆用户
     * @param backUserBO
     */
    void deleteUsers(BackUserBO backUserBO);

    /**
     * 修改用户基本信息
     * @param backUserBO
     */
    void updateUsers(BackUserBO backUserBO);

    /**
     * 查询管理员是否存在
     * @return
     */
    BackUser queryAdmin();

    /**
     * 平台初始化的时候创建超级管理员
     * @param username 管理员用户名
     * @param password 管理员的密码
     * @param token 管理员的token信息
     * @return
     */
    UserDetailDTO createAdmin(String username, String password, String token);

    /**
     * 通过用户名和密码查询用户详细信息
     * @param username 用户名
     * @param password 密码
     * @return
     */
    UserDetailDTO queryUserByNameAndPwd(String username, String password);

    /**
     * 用户初次登录，插入token
     * @param uid 用户uid
     * @param token token值
     */
    void addToken(Long uid, String token);

    /**
     * 用户再次登录，更新token信息
     * @param uid 用户id
     * @param token 用户token信息
     */
    void updateToken(Long uid, String token);

    /**
     * 查询用户下的角色信息
     * @param backUserBO
     * @return
     */
    List<BackAuthgroupsVO> showRoleInfoUnderAccount(BackUserBO backUserBO);

    /**
     * 为用户分配角色
     * @param backUserBO
     */
    void assignRoles4Account(BackUserBO backUserBO);

    /**
     * 用户中心，用户修改密码
     * @param backUserBO
     */
    void modifyPassword(BackUserBO backUserBO);
}
