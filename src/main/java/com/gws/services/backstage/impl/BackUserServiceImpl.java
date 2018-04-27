package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.BackAuthesEnum;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.common.constants.backstage.Roles;
import com.gws.dto.backstage.BackAuthgroupsVO;
import com.gws.dto.backstage.PageDTO;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.master.backstage.*;
import com.gws.repositories.query.backstage.*;
import com.gws.repositories.slave.backstage.*;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.redis.RedisUtil;
import org.hibernate.internal.QueryImpl;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Service
public class BackUserServiceImpl implements BackUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackUserServiceImpl.class);

    private final BackUserMaster backUserMaster;

    private final BackUserSlave backUserSlave;

    private final BackUsersAuthgroupsMaster backUsersAuthgroupsMaster;

    private final BackUsersAuthgroupsSlave backUsersAuthgroupsSlave;

    private final BackAuthgroupsMaster backAuthgroupsMaster;

    private final BackAuthgroupsSlave backAuthgroupsSlave;

    private final BackAuthgroupsAuthesMaster backAuthgroupsAuthesMaster;

    private final BackAuthgroupsAuthesSlave backAuthgroupsAuthesSlave;

    private final BackAuthesMaster backAuthesMaster;

    private final BackAuthesSlave backAuthesSlave;

    private final BackUserTokenMaster backUserTokenMaster;

    private final BackUserTokenSlave backUserTokenSlave;

    private final IdGlobalGenerator idGlobalGenerator;

    private final RedisTemplate<Object,Object> redisTemplate;

    private final RedisUtil redisUtil;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BackUserServiceImpl(BackUserMaster backUserMaster, BackUserSlave backUserSlave, BackUsersAuthgroupsMaster backUsersAuthgroupsMaster,
                               BackUsersAuthgroupsSlave backUsersAuthgroupsSlave, BackAuthgroupsMaster backAuthgroupsMaster, BackAuthgroupsSlave backAuthgroupsSlave,
                               BackAuthgroupsAuthesMaster backAuthgroupsAuthesMaster, BackAuthgroupsAuthesSlave backAuthgroupsAuthesSlave,
                               BackAuthesMaster backAuthesMaster, BackAuthesSlave backAuthesSlave, BackUserTokenMaster backUserTokenMaster, BackUserTokenSlave backUserTokenSlave, IdGlobalGenerator idGlobalGenerator, RedisTemplate<Object, Object> redisTemplate, RedisUtil redisUtil) {
        this.backUserMaster = backUserMaster;
        this.backUserSlave = backUserSlave;
        this.backUsersAuthgroupsMaster = backUsersAuthgroupsMaster;
        this.backUsersAuthgroupsSlave = backUsersAuthgroupsSlave;
        this.backAuthgroupsMaster = backAuthgroupsMaster;
        this.backAuthgroupsSlave = backAuthgroupsSlave;
        this.backAuthgroupsAuthesMaster = backAuthgroupsAuthesMaster;
        this.backAuthgroupsAuthesSlave = backAuthgroupsAuthesSlave;
        this.backAuthesMaster = backAuthesMaster;
        this.backAuthesSlave = backAuthesSlave;
        this.backUserTokenMaster = backUserTokenMaster;
        this.backUserTokenSlave = backUserTokenSlave;
        this.idGlobalGenerator = idGlobalGenerator;
        this.redisTemplate = redisTemplate;
        this.redisUtil = redisUtil;
    }

    /**
     * 获取用户详细信息的通用方法
     * @param uid
     * @param authUrl
     * @return
     */
    private UserDetailDTO getUserAuthority(Long uid, String authUrl){
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BackUser backUser = backUserSlave.findOne(uid);
        BeanUtils.copyProperties(backUser,userDetailDTO);
        BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
        backUsersAuthgroupsQuery.setUid(backUser.getUid());
        //查询uid对应的所有authgroupId
        List<BackUsersAuthgroups> backUsersAuthgroupsList = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
        if(backUsersAuthgroupsList.size() == 0){
            throw new RuntimeException("用户无权操作");
        }
        List<Long> authgroupIds = new ArrayList<>();
        for (BackUsersAuthgroups backUsersAuthgroups : backUsersAuthgroupsList){
            authgroupIds.add(backUsersAuthgroups.getAuthgroupId());
        }
        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupIds(authgroupIds);
        List<BackAuthgroups> backAuthgroupsList = backAuthgroupsSlave.findAll(backAuthgroupsQuery);
        //用户的所有权限组集合
        List<String> authgroupNames = new ArrayList<>();
        for (BackAuthgroups backAuthgroups : backAuthgroupsList) {
            authgroupNames.add(backAuthgroups.getAuthgroupName());
        }
        //将权限组信息插入到userDetailDTO
        userDetailDTO.setRoleName(authgroupNames);
        BackAuthgroupsAuthesQuery backAuthgroupsAuthesQuery = new BackAuthgroupsAuthesQuery();
        backAuthgroupsAuthesQuery.setAuthgroupIds(authgroupIds);
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesList = backAuthgroupsAuthesSlave.findAll(backAuthgroupsAuthesQuery);
        if(backAuthgroupsAuthesList.size() == 0){
            throw new RuntimeException("用户无权操作");
        }
        List<Long> authIds = new ArrayList<>();
        for (BackAuthgroupsAuthes backAuthgroupsAuthes: backAuthgroupsAuthesList) {
            authIds.add(backAuthgroupsAuthes.getAuthId());
        }
        BackAuthesQuery backAuthesQuery = new BackAuthesQuery();
        backAuthesQuery.setAuthIds(authIds);
        List<BackAuthes> backAuthesList = backAuthesSlave.findAll(backAuthesQuery);
        //用户的所有权限url集合
        List<String> authUrls = new ArrayList<>();
        for (BackAuthes backAuthes : backAuthesList) {
            authUrls.add(backAuthes.getAuthUrl());
        }
        userDetailDTO.setAuthUrl(authUrls);
        if(!authUrls.contains(authUrl)){
            throw new RuntimeException("用户无权操作");
        }
        return userDetailDTO;
    }

    /**
     * 当更改用户信息后，要删除用户在redis的详细信息
     * 支持批量的用户redis缓存信息删除
     */
    private void deleteUserRredisInfo(Long... uids){
        if(uids.length == 0){
            throw new RuntimeException("redis删除时必须传入uid信息");
        }
        for(long uid : uids){
            //去redis将用户的信息删除掉
            redisUtil.delete(RedisConfig.USER_AUTH_PREFIX + uid);
            redisUtil.delete(RedisConfig.USER_TOKEN_PREFIX + uid);
            //待续.......
        }
    }

    @Override
    public UserDetailDTO checkUserAuthority(Long uid, String authUrl) {
        String redisAuthority;
        try {
            redisAuthority = (String) redisTemplate.opsForValue().get(RedisConfig.USER_AUTH_PREFIX + uid);
        } catch (Exception e) {
            LOGGER.error("用户:{},redis获取用户权限信息异常,去数据库获取信息",uid);
            return getUserAuthority(uid,authUrl);
        }

        if(StringUtils.isEmpty(redisAuthority)){
            //如果redis获取信息为空，去数据库获取
            LOGGER.info("用户:{},去数据库获取用户权限信息，并存入redis",uid);
            UserDetailDTO userDetailDTO = getUserAuthority(uid,authUrl);
            redisTemplate.opsForValue().set(RedisConfig.USER_AUTH_PREFIX + uid, JSON.toJSONString(userDetailDTO));
            return userDetailDTO;
        }else{
            LOGGER.info("用户:{},从redis获取用户权限信息",uid);
            UserDetailDTO userDetailDTO = JSON.parseObject(redisAuthority, UserDetailDTO.class);
            List<String> authUrls = userDetailDTO.getAuthUrl();
            if(!authUrls.contains(authUrl)){
                throw new RuntimeException("用户无权操作");
            }
            return userDetailDTO;
        }
    }

    @Override
    public boolean verificationToken(Long uid, String token) {
        String redisToken;
        try {
            redisToken = (String) redisTemplate.opsForValue().get(RedisConfig.USER_TOKEN_PREFIX +uid);
        } catch (Exception e) {
            //如果redis连接有问题，那么就去数据库操作
            BackUserToken backUserToken = backUserTokenSlave.findOne(uid);
            System.out.println("去数据库获取token信息："+backUserToken);
            //查不到token信息返回false
            if(backUserToken==null){
                return false;
            }
            String userToken = backUserToken.getToken();
            //数据库中的token为空，返回false
            if(userToken==null||"".equals(userToken.trim())){
                return false;
            }
            //已有的token和用户传过来的token不一致返回false
            if(!userToken.equals(token)){
                return false;
            }
            return true;
        }
        System.out.println("去redis获取tokne信息："+redisToken);
        if(StringUtils.isEmpty(redisToken)){
            throw new RuntimeException("token信息超时，请重新登录");
        }
        if(!redisToken.equals(token)){
            return false;
        }
        return true;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(BackUserBO backUserBO) {
        BackUsersQuery backUsersQuery = new BackUsersQuery();
        backUsersQuery.setUsername(backUserBO.getUsername());
        //在非删除用户中选择
        backUsersQuery.setIsDelete(1);
        //查询用户名是否重复
        List<BackUser> backUserList1 = backUserSlave.findAll(backUsersQuery);
        if(backUserList1.size() > 0){
            throw new RuntimeException("用户名重复");
        }
        backUsersQuery.setUsername(null);
        backUsersQuery.setContact(backUserBO.getContact());
        //查询联系方式是否重名
        List<BackUser> backUserList2 = backUserSlave.findAll(backUsersQuery);
        if(backUserList2.size() > 0){
            throw new RuntimeException("联系方式重复");
        }
        BackUser backUser = new BackUser();
        BeanUtils.copyProperties(backUserBO,backUser);
        backUser.setUid(idGlobalGenerator.getSeqId(BackUser.class));
        backUser.setIsDelete(1);
        backUserMaster.save(backUser);
    }

    @Override
    public PageDTO queryUserInfo(BackUserBO backUserBO) {

        Long uid = backUserBO.getOperatorUid();

        BackUsersQuery backUsersQuery = new BackUsersQuery();
        if(!StringUtils.isEmpty(backUserBO.getUsername())){
            backUsersQuery.setUsernameLike(backUserBO.getUsername());
        }
        backUsersQuery.setIsDelete(1);
        if(uid.equals(1L)){
            //如果是超级管理员除了自己的信息不能看到其他人都能看到
            Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id not in (1)");
            List<Long> resultList = (List<Long>) query.getResultList();
            backUsersQuery.setUids(resultList);
        }else{
            //如果是普通管理员就不能看到自己和其它管理员的信息
            Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id not in (1,2)");
            List<Long> resultList = (List<Long>) query.getResultList();
            backUsersQuery.setUids(resultList);
        }
        backUsersQuery.setCstartTime(backUserBO.getStartTime());
        backUsersQuery.setCendTime(backUserBO.getEndTime());
        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(backUserBO.getPage()-1,backUserBO.getRowNum(),sort);
        Page<BackUser> backUserPage = backUserSlave.findAll(backUsersQuery, pageable);

        List<BackUser> list = backUserPage == null ? Collections.emptyList() : backUserPage.getContent();
        long totalPage = backUserPage == null ? 0 : backUserPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(BackUserBO backUserBO) {
        //操作人uid
        Long operatorUid = backUserBO.getOperatorUid();

        //被删除的uids
        List<Long> uids = backUserBO.getUids();

        if(!operatorUid.equals(1L)){
            //如果是超级管理员除了自己的信息不能看到其他人都能看到
            Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id in (1,2)");
            List<Long> resultList = (List<Long>) query.getResultList();
            for(Long uid : resultList){
                if(uids.contains(uid)){
                    throw new RuntimeException("不能删除同级别的管理员用户");
                }
            }
        }

        BackUsersQuery backUsersQuery = new BackUsersQuery();
        backUsersQuery.setUids(uids);
        System.out.println(uids);
        BackUser backUser = new BackUser();
        backUser.setIsDelete(0);
        backUserMaster.update(backUser, backUsersQuery, "isDelete");

        deleteUserRredisInfo(uids.toArray(new Long[0]));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUsers(BackUserBO backUserBO) {

        //当前操作人的uid
        Long operatorUid = backUserBO.getOperatorUid();

        //被修改用户的uid
        long uid = backUserBO.getUid();

        if(!operatorUid.equals(1L)){
            //如果是超级管理员除了自己的信息不能看到其他人都能看到
            Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id in (1,2)");
            List<Long> resultList = (List<Long>) query.getResultList();
            if(resultList.contains(uid)){
                throw new RuntimeException("不能修改同级别的管理员用户");
            }
        }

        BackUsersQuery backUsersQuery = new BackUsersQuery();
        backUsersQuery.setUsername(backUserBO.getUsername());
        //和被删除用户的名字也不能相同
        backUsersQuery.setUidNotIn(uid);
        //查询用户名是否重复
        List<BackUser> backUserList1 = backUserSlave.findAll(backUsersQuery);
        if(backUserList1.size() > 0){
            throw new RuntimeException("用户名重复");
        }
        backUsersQuery.setUsername(null);
        backUsersQuery.setContact(backUserBO.getContact());
        //查询联系方式是否重名(排除被修改者本身)
        List<BackUser> backUserList2 = backUserSlave.findAll(backUsersQuery);
        if(backUserList2.size() > 0){
            throw new RuntimeException("联系方式重复");
        }
        BackUser backUser = new BackUser();
        BeanUtils.copyProperties(backUserBO,backUser);
        backUserMaster.updateById(backUser,uid,"username","password","contact","personName");

        //删除redis缓存
        deleteUserRredisInfo(uid);
    }

    @Override
    public BackUser queryAdmin() {
        BackUser admin = backUserSlave.findOne(1L);
        return admin;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetailDTO createAdmin(String username, String password, String token) {
        //创建超级管理员账号
        BackUser backUser = new BackUser();
        //超级管理员的id为1
        backUser.setUid(1L);
        backUser.setUsername(username);
        backUser.setPassword(password);
        backUser.setPersonName(Roles.SUPER_ADMIN);
        backUser.setContact("-");
        backUser.setIsDelete(1);
        backUserMaster.save(backUser);

        //生成管理员对应的token信息
        BackUserToken backUserToken = new BackUserToken();
        backUserToken.setUid(1L);
        backUserToken.setToken(token);
        backUserTokenMaster.save(backUserToken);

        //插入用户权限组关联表
        BackUsersAuthgroups backUsersAuthgroups = new BackUsersAuthgroups();
        backUsersAuthgroups.setId(idGlobalGenerator.getSeqId(BackUsersAuthgroups.class));
        backUsersAuthgroups.setUid(1L);
        backUsersAuthgroups.setAuthgroupId(1L);
        backUsersAuthgroupsMaster.save(backUsersAuthgroups);

        //生成超级管理员和普通管理员这两个权限组(角色)
        List<BackAuthgroups> backAuthgroupsList = new ArrayList<>();
        BackAuthgroups backAuthgroups1 = new BackAuthgroups();
        backAuthgroups1.setAuthgroupId(1L);
        backAuthgroups1.setAuthgroupName(Roles.SUPER_ADMIN);
        backAuthgroupsList.add(backAuthgroups1);
        BackAuthgroups backAuthgroups2 = new BackAuthgroups();
        backAuthgroups2.setAuthgroupId(2L);
        backAuthgroups2.setAuthgroupName(Roles.ADMIN);
        backAuthgroupsList.add(backAuthgroups2);
        backAuthgroupsMaster.save(backAuthgroupsList);

        //插入权限组权限关联表【给超级管理员建立关联】
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesList1 = new ArrayList<>();
        for (BackAuthesEnum backAuthesEnum : BackAuthesEnum.values()) {
            BackAuthgroupsAuthes backAuthgroupsAuthes = new BackAuthgroupsAuthes();
            backAuthgroupsAuthes.setId(idGlobalGenerator.getSeqId(BackAuthgroupsAuthes.class));
            backAuthgroupsAuthes.setAuthgroupId(1L);
            backAuthgroupsAuthes.setAuthId(backAuthesEnum.getAuthId());
            backAuthgroupsAuthesList1.add(backAuthgroupsAuthes);
        }
        backAuthgroupsAuthesMaster.save(backAuthgroupsAuthesList1);

        //插入权限组权限关联表【给普通管理员建立关联】
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesList2 = new ArrayList<>();
        for (Long authId : BackAuthesEnum.ADMIN_AUTH_INDEX) {
            BackAuthgroupsAuthes backAuthgroupsAuthes = new BackAuthgroupsAuthes();
            backAuthgroupsAuthes.setId(idGlobalGenerator.getSeqId(BackAuthgroupsAuthes.class));
            backAuthgroupsAuthes.setAuthgroupId(2L);
            backAuthgroupsAuthes.setAuthId(authId);
            backAuthgroupsAuthesList2.add(backAuthgroupsAuthes);
        }
        backAuthgroupsAuthesMaster.save(backAuthgroupsAuthesList2);

        //插入所有权限信息
        List<BackAuthes> backAuthesList = new ArrayList<>();
        for (BackAuthesEnum backAuthesEnum : BackAuthesEnum.values()) {
            BackAuthes backAuthes = new BackAuthes();
            backAuthes.setAuthId(backAuthesEnum.getAuthId());
            backAuthes.setAuthName(backAuthesEnum.getAuthName());
            backAuthes.setAuthUrl(backAuthesEnum.getAuthUrl());
            backAuthesList.add(backAuthes);
        }
        backAuthesMaster.save(backAuthesList);

        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(backUser,userDetailDTO);
        userDetailDTO.setIfFirstLogin(true);
        userDetailDTO.setRoleName(Arrays.asList(Roles.SUPER_ADMIN));
        userDetailDTO.setAuthUrl(BackAuthesEnum.AUTH_URLS);
        userDetailDTO.setAuthName(BackAuthesEnum.AUTH_NAMES);
        userDetailDTO.setTokenId("Bearer"+token+"&"+1);
        return userDetailDTO;
    }

    @Override
    public UserDetailDTO queryUserByNameAndPwd(String username, String password) {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BackUsersQuery backUsersQuery = new BackUsersQuery();
        backUsersQuery.setUsername(username);
        backUsersQuery.setPassword(password);
        BackUser backUser = backUserSlave.findOne(backUsersQuery);
        //如果查无此账号，直接返回空
        if(backUser==null){
            return null;
        }

        //查询用户的token信息
        BackUserToken backUserToken = backUserTokenSlave.findOne(backUser.getUid());
        if(backUserToken!=null){
            userDetailDTO.setToken(backUserToken.getToken());
        }
        //复制参数到userDetailDTO
        BeanUtils.copyProperties(backUser,userDetailDTO);

        //【用户权限组中间表】查询对象
        BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
        backUsersAuthgroupsQuery.setUid(backUser.getUid());
        //查询uid对应的所有authgroupId
        List<BackUsersAuthgroups> backUsersAuthgroupsList = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
        if(backUsersAuthgroupsList.size() == 0){
            userDetailDTO.setRoleName(Collections.emptyList());
            userDetailDTO.setAuthName(Collections.emptyList());
            userDetailDTO.setAuthUrl(Collections.emptyList());
            return userDetailDTO;
        }
        List<Long> authgroupIds = new ArrayList<>();
        for (BackUsersAuthgroups backUsersAuthgroups : backUsersAuthgroupsList){
            authgroupIds.add(backUsersAuthgroups.getAuthgroupId());
        }

        //查询权限组id
        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupIds(authgroupIds);
        List<BackAuthgroups> backAuthgroupsList = backAuthgroupsSlave.findAll(backAuthgroupsQuery);
        //用户的所有权限组集合
        List<String> authgroupNames = new ArrayList<>();
        for (BackAuthgroups backAuthgroups : backAuthgroupsList) {
            authgroupNames.add(backAuthgroups.getAuthgroupName());
        }
        userDetailDTO.setRoleName(authgroupNames);

        BackAuthgroupsAuthesQuery backAuthgroupsAuthesQuery = new BackAuthgroupsAuthesQuery();
        backAuthgroupsAuthesQuery.setAuthgroupIds(authgroupIds);
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesList = backAuthgroupsAuthesSlave.findAll(backAuthgroupsAuthesQuery);
        //如果权限组权限关联表查不到任何信息，则直接返回
        if(backAuthgroupsAuthesList.size() == 0){
            userDetailDTO.setAuthName(Collections.emptyList());
            userDetailDTO.setAuthUrl(Collections.emptyList());
            return userDetailDTO;
        }
        List<Long> authIds = new ArrayList<>();
        for (BackAuthgroupsAuthes backAuthgroupsAuthes: backAuthgroupsAuthesList) {
            authIds.add(backAuthgroupsAuthes.getAuthId());
        }
        BackAuthesQuery backAuthesQuery = new BackAuthesQuery();
        backAuthesQuery.setAuthIds(authIds);
        List<BackAuthes> backAuthesList = backAuthesSlave.findAll(backAuthesQuery);
        //用户的所有权限url集合
        List<String> authUrls = new ArrayList<>();
        for (BackAuthes backAuthes : backAuthesList) {
            authUrls.add(backAuthes.getAuthUrl());
        }
        userDetailDTO.setAuthUrl(authUrls);
        return userDetailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToken(Long uid, String token) {
        BackUserToken backUserToken = new BackUserToken();
        backUserToken.setUid(uid);
        backUserToken.setToken(token);
        backUserTokenMaster.save(backUserToken);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateToken(Long uid, String token) {
        Integer currentTime = (int)(System.currentTimeMillis()/1000L);
        BackUserToken backUserToken = new BackUserToken();
        backUserToken.setToken(token);
        backUserToken.setUtime(currentTime);
        backUserTokenMaster.updateById(backUserToken,uid,"token","utime");
    }

    @Override
    public List<BackAuthgroupsVO> showRoleInfoUnderAccount(BackUserBO backUserBO) {
        List<BackAuthgroups> allBackAuthgroups = backAuthgroupsSlave.findAll();
        if(allBackAuthgroups.size() == 0){
            return Collections.emptyList();
        }
        BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
        backUsersAuthgroupsQuery.setUid(backUserBO.getUid());
        List<BackUsersAuthgroups> backUsersAuthgroupsList = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
        //如果都没有选中，则返回均未选中
        if(backUsersAuthgroupsList.size() == 0){
            List<BackAuthgroupsVO> backAuthgroupsVOList = new ArrayList<>();
            for (BackAuthgroups backAuthgroups : allBackAuthgroups) {
                BackAuthgroupsVO backAuthgroupsVO = new BackAuthgroupsVO();
                Long authgroupId = backAuthgroups.getAuthgroupId();
                String authgroupName = backAuthgroups.getAuthgroupName();
                backAuthgroupsVO.setAuthgroupId(authgroupId);
                backAuthgroupsVO.setAuthgroupName(authgroupName);
                backAuthgroupsVO.setIsSelected(0);
                backAuthgroupsVOList.add(backAuthgroupsVO);
            }
            return backAuthgroupsVOList;
        }
        List<Long> authgroupsIds = new ArrayList<>();
        for (BackUsersAuthgroups backUsersAuthgroup : backUsersAuthgroupsList) {
            authgroupsIds.add(backUsersAuthgroup.getAuthgroupId());
        }

        List<BackAuthgroupsVO> backAuthgroupsVOList = new ArrayList<>();
        for (BackAuthgroups backAuthgroups : allBackAuthgroups) {
            BackAuthgroupsVO backAuthgroupsVO = new BackAuthgroupsVO();
            Long authgroupId = backAuthgroups.getAuthgroupId();
            String authgroupName = backAuthgroups.getAuthgroupName();
            backAuthgroupsVO.setAuthgroupId(authgroupId);
            backAuthgroupsVO.setAuthgroupName(authgroupName);
            if(authgroupsIds.contains(authgroupId)){
                backAuthgroupsVO.setIsSelected(1);
            }else{
                backAuthgroupsVO.setIsSelected(0);
            }
            backAuthgroupsVOList.add(backAuthgroupsVO);
        }
        return backAuthgroupsVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles4Account(BackUserBO backUserBO) {

        //当前操作人的uid
        Long operatorUid = backUserBO.getOperatorUid();

        //被操作人的uid
        Long uid = backUserBO.getUid();

        if(!operatorUid.equals(1L)){
            //管理员同级之间不能相互修改
            Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id in (1,2)");
            List<Long> resultList = (List<Long>) query.getResultList();
            if(resultList.contains(uid)){
                throw new RuntimeException("不能分配同级别的管理员用户的角色信息");
            }
        }

        List<Long> authgroupIds = backUserBO.getAuthgroupIds();
        BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
        backUsersAuthgroupsQuery.setUid(uid);
        List<BackUsersAuthgroups> usersAuthgroupsInfos = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
        if(usersAuthgroupsInfos.size()!=0){
            //删除原始的关联关系
            backUsersAuthgroupsMaster.delete(usersAuthgroupsInfos);
        }
        if(authgroupIds.size() > 0){
            List<BackUsersAuthgroups> backUsersAuthgroupsList = new ArrayList<>();
            for(Long authgroupId: authgroupIds){
                BackUsersAuthgroups backUsersAuthgroups1 = new BackUsersAuthgroups();
                backUsersAuthgroups1.setId(idGlobalGenerator.getSeqId(BackUsersAuthgroups.class));
                backUsersAuthgroups1.setUid(uid);
                backUsersAuthgroups1.setAuthgroupId(authgroupId);
                backUsersAuthgroupsList.add(backUsersAuthgroups1);
            }
            backUsersAuthgroupsMaster.save(backUsersAuthgroupsList);
        }

        //删除用户的redis缓存信息
        deleteUserRredisInfo(uid);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPassword(BackUserBO backUserBO) {
        Long uid = backUserBO.getUid();
        String newPassword = backUserBO.getNewPassword();
        BackUser backUser = new BackUser();
        backUser.setPassword(newPassword);
        backUserMaster.updateById(backUser,uid,"password");
    }
}
