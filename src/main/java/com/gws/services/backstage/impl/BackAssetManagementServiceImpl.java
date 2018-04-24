package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.CoinType;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.query.backstage.FrontUserQuery;
import com.gws.repositories.query.backstage.FrontUserRechargeQuery;
import com.gws.repositories.query.backstage.UsdgUserAccountQuery;
import com.gws.repositories.slave.backstage.FrontUserAccountSlave;
import com.gws.repositories.slave.backstage.FrontUserRechargeSlave;
import com.gws.repositories.slave.backstage.FrontUserSlave;
import com.gws.repositories.slave.backstage.UserIdentitySlave;
import com.gws.services.backstage.BackAssetManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Service
public class BackAssetManagementServiceImpl implements BackAssetManagementService {

    private final FrontUserSlave frontUserSlave;

    private final FrontUserAccountSlave frontUserAccountSlave;

    private final FrontUserRechargeSlave frontUserRechargeSlave;

    private final UserIdentitySlave userIdentitySlave;

    @Autowired
    public BackAssetManagementServiceImpl(FrontUserSlave frontUserSlave, FrontUserAccountSlave frontUserAccountSlave, UserIdentitySlave userIdentitySlave, FrontUserRechargeSlave frontUserRechargeSlave) {
        this.frontUserSlave = frontUserSlave;
        this.frontUserAccountSlave = frontUserAccountSlave;
        this.userIdentitySlave = userIdentitySlave;
        this.frontUserRechargeSlave = frontUserRechargeSlave;
    }

    @Override
    public PageDTO queryFrontUserAccount(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        Long uid = frontUserBO.getUid();
        String username = frontUserBO.getUsername();
        String email = frontUserBO.getEmail();
        String phoneNumber = frontUserBO.getPhoneNumber();

        FrontUserQuery frontUserQuery = new FrontUserQuery();
        if(uid != null){
            frontUserQuery.setUid(uid);
        }
        if(!StringUtils.isEmpty(username)){
            frontUserQuery.setUsernameLike(username);
        }
        if(!StringUtils.isEmpty(email)){
            frontUserQuery.setEmailAddressLike(email);
        }
        if(!StringUtils.isEmpty(phoneNumber)){
            frontUserQuery.setPhoneNumberLike(phoneNumber);
        }

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<FrontUser> frontUserPage = frontUserSlave.findAll(frontUserQuery, pageable);

        List<FrontUser> list = frontUserPage == null ? Collections.emptyList() : frontUserPage.getContent();
        long totalPage = frontUserPage == null ? 0 : frontUserPage.getTotalElements();
        if(totalPage == 0){
            //如果没有数据直接返回空的分页信息
            return PageDTO.getPagination(totalPage,Collections.emptyList());
        }

        //获取所有的uid
        List<Long> uids = new ArrayList<>();
        for (FrontUser frontUser : list) {
            uids.add(frontUser.getUid());
        }

        UsdgUserAccountQuery usdgUserAccountQuery = new UsdgUserAccountQuery();
        usdgUserAccountQuery.setUids(uids);
        List<UsdgUserAccount> usdgUserAccountList = frontUserAccountSlave.findAll(usdgUserAccountQuery);

        Map<Long,FrontUserAssetVO> frontUserAssetVOMap = new LinkedHashMap();
        for (UsdgUserAccount usdgUserAccount : usdgUserAccountList) {
            Long frontUid = usdgUserAccount.getUid();
            Integer type = usdgUserAccount.getType();
            Double realAmount = usdgUserAccount.getRealAmount();
            if(frontUserAssetVOMap.containsKey(frontUid)){
                FrontUserAssetVO frontUserAssetVO = frontUserAssetVOMap.get(frontUid);
                if(CoinType.USDG.equals(type)){
                    frontUserAssetVO.setUsdgNum(realAmount);
                }
                if(CoinType.BTY.equals(type)){
                    frontUserAssetVO.setBtyNum(realAmount);
                }
            }else{
                FrontUserAssetVO frontUserAssetVO = new FrontUserAssetVO();
                frontUserAssetVO.setUid(frontUid);
                if(CoinType.USDG.equals(type)){
                    frontUserAssetVO.setUsdgNum(realAmount);
                }
                if(CoinType.BTY.equals(type)){
                    frontUserAssetVO.setBtyNum(realAmount);
                }
                frontUserAssetVOMap.put(frontUid,frontUserAssetVO);
            }
        }

        List<FrontUserAssetVO> frontUserAssetVOList = new ArrayList<>();
        for(Long key : frontUserAssetVOMap.keySet()){
            FrontUserAssetVO frontUserAssetVO = frontUserAssetVOMap.get(key);
            frontUserAssetVOList.add(frontUserAssetVO);
        }

        return PageDTO.getPagination(totalPage,frontUserAssetVOList);
    }

    @Override
    public PageDTO queryFrontUserRecharge(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        Integer coinType = frontUserBO.getCoinType();
        Integer startTime = frontUserBO.getStartTime();
        Integer endTime = frontUserBO.getEndTime();
        Long uid = frontUserBO.getUid();

        FrontUserRechargeQuery frontUserRechargeQuery = new FrontUserRechargeQuery();
        if(coinType != null){
            frontUserRechargeQuery.setCoinType(coinType);
        }
        if(uid != null){
            frontUserRechargeQuery.setUid(uid);
        }
        frontUserRechargeQuery.setCstartTime(startTime);
        frontUserRechargeQuery.setCendTime(endTime);

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<FrontUserRecharge> frontUserRechargePage = frontUserRechargeSlave.findAll(frontUserRechargeQuery, pageable);

        List<FrontUserRecharge> list = frontUserRechargePage == null ? Collections.emptyList() : frontUserRechargePage.getContent();
        long totalPage = frontUserRechargePage == null ? 0 : frontUserRechargePage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }
}
