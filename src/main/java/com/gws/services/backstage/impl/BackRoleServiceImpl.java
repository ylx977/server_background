package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.RedisConfig;
import com.gws.dto.backstage.BackAuthesVO;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.master.backstage.*;
import com.gws.repositories.query.backstage.BackAuthgroupsAuthesQuery;
import com.gws.repositories.query.backstage.BackAuthgroupsQuery;
import com.gws.repositories.query.backstage.BackUsersAuthgroupsQuery;
import com.gws.repositories.slave.backstage.*;
import com.gws.services.backstage.BackRoleService;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Service
public class BackRoleServiceImpl implements BackRoleService {

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

    private final RedisUtil redisUtil;

    @Autowired
    public BackRoleServiceImpl(BackUserMaster backUserMaster, BackUserSlave backUserSlave, BackUsersAuthgroupsMaster backUsersAuthgroupsMaster,
                               BackUsersAuthgroupsSlave backUsersAuthgroupsSlave, BackAuthgroupsMaster backAuthgroupsMaster, BackAuthgroupsSlave backAuthgroupsSlave,
                               BackAuthgroupsAuthesMaster backAuthgroupsAuthesMaster, BackAuthgroupsAuthesSlave backAuthgroupsAuthesSlave,
                               BackAuthesMaster backAuthesMaster, BackAuthesSlave backAuthesSlave, BackUserTokenMaster backUserTokenMaster, BackUserTokenSlave backUserTokenSlave, IdGlobalGenerator idGlobalGenerator, RedisUtil redisUtil) {
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
        this.redisUtil = redisUtil;
    }

    /**
     * 当更改用户信息后，要删除用户在redis的详细信息
     * 支持批量的用户redis缓存信息删除
     */
    private void deleteUserRredisInfo(Long... uids){
        if(uids.length != 0){
            for(long uid : uids){
                //去redis将用户的信息删除掉
                redisUtil.delete(RedisConfig.USER_AUTH_PREFIX + uid);
                redisUtil.delete(RedisConfig.USER_TOKEN_PREFIX + uid);
                //待续.......
            }
        }else{
            System.out.println("不需要去redis删除用户的缓存信息");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(BackUserBO backUserBO) {
        BackAuthgroups backAuthgroups = new BackAuthgroups();
        backAuthgroups.setAuthgroupName(backUserBO.getRoleName());
        backAuthgroups.setAuthgroupId(idGlobalGenerator.getSeqId(BackAuthgroups.class));
        backAuthgroupsMaster.save(backAuthgroups);
    }

    @Override
    public PageDTO queryRoles(BackUserBO backUserBO) {
        Integer page = backUserBO.getPage();
        Integer rowNum = backUserBO.getRowNum();

        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        if(!StringUtils.isEmpty(backUserBO.getRoleName())){
            backAuthgroupsQuery.setAuthgroupNameLike(backUserBO.getRoleName());
        }
        backAuthgroupsQuery.setCstartTime(backUserBO.getStartTime());
        backAuthgroupsQuery.setCendTime(backUserBO.getEndTime());
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1, rowNum,sort);
        Page<BackAuthgroups> backAuthgroupsPage = backAuthgroupsSlave.findAll(backAuthgroupsQuery, pageable);
        List<BackAuthgroups> list = backAuthgroupsPage == null ? Collections.EMPTY_LIST : backAuthgroupsPage.getContent();
        long totalPage = backAuthgroupsPage == null ? 0 : backAuthgroupsPage.getTotalElements();
        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(BackUserBO backUserBO) {

        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupName(backUserBO.getRoleName());
        BackAuthgroups one = backAuthgroupsSlave.findOne(backAuthgroupsQuery);
        if(one != null){
            throw new RuntimeException("角色名重复了");
        }

        Long roleId = backUserBO.getRoleId();
        BackAuthgroups backAuthgroups = new BackAuthgroups();
        backAuthgroups.setAuthgroupName(backUserBO.getRoleName());
        backAuthgroupsMaster.updateById(backAuthgroups,roleId,"authgroupName");

        BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
        backUsersAuthgroupsQuery.setAuthgroupId(roleId);
        List<BackUsersAuthgroups> backUsersAuthgroupsList = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
        //获取所有和该角色关联的用户uid缓存，并将这些用户的关于redis的缓存信息删除
        List<Long> uids = new ArrayList<>();
        for(BackUsersAuthgroups backUsersAuthgroups : backUsersAuthgroupsList){
            uids.add(backUsersAuthgroups.getUid());
        }
        //删除这些用户的redis缓存信息
        deleteUserRredisInfo(uids.toArray(new Long[0]));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(BackUserBO backUserBO) {
        List<Long> roleIds = backUserBO.getRoleIds();
        if(roleIds.size() == 0){
            return;
        }
        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupIds(roleIds);
        List<BackAuthgroups> backAuthgroupsInfos = backAuthgroupsSlave.findAll(backAuthgroupsQuery);
        backAuthgroupsMaster.delete(backAuthgroupsInfos);

        BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
        backUsersAuthgroupsQuery.setAuthgroupIds(roleIds);
        List<BackUsersAuthgroups> backUsersAuthgroupsInfos = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
        backUsersAuthgroupsMaster.delete(backUsersAuthgroupsInfos);

        BackAuthgroupsAuthesQuery backAuthgroupsAuthesList = new BackAuthgroupsAuthesQuery();
        backAuthgroupsAuthesList.setAuthgroupIds(roleIds);
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesInfos= backAuthgroupsAuthesSlave.findAll(backAuthgroupsAuthesList);
        backAuthgroupsAuthesMaster.delete(backAuthgroupsAuthesInfos);


        //获取所有和该角色关联的用户uid缓存，并将这些用户的关于redis的缓存信息删除
        List<Long> uids = new ArrayList<>();
        for(BackUsersAuthgroups backUsersAuthgroups : backUsersAuthgroupsInfos){
            uids.add(backUsersAuthgroups.getUid());
        }
        //删除这些用户的redis缓存信息
        deleteUserRredisInfo(uids.toArray(new Long[0]));
    }

    @Override
    public List<BackAuthesVO> showAuthoritiesUnderRole(BackUserBO backUserBO) {
        Long roleId = backUserBO.getRoleId();
        List<BackAuthes> allbackAuthesList = backAuthesSlave.findAll();
        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupId(roleId);
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesList = backAuthgroupsAuthesSlave.findAll(backAuthgroupsQuery);
        //如果都没有选中，则返回均未选中
        if(backAuthgroupsAuthesList.size() == 0){
            List<BackAuthesVO> backAuthesVOList = new ArrayList<>();
            for (BackAuthes backAuthes : allbackAuthesList){
                BackAuthesVO backAuthesVO = new BackAuthesVO();
                Long authId = backAuthes.getAuthId();
                String authName = backAuthes.getAuthName();
                backAuthesVO.setAuthId(authId);
                backAuthesVO.setAuthName(authName);
                backAuthesVO.setIsSelected(0);
                backAuthesVOList.add(backAuthesVO);
            }
            return backAuthesVOList;
        }

        List<Long> authIds = new ArrayList<>();
        for(BackAuthgroupsAuthes backAuthgroupsAuthes : backAuthgroupsAuthesList){
            authIds.add(backAuthgroupsAuthes.getAuthId());
        }
        List<BackAuthesVO> backAuthesVOList = new ArrayList<>();
        for (BackAuthes backAuthes : allbackAuthesList){
            BackAuthesVO backAuthesVO = new BackAuthesVO();
            Long authId = backAuthes.getAuthId();
            String authName = backAuthes.getAuthName();
            backAuthesVO.setAuthId(authId);
            backAuthesVO.setAuthName(authName);
            if(authIds.contains(authId)){
                backAuthesVO.setIsSelected(1);
            }else{
                backAuthesVO.setIsSelected(0);
            }
            backAuthesVOList.add(backAuthesVO);
        }
        return backAuthesVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignAuthorities4Role(BackUserBO backUserBO) {
        Long roleId = backUserBO.getRoleId();
        BackAuthgroupsAuthesQuery backAuthgroupsAuthesQuery = new BackAuthgroupsAuthesQuery();
        backAuthgroupsAuthesQuery.setAuthgroupId(roleId);
        List<BackAuthgroupsAuthes> backAuthgroupsAuthesInfos = backAuthgroupsAuthesSlave.findAll(backAuthgroupsAuthesQuery);
        if(backAuthgroupsAuthesInfos.size() != 0){
            backAuthgroupsAuthesMaster.delete(backAuthgroupsAuthesInfos);
        }

        List<Long> authIds = backUserBO.getAuthIds();
        if(authIds.size() != 0){
            List<BackAuthgroupsAuthes> backAuthgroupsAuthesList = new ArrayList<>();
            for(Long authId : authIds){
                BackAuthgroupsAuthes backAuthgroupsAuthes1 = new BackAuthgroupsAuthes();
                backAuthgroupsAuthes1.setId(idGlobalGenerator.getSeqId(BackAuthgroupsAuthes.class));
                backAuthgroupsAuthes1.setAuthgroupId(roleId);
                backAuthgroupsAuthes1.setAuthId(authId);
                backAuthgroupsAuthesList.add(backAuthgroupsAuthes1);
            }
            backAuthgroupsAuthesMaster.save(backAuthgroupsAuthesList);
        }

        //只要有被修改过，就要删除redis中相关用户的缓存信息
        if(backAuthgroupsAuthesInfos.size() != 0 || authIds.size() != 0){
            BackUsersAuthgroupsQuery backUsersAuthgroupsQuery = new BackUsersAuthgroupsQuery();
            backUsersAuthgroupsQuery.setAuthgroupId(roleId);
            List<BackUsersAuthgroups> backUsersAuthgroupsList = backUsersAuthgroupsSlave.findAll(backUsersAuthgroupsQuery);
            //获取所有和该角色关联的用户uid缓存，并将这些用户的关于redis的缓存信息删除
            List<Long> uids = new ArrayList<>();
            for(BackUsersAuthgroups backUsersAuthgroups : backUsersAuthgroupsList){
                uids.add(backUsersAuthgroups.getUid());
            }
            //删除这些用户的redis缓存信息
            deleteUserRredisInfo(uids.toArray(new Long[0]));
        }
    }


}
