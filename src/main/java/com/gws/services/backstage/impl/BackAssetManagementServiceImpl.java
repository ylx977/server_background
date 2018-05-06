package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.CoinType;
import com.gws.common.constants.backstage.CoinWithdrawStatusEnum;
import com.gws.dto.backstage.PageDTO;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.master.backstage.*;
import com.gws.repositories.query.backstage.*;
import com.gws.repositories.slave.backstage.*;
import com.gws.services.backstage.BackAssetManagementService;
import com.gws.utils.decimal.DecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Service
public class BackAssetManagementServiceImpl implements BackAssetManagementService {

    private final FrontUserSlave frontUserSlave;

    private final FrontUserAccountSlave frontUserAccountSlave;

    private final FrontUserAccountMaster frontUserAccountMaster;

    private final FrontUserRechargeSlave frontUserRechargeSlave;

    private final PlatformUsdgSlave platformUsdgSlave;

    private final PlatformUsdgMaster platformUsdgMaster;

    private final UsdgOfficialAccountMaster usdgOfficialAccountMaster;

    private final UsdgOfficialAccountSlave usdgOfficialAccountSlave;

    private final FrontUserCoinWithdrawMaster frontUserCoinWithdrawMaster;

    private final FrontUserCoinWithdrawSlave frontUserCoinWithdrawSlave;

    private final BtyUsdgTradeOrderSlave btyUsdgTradeOrderSlave;

    private final PlatformGoldSlave platformGoldSlave;

    private final PlatformGoldMaster platformGoldMaster;

    private final UserIdentitySlave userIdentitySlave;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BackAssetManagementServiceImpl(FrontUserSlave frontUserSlave, FrontUserAccountSlave frontUserAccountSlave, FrontUserAccountMaster frontUserAccountMaster, UserIdentitySlave userIdentitySlave, FrontUserRechargeSlave frontUserRechargeSlave, PlatformUsdgSlave platformUsdgSlave, PlatformUsdgMaster platformUsdgMaster, UsdgOfficialAccountMaster usdgOfficialAccountMaster, UsdgOfficialAccountSlave usdgOfficialAccountSlave, FrontUserCoinWithdrawMaster frontUserCoinWithdrawMaster, FrontUserCoinWithdrawSlave frontUserCoinWithdrawSlave, BtyUsdgTradeOrderSlave btyUsdgTradeOrderSlave, PlatformGoldSlave platformGoldSlave, PlatformGoldMaster platformGoldMaster) {
        this.frontUserSlave = frontUserSlave;
        this.frontUserAccountSlave = frontUserAccountSlave;
        this.frontUserAccountMaster = frontUserAccountMaster;
        this.userIdentitySlave = userIdentitySlave;
        this.frontUserRechargeSlave = frontUserRechargeSlave;
        this.platformUsdgSlave = platformUsdgSlave;
        this.platformUsdgMaster = platformUsdgMaster;
        this.usdgOfficialAccountMaster = usdgOfficialAccountMaster;
        this.usdgOfficialAccountSlave = usdgOfficialAccountSlave;
        this.frontUserCoinWithdrawMaster = frontUserCoinWithdrawMaster;
        this.frontUserCoinWithdrawSlave = frontUserCoinWithdrawSlave;
        this.btyUsdgTradeOrderSlave = btyUsdgTradeOrderSlave;
        this.platformGoldSlave = platformGoldSlave;
        this.platformGoldMaster = platformGoldMaster;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUsdg(AssetBO assetBO) {
        //增加黄金的量(单位kg)
        Double gold = assetBO.getGold();
        double goldInData = DecimalUtil.multiply(gold,1000,8, RoundingMode.HALF_EVEN);
        //增加的usdg的量(等于黄金量*100000)，因为黄金单位是kg，1g黄金=100usdg
        double increasement = DecimalUtil.multiply(gold,100000,8, RoundingMode.HALF_EVEN);

        Query usdgQuery = entityManager.createNativeQuery("SELECT id,total_usdg,ctime,utime FROM platform_usdg WHERE id=1 for update",PlatformUsdg.class);
        Query goldQuery = entityManager.createNativeQuery("SELECT id,total_gold,ctime,utime FROM platform_gold WHERE id=1 for update",PlatformGold.class);
        List resultList1 = usdgQuery.getResultList();
        List resultList2 = goldQuery.getResultList();
        if(resultList1.size() != 0 && resultList2.size() != 0){
            PlatformUsdg usdgOne = (PlatformUsdg) usdgQuery.getSingleResult();
            PlatformGold goldOne = (PlatformGold) goldQuery.getSingleResult();
            //平台总的usdg插入数据
            Double totalUsdg = usdgOne.getTotalUsdg();
            PlatformUsdg platformUsdg = new PlatformUsdg();
            platformUsdg.setTotalUsdg(DecimalUtil.add(increasement, totalUsdg, 8, RoundingMode.HALF_EVEN));
            platformUsdgMaster.updateById(platformUsdg,1L, "totalUsdg");

            //平台总的黄金库表插入数据
            Double totalGold = goldOne.getTotalGold();
            PlatformGold platformGold = new PlatformGold();
            platformGold.setTotalGold(DecimalUtil.add(goldInData, totalGold, 8, RoundingMode.HALF_EVEN));
            platformGoldMaster.updateById(platformGold,1L, "totalGold");

            //更新平台的usdg账户中总usdg数量和可用usdg数量
            Query accountQuery = entityManager.createNativeQuery("SELECT id,type,name,address,publickey,privatekey,real_amount,usable_amount,freeze_amount,ctime,utime FROM usdg_official_account WHERE type="+CoinType.USDG+" FOR UPDATE ",UsdgOfficialAccount.class);
            List<UsdgOfficialAccount> accountOnes = (List<UsdgOfficialAccount>) accountQuery.getResultList();
            UsdgOfficialAccount accountOne = accountOnes.get(0);
            if(accountOne == null){
                throw new RuntimeException("平台usdg账户信息丢失");
            }

            UsdgOfficialAccount usdgOfficialAccount = new UsdgOfficialAccount();
            Long id = accountOne.getId();
            Double realAmount = accountOne.getRealAmount();
            Double usableAmount = accountOne.getUsableAmount();
            usdgOfficialAccount.setRealAmount(DecimalUtil.add(realAmount,increasement,8,RoundingMode.HALF_EVEN));
            usdgOfficialAccount.setUsableAmount(DecimalUtil.add(usableAmount,increasement,8,RoundingMode.HALF_EVEN));
            usdgOfficialAccountMaster.updateById(usdgOfficialAccount, id,"realAmount","usableAmount");
            return;
        }
        //平台总的usdg插入数据
        PlatformUsdg platformUsdg = new PlatformUsdg();
        platformUsdg.setId(1L);
        platformUsdg.setTotalUsdg(increasement);
        System.out.println(platformUsdg);
        platformUsdgMaster.save(platformUsdg);
        //平台的账户插入数据
        UsdgOfficialAccount usdgOfficialAccount = new UsdgOfficialAccount();
        usdgOfficialAccount.setId(1L);
        usdgOfficialAccount.setType(CoinType.USDG);
        usdgOfficialAccount.setName("USDG");
        //TODO
        usdgOfficialAccount.setAddress("ABCDEFG");
        //TODO
        usdgOfficialAccount.setPublicKey("publickey1");
        //TODO
        usdgOfficialAccount.setPrivateKey("privatekey1");
        usdgOfficialAccount.setRealAmount(increasement);
        usdgOfficialAccount.setUsableAmount(increasement);
        usdgOfficialAccount.setFreezeAmount(0d);
        usdgOfficialAccountMaster.save(usdgOfficialAccount);

        //平台总的黄金库存表插入数据
        PlatformGold platformGold = new PlatformGold();
        platformGold.setId(1L);
        platformGold.setTotalGold(DecimalUtil.multiply(gold,1000,8,RoundingMode.HALF_EVEN));
        platformGoldMaster.save(platformGold);
    }

    @Override
    public PageDTO queryFrontUserWithdraw(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        Integer startTime = frontUserBO.getStartTime();
        Integer endTime = frontUserBO.getEndTime();

        FrontUserWithdrawApplyQuery frontUserWithdrawApplyQuery = new FrontUserWithdrawApplyQuery();

        if(!StringUtils.isEmpty(frontUserBO.getPhoneNumber())){
            frontUserWithdrawApplyQuery.setPhoneNumberLike(frontUserBO.getPhoneNumber());
        }
        if(!StringUtils.isEmpty(frontUserBO.getEmail())){
            frontUserWithdrawApplyQuery.setEmailLike(frontUserBO.getEmail());
        }
        if(!StringUtils.isEmpty(frontUserBO.getPersonName())){
            frontUserWithdrawApplyQuery.setPersonNameLike(frontUserBO.getPersonName());
        }
        if(frontUserBO.getCoinType()!=null){
            frontUserWithdrawApplyQuery.setCoinType(frontUserBO.getCoinType());
        }
        if(frontUserBO.getUid()!=null){
            frontUserWithdrawApplyQuery.setUid(frontUserBO.getUid());
        }
        frontUserWithdrawApplyQuery.setApplyStatuses(Arrays.asList(CoinWithdrawStatusEnum.TO_CHECK.getCode(),CoinWithdrawStatusEnum.FIRST_PASS.getCode()));
        frontUserWithdrawApplyQuery.setCstartTime(startTime);
        frontUserWithdrawApplyQuery.setCendTime(endTime);

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<FrontUserWithdrawApply> frontUserWithdrawApplyPage = frontUserCoinWithdrawSlave.findAll(frontUserWithdrawApplyQuery, pageable);

        List<FrontUserWithdrawApply> list = frontUserWithdrawApplyPage == null ? Collections.emptyList() : frontUserWithdrawApplyPage.getContent();
        long totalPage = frontUserWithdrawApplyPage == null ? 0 : frontUserWithdrawApplyPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    public PageDTO queryFrontUserWithdrawHistory(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        Integer startTime = frontUserBO.getStartTime();
        Integer endTime = frontUserBO.getEndTime();

        FrontUserWithdrawApplyQuery frontUserWithdrawApplyQuery = new FrontUserWithdrawApplyQuery();

        if(!StringUtils.isEmpty(frontUserBO.getPhoneNumber())){
            frontUserWithdrawApplyQuery.setPhoneNumberLike(frontUserBO.getPhoneNumber());
        }
        if(!StringUtils.isEmpty(frontUserBO.getEmail())){
            frontUserWithdrawApplyQuery.setEmailLike(frontUserBO.getEmail());
        }
        if(!StringUtils.isEmpty(frontUserBO.getPersonName())){
            frontUserWithdrawApplyQuery.setPersonNameLike(frontUserBO.getPersonName());
        }
        if(frontUserBO.getCoinType()!=null){
            frontUserWithdrawApplyQuery.setCoinType(frontUserBO.getCoinType());
        }
        if(frontUserBO.getUid()!=null){
            frontUserWithdrawApplyQuery.setUid(frontUserBO.getUid());
        }
        if(frontUserBO.getFirstCheckUid()!=null){
            frontUserWithdrawApplyQuery.setFirstCheckUid(frontUserBO.getFirstCheckUid());
        }
        if(frontUserBO.getSecondCheckUid()!=null){
            frontUserWithdrawApplyQuery.setSecondCheckUid(frontUserBO.getSecondCheckUid());
        }
        if(frontUserBO.getFirstCheckName()!=null){
            frontUserWithdrawApplyQuery.setFirstCheckNameLike(frontUserBO.getFirstCheckName());
        }
        if(frontUserBO.getSecondCheckName()!=null){
            frontUserWithdrawApplyQuery.setSecondCheckNameLike(frontUserBO.getSecondCheckName());
        }
        frontUserWithdrawApplyQuery.setApplyStatuses(Arrays.asList(CoinWithdrawStatusEnum.REJECT.getCode(),CoinWithdrawStatusEnum.SECOND_PASS.getCode()));
        frontUserWithdrawApplyQuery.setCstartTime(startTime);
        frontUserWithdrawApplyQuery.setCendTime(endTime);

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<FrontUserWithdrawApply> frontUserWithdrawApplyPage = frontUserCoinWithdrawSlave.findAll(frontUserWithdrawApplyQuery, pageable);

        List<FrontUserWithdrawApply> list = frontUserWithdrawApplyPage == null ? Collections.emptyList() : frontUserWithdrawApplyPage.getContent();
        long totalPage = frontUserWithdrawApplyPage == null ? 0 : frontUserWithdrawApplyPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void firstPass(FrontUserBO frontUserBO) {
        //获取用户的详细信息
        UserDetailDTO userDetailDTO = frontUserBO.getUserDetailDTO();

        Integer currentTime = (int)System.currentTimeMillis()/1000;
        Long id = frontUserBO.getId();
        FrontUserWithdrawApply one = frontUserCoinWithdrawSlave.findOne(id);
        if(one==null){
            throw new RuntimeException("该申请信息不存在");
        }
        if(!one.getApplyStatus().equals(CoinWithdrawStatusEnum.TO_CHECK.getCode())){
            throw new RuntimeException("该申请信息不为待审核状态");
        }
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.FIRST_PASS.getCode());
        frontUserWithdrawApply.setUtime(currentTime);
        frontUserWithdrawApply.setFirstCheckUid(userDetailDTO.getUid());
        frontUserWithdrawApply.setFirstCheckName(userDetailDTO.getPersonName());
        int success = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","firstCheckUid","firstCheckName");
        if(success==0){
            throw new RuntimeException("用户申请信息更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void secondPass(FrontUserBO frontUserBO) {
        //获取用户的详细信息
        UserDetailDTO userDetailDTO = frontUserBO.getUserDetailDTO();

        Integer currentTime = (int)System.currentTimeMillis()/1000;
        Long id = frontUserBO.getId();
        FrontUserWithdrawApply one = frontUserCoinWithdrawSlave.findOne(id);
        if(one==null){
            throw new RuntimeException("该申请信息不存在");
        }
        if(!one.getApplyStatus().equals(CoinWithdrawStatusEnum.FIRST_PASS.getCode())){
            throw new RuntimeException("该申请信息不为初审通过状态");
        }
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.SECOND_PASS.getCode());
        frontUserWithdrawApply.setUtime(currentTime);
        frontUserWithdrawApply.setSecondCheckUid(userDetailDTO.getUid());
        frontUserWithdrawApply.setSecondCheckName(userDetailDTO.getPersonName());
        int success = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","secondCheckUid","secondCheckName");
        if(success==0){
            throw new RuntimeException("用户申请信息更新失败");
        }

        /*
         * 将钱和旷工费从用户的账户中扣除(扣除掉冻结那部分的钱)
         */
        //提取的数量
        Double coinAmount = one.getCoinAmount();
        //旷工费
        Double minerAmount = one.getMinerAmount();
        double totalAmount = DecimalUtil.add(coinAmount,minerAmount,8,RoundingMode.HALF_EVEN);
        //币种类型
        Integer coinType = one.getCoinType();
        //申请该订单信息的人的uid
        Long uid = one.getUid();

        UsdgUserAccountQuery usdgUserAccountQuery = new UsdgUserAccountQuery();
        usdgUserAccountQuery.setType(coinType);
        usdgUserAccountQuery.setUid(uid);
        UsdgUserAccount usdgUserAccount = frontUserAccountSlave.findOne(usdgUserAccountQuery);

        //当前用户的账户id号
        Long accountId = usdgUserAccount.getId();
        //当前账户可用数量
        Double usableAmount = usdgUserAccount.getUsableAmount();
        //当前账户冻结数量
        Double freezeAmount = usdgUserAccount.getFreezeAmount();

        double freezeResult = DecimalUtil.subtract(freezeAmount,totalAmount,8,RoundingMode.HALF_EVEN);
        double realResult = DecimalUtil.add(usableAmount,freezeResult,8,RoundingMode.HALF_EVEN);

        UsdgUserAccount userAccount = new UsdgUserAccount();
        userAccount.setFreezeAmount(freezeResult);
        userAccount.setRealAmount(realResult);
        userAccount.setUtime(currentTime);
        int success2 = frontUserAccountMaster.updateById(userAccount, accountId, "realAmount", "freezeAmount", "utime");
        if(success2 == 0){
            throw new RuntimeException("用户账户信息更新失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectWithdraw(FrontUserBO frontUserBO) {
        //获取用户的详细信息
        UserDetailDTO userDetailDTO = frontUserBO.getUserDetailDTO();

        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Long id = frontUserBO.getId();
        FrontUserWithdrawApply one = frontUserCoinWithdrawSlave.findOne(id);
        if(one==null){
            throw new RuntimeException("该申请信息不存在");
        }
        if(!one.getApplyStatus().equals(CoinWithdrawStatusEnum.TO_CHECK.getCode()) && !one.getApplyStatus().equals(CoinWithdrawStatusEnum.FIRST_PASS.getCode())){
            throw new RuntimeException("该申请信息已经完成");
        }

        //更新申请信息的状态
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.REJECT.getCode());
        frontUserWithdrawApply.setUtime(currentTime);
        int success1 = 0;
        if(one.getApplyStatus().equals(CoinWithdrawStatusEnum.TO_CHECK.getCode())){
            frontUserWithdrawApply.setFirstCheckUid(userDetailDTO.getUid());
            frontUserWithdrawApply.setFirstCheckName(userDetailDTO.getPersonName());
            success1 = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","firstCheckUid","firstCheckName");
        }
        if(one.getApplyStatus().equals(CoinWithdrawStatusEnum.FIRST_PASS.getCode())){
            frontUserWithdrawApply.setSecondCheckUid(userDetailDTO.getUid());
            frontUserWithdrawApply.setSecondCheckName(userDetailDTO.getPersonName());
            success1 = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","secondCheckUid","secondCheckName");
        }
        if(success1 == 0){
            throw new RuntimeException("用户申请信息更新失败");
        }

        /*
         * 将钱和旷工费从冻结状态返还给该用户的账户
         */
        //提取的数量
        Double coinAmount = one.getCoinAmount();
        //旷工费
        Double minerAmount = one.getMinerAmount();
        double totalAmount = DecimalUtil.add(coinAmount,minerAmount,8,RoundingMode.HALF_EVEN);
        //币种类型
        Integer coinType = one.getCoinType();
        //申请该订单信息的人的uid
        Long uid = one.getUid();

        UsdgUserAccountQuery usdgUserAccountQuery = new UsdgUserAccountQuery();
        usdgUserAccountQuery.setType(coinType);
        usdgUserAccountQuery.setUid(uid);
        UsdgUserAccount usdgUserAccount = frontUserAccountSlave.findOne(usdgUserAccountQuery);

        //当前用户的账户id号
        Long accountId = usdgUserAccount.getId();
        //当前账户可用数量
        Double usableAmount = usdgUserAccount.getUsableAmount();
        //当前账户冻结数量
        Double freezeAmount = usdgUserAccount.getFreezeAmount();

        double usableResult = DecimalUtil.add(totalAmount,usableAmount,8,RoundingMode.HALF_EVEN);
        double freezeResult = DecimalUtil.subtract(freezeAmount,totalAmount,8,RoundingMode.HALF_EVEN);
        double realResult = DecimalUtil.add(usableResult,freezeResult,8,RoundingMode.HALF_EVEN);

        UsdgUserAccount userAccount = new UsdgUserAccount();
        userAccount.setRealAmount(realResult);
        userAccount.setUsableAmount(usableResult);
        userAccount.setFreezeAmount(freezeResult);
        userAccount.setUtime(currentTime);
        int success2 = frontUserAccountMaster.updateById(userAccount, accountId, "realAmount","usableAmount", "freezeAmount", "utime");
        if(success2 == 0){
            throw new RuntimeException("用户账户信息更新失败");
        }
    }

    @Override
    public PageDTO queryExchange(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        Integer startTime = frontUserBO.getStartTime();
        Integer endTime = frontUserBO.getEndTime();


        BtyUsdgTradeOrderQuery btyUsdgTradeOrderQuery = new BtyUsdgTradeOrderQuery();

        if(frontUserBO.getUid()!=null){
            btyUsdgTradeOrderQuery.setUid(frontUserBO.getUid());
        }

        if(frontUserBO.getTradeType()!=null){
            btyUsdgTradeOrderQuery.setTradeType(frontUserBO.getTradeType());
        }

        if(frontUserBO.getPersonName()!=null){
            btyUsdgTradeOrderQuery.setPersonNameLike(frontUserBO.getPersonName());
        }

        if(frontUserBO.getPhoneNumber()!=null){
            btyUsdgTradeOrderQuery.setPhoneNumberLike(frontUserBO.getPhoneNumber());
        }

        if(frontUserBO.getEmail()!=null){
            btyUsdgTradeOrderQuery.setEmailLike(frontUserBO.getEmail());
        }
        btyUsdgTradeOrderQuery.setCstartTime(startTime);
        btyUsdgTradeOrderQuery.setCendTime(endTime);

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<BtyUsdgTradeOrder> btyUsdgTradeOrderPage = btyUsdgTradeOrderSlave.findAll(btyUsdgTradeOrderQuery, pageable);

        List<BtyUsdgTradeOrder> list = btyUsdgTradeOrderPage == null ? Collections.emptyList() : btyUsdgTradeOrderPage.getContent();
        long totalPage = btyUsdgTradeOrderPage == null ? 0 : btyUsdgTradeOrderPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    public AssetBalanceVO queryAssetBalance() {
        AssetBalanceVO assetBalanceVO = new AssetBalanceVO();

        PlatformGold platformGold = platformGoldSlave.findOne(1L);
        PlatformUsdg platformUsdg = platformUsdgSlave.findOne(1L);
        List<UsdgOfficialAccount> usdgOfficialAccountList = usdgOfficialAccountSlave.findAll();

        assetBalanceVO.setGoldBanance(platformGold.getTotalGold());
        assetBalanceVO.setTotalUsdgAmount(platformUsdg.getTotalUsdg());
        for (UsdgOfficialAccount usdgOfficialAccount: usdgOfficialAccountList){
            Integer type = usdgOfficialAccount.getType();
            if(CoinType.USDG.equals(type)){
                assetBalanceVO.setUsdgBalance(usdgOfficialAccount.getRealAmount());
            }
            if(CoinType.BTY.equals(type)){
                assetBalanceVO.setPlatformBtyAddress(usdgOfficialAccount.getAddress());
                assetBalanceVO.setBtyBalance(usdgOfficialAccount.getRealAmount());
            }
        }
        assetBalanceVO.setMarketUsdgBalance(assetBalanceVO.getTotalUsdgAmount()-assetBalanceVO.getUsdgBalance());
        return assetBalanceVO;
    }
}
