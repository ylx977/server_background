package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.*;
import com.gws.configuration.backstage.UserInfoConfig;
import com.gws.controllers.backSM.SMUtil;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.*;
import com.gws.entity.backstage.price.PriceResult;
import com.gws.entity.backstage.wallet_lbz.USDGTXUtis;
import com.gws.mapper.BackAssetManagementMapper;
import com.gws.mapper.FrontUserRechargeMapper;
import com.gws.repositories.master.backstage.*;
import com.gws.repositories.query.backstage.*;
import com.gws.repositories.slave.backstage.*;
import com.gws.services.backstage.BackAssetManagementService;
import com.gws.utils.IPUtil;
import com.gws.utils.blockchain.*;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.decimal.DecimalUtil;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.transfer.TransferCoin;
import com.gws.utils.wallet.BitcoinAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Service
public class BackAssetManagementServiceImpl implements BackAssetManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackAssetManagementServiceImpl.class);

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

    private final BtyAddressesSlave btyAddressesSlave;

    private final BtyAddressesMaster btyAddressesMaster;

    private final IdGlobalGenerator idGlobalGenerator;

    private final BackUserSlave backUserSlave;

    private final UserContractSlave userContractSlave;

    private final UsdgGoldTopupMaster usdgGoldTopupMaster;

    private final BackAssetManagementMapper backAssetManagementMapper;

    private final FrontUserRechargeMapper frontUserRechargeMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 这不是银行的地址，而是银行的公钥
     */
    @Value("${blockchain.bankAddress}")
    private String bankAddress;
    /**
     * 银行的公钥
     */
    @Value("${blockchain.bankPubkey}")
    private String bankPubkey;
    /**
     * 银行的私钥
     */
    @Value("${blockchain.bankPrikey}")
    private String bankPrikey;

    /**
     * 平台BTY地址
     */
    @Value("${platform.bty.address}")
    private String platformBtyAddress;
    @Value("${platform.bty.seed}")
    private String platformBtySeed;
    @Value("${platform.bty.privatekey}")
    private String platformBtyPrivatekey;
    @Value("${platform.bty.publickey}")
    private String platformBtyPublickey;
    @Value("${platform.bty.pubkey}")
    private String platformBtyPubkey;
    @Value("${platform.bty.prikey}")
    private String platformBtyPrikey;

    /**
     * 平台USDG地址
     */
    @Value("${platform.usdg.address}")
    private String platformUsdgAddress;
    @Value("${platform.usdg.seed}")
    private String platformUsdgSeed;
    @Value("${platform.usdg.privatekey}")
    private String platformUsdgPrivatekey;
    @Value("${platform.usdg.publickey}")
    private String platformUsdgPublickey;
    @Value("${platform.usdg.pubkey}")
    private String platformUsdgPubkey;
    @Value("${platform.usdg.prikey}")
    private String platformUsdgPrikey;

    /**
     * 获取最新价格的接口
     */
    @Value("${kline.data.price}")
    private String gettingprice;

    /**
     * 滨江区块链接口
     */
    @Value("${blockchain.walleturl}")
    private String walleturl;

    @Autowired
    public BackAssetManagementServiceImpl(FrontUserSlave frontUserSlave,
                                          FrontUserAccountSlave frontUserAccountSlave,
                                          FrontUserAccountMaster frontUserAccountMaster,
                                          UserIdentitySlave userIdentitySlave,
                                          FrontUserRechargeSlave frontUserRechargeSlave,
                                          PlatformUsdgSlave platformUsdgSlave,
                                          PlatformUsdgMaster platformUsdgMaster,
                                          UsdgOfficialAccountMaster usdgOfficialAccountMaster,
                                          UsdgOfficialAccountSlave usdgOfficialAccountSlave,
                                          FrontUserCoinWithdrawMaster frontUserCoinWithdrawMaster,
                                          FrontUserCoinWithdrawSlave frontUserCoinWithdrawSlave,
                                          BtyUsdgTradeOrderSlave btyUsdgTradeOrderSlave,
                                          PlatformGoldSlave platformGoldSlave,
                                          PlatformGoldMaster platformGoldMaster,
                                          BtyAddressesSlave btyAddressesSlave,
                                          BtyAddressesMaster btyAddressesMaster,
                                          IdGlobalGenerator idGlobalGenerator,
                                          BackUserSlave backUserSlave,
                                          UserContractSlave userContractSlave,
                                          UsdgGoldTopupMaster usdgGoldTopupMaster,
                                          BackAssetManagementMapper backAssetManagementMapper,
                                          FrontUserRechargeMapper frontUserRechargeMapper) {
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
        this.btyAddressesSlave = btyAddressesSlave;
        this.btyAddressesMaster = btyAddressesMaster;
        this.idGlobalGenerator = idGlobalGenerator;
        this.backUserSlave = backUserSlave;
        this.userContractSlave = userContractSlave;
        this.usdgGoldTopupMaster = usdgGoldTopupMaster;
        this.backAssetManagementMapper = backAssetManagementMapper;
        this.frontUserRechargeMapper = frontUserRechargeMapper;
    }

    @Override
    public PageDTO queryFrontUserAccount(FrontUserBO frontUserBO) {
        System.out.println("柯凡地址："+gettingprice);
        //从柯凡处获取最新市价
        String jsonResult = HttpRequest.sendGet(gettingprice);
        PriceResult priceResult = JSON.parseObject(jsonResult, PriceResult.class);
        if(priceResult.getCode()!=200){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.GET_PRICE_NETWORK_ERROR));
        }
        List<PriceResult.Trade> data = priceResult.getData();
        Double btyusdtPrice = 0d;
        Double usdtrmbPrice = 0d;
        Double btyusdgPrice = 0d;
        //获取【BTY/USDT】,【USDT/RMB】,【BTY/USDG】的兑换价格
        try{
            for(PriceResult.Trade trade : data){
                String symbol = trade.getSymbol();
                if(Symbol.BTYUSDG.equals(symbol)){
                    btyusdgPrice = (trade.getBuy() + trade.getSell())/2;
                }
                if(Symbol.USDTRMB.equals(symbol)){
                    usdtrmbPrice = (trade.getBuy() + trade.getSell())/2;
                }
                if(Symbol.BTYUSDT.equals(symbol)){
                    btyusdtPrice = (trade.getBuy() + trade.getSell())/2;
                }
            }
        }catch(Exception e){
            LOGGER.warn("实时价格计算失败");
        }



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
            Double btyNum = frontUserAssetVO.getBtyNum();
            Double usdgNum = frontUserAssetVO.getUsdgNum();
            Double totalAssets = btyNum * btyusdtPrice * usdtrmbPrice + usdgNum / btyusdgPrice * btyusdtPrice * usdtrmbPrice;
            frontUserAssetVO.setTotalAssets(totalAssets);
            frontUserAssetVOList.add(frontUserAssetVO);
        }

        return PageDTO.getPagination(totalPage,frontUserAssetVOList);
    }

    @Override
    public PageDTO queryFrontUserRecharge(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        frontUserBO.setStartPage((page - 1) * rowNum);
        List<FrontUserRecharge> list = frontUserRechargeMapper.queryFrontUserRecharge(frontUserBO);
        long totalPage = frontUserRechargeMapper.queryFrontUserRechargeCount(frontUserBO);
        return PageDTO.getPagination(totalPage,list);
    }

    /**
     * 目前思路是
     * 1：判断平台usdg和gold总账户是否有数据，没有数据就创建新数据，账户资产都是0
     * 2：调用滨江的转币接口
     * 3：增加平台黄金增加记录(状态1表示申请中)表usdg_gold_topup
     * @param assetBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUsdg(AssetBO assetBO) {
        //增加黄金的量(单位kg)
        Double gold = assetBO.getGold();
        //增加黄金的量(单位g)
        double goldInGram = DecimalUtil.multiply(gold,1000,8, RoundingMode.HALF_EVEN);
        //增加的usdg的量(等于黄金量*100000)，因为黄金单位是kg，1g黄金=100usdg
        double increasement = DecimalUtil.multiply(goldInGram,100,8, RoundingMode.HALF_EVEN);

        //查平台的usdg总账户
        PlatformUsdg platformUsdg = platformUsdgSlave.findOne(1L);
        //查平台的黄金总账户
        PlatformGold platformGold = platformGoldSlave.findOne(1L);
        //查询平台官方的的usdg信息
        UsdgOfficialAccount officialAccount = usdgOfficialAccountSlave.findOne(1L);
        if(platformUsdg == null && platformGold == null && officialAccount == null){
            //平台总的usdg插入数据
            PlatformUsdg newPlatformUsdg = new PlatformUsdg();
            newPlatformUsdg.setId(1L);
            newPlatformUsdg.setTotalUsdg(increasement);
            platformUsdgMaster.save(newPlatformUsdg);

            //平台总的黄金库存表插入数据
            PlatformGold newPlatformGold = new PlatformGold();
            newPlatformGold.setId(1L);
            newPlatformGold.setTotalGold(goldInGram );
            platformGoldMaster.save(newPlatformGold);

            //平台官方账户里面初次加入BTY/USDG数据
            String address = USDGTXUtis.createAddress(PlatformUid.STRINGUID);
            for (int i=1;i<=2;i++){
                UsdgOfficialAccount usdgOfficialAccount = new UsdgOfficialAccount();
                usdgOfficialAccount.setId((long)i);
                usdgOfficialAccount.setType(i);
                if(CoinType.USDG.equals(i)){
                    usdgOfficialAccount.setName(CoinType2.USDG);
                    usdgOfficialAccount.setPubkey(platformUsdgPubkey);
                    usdgOfficialAccount.setPrikey(platformUsdgPrikey);
                    usdgOfficialAccount.setRealAmount(increasement);
                    usdgOfficialAccount.setUsableAmount(increasement);
                    usdgOfficialAccount.setFreezeAmount(0d);
                }
                if(CoinType.BTY.equals(i)){
                    usdgOfficialAccount.setName(CoinType2.BTY);
                    usdgOfficialAccount.setPubkey(platformBtyPubkey);
                    usdgOfficialAccount.setPrikey(platformBtyPrikey);
                    usdgOfficialAccount.setRealAmount(0d);
                    usdgOfficialAccount.setUsableAmount(0d);
                    usdgOfficialAccount.setFreezeAmount(0d);
                }
                usdgOfficialAccount.setAddress(address);
                usdgOfficialAccountMaster.save(usdgOfficialAccount);
            }
        }else{
            Query nativeQuery1 = entityManager.createNativeQuery("UPDATE platform_gold SET total_gold = total_gold + ? WHERE id = 1");
            nativeQuery1.setParameter(1,goldInGram);
            int success1 = nativeQuery1.executeUpdate();
            if(success1 == 0){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_GOLD_TABLE_FAIL));
            }
            Query nativeQuery2 = entityManager.createNativeQuery("UPDATE platform_usdg SET total_usdg = total_usdg + ? WHERE id = 1");
            nativeQuery2.setParameter(1,increasement);
            int success2 = nativeQuery2.executeUpdate();
            if(success2 == 0){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USDG_TABLE_FAIL));
            }
            Query nativeQuery3 = entityManager.createNativeQuery("UPDATE usdg_official_account SET usable_amount = usable_amount + ?,real_amount = real_amount + ? WHERE type = ?");
            nativeQuery3.setParameter(1,increasement);
            nativeQuery3.setParameter(2,increasement);
            nativeQuery3.setParameter(3,CoinType.USDG);
            int success3 = nativeQuery3.executeUpdate();
            if(success3 == 0){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USDG_ACCOUNT_FAIL));
            }
        }

        ProtobufBean protobufBean = Protobuf4EdsaUtils.requestTransfer(bankPrikey, SymbolId.USDG, bankPubkey, platformUsdgPubkey, (long) (100000000 * increasement));
        String result = BlockUtils.sendPostParam(protobufBean);
        boolean flag = BlockUtils.vilaResult(result);
        if(!flag){
            LOGGER.error("平台增加黄金出现区块链错误:"+BlockUtils.getErrorMessage(result));
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+BlockUtils.getErrorMessage(result));
        }
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
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Long id = frontUserBO.getId();
        FrontUserWithdrawApply one = frontUserCoinWithdrawSlave.findOne(id);
        if(one==null){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.APPLY_ORDER_NOT_EXIST));
        }
        if(!one.getApplyStatus().equals(CoinWithdrawStatusEnum.TO_CHECK.getCode())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.APPLY_NOT_IN_TO_CHECK));
        }
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.FIRST_PASS.getCode());
        frontUserWithdrawApply.setUtime(currentTime);
        frontUserWithdrawApply.setFirstCheckUid(UserInfoConfig.getUserInfo().getUid());
        frontUserWithdrawApply.setFirstCheckName(UserInfoConfig.getUserInfo().getPersonName());
        int success = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","firstCheckUid","firstCheckName");
        if(success==0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USER_APPLY_INFO_FAIL));
        }
    }

    /**
     * 王斌在提币申请的时候，只是冻结了用户资产(包含手续费)
     * 异常处理注意：如果这里调滨江接口的时候出现异常，等接口正常后再调取(大不了拒绝就行了)
     * @param frontUserBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  secondPass(FrontUserBO frontUserBO) {
        /*
            1，查询用户本地合约(黄学忠接口用)的公私钥
            2，旷工费打给平台
            3，提的币(不含旷工费)打给银行
            4，调用滨江转币接口
            5，将4返回的hash存入提取的那条数据中

            注意：不改变用户账户的冻结资产，等王斌根据这个hash查询到用户的交易信息后再去数据库改，同时平台的账户还要增加旷工费
         */

        Integer currentTime = (int) (System.currentTimeMillis()/1000);
        Long id = frontUserBO.getId();
        //获取该申请单详细信息
        FrontUserWithdrawApply one = frontUserCoinWithdrawSlave.findOne(id);
        if(one==null){
            throw new RuntimeException (LangReadUtil.getProperty(ErrorMsg.APPLY_ORDER_NOT_EXIST));
        }
        //判断订单状态是否正确
        if(!one.getApplyStatus().equals(CoinWithdrawStatusEnum.FIRST_PASS.getCode())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.APPLY_NOT_IN_FIRST_PASS));
        }

        //todo 提取的数量（包含旷工费）
        Double totalAmount = one.getCoinAmount();
        //todo 旷工费(这笔钱是通过交易给平台的账号，真实的币是通过总地址打给外部的地址的)
        Double minerAmount = one.getMinerAmount();
        // todo 真实到账的币数量 = 提币数量 - 旷工费
        double coinAmount = DecimalUtil.subtract(totalAmount,minerAmount,8,RoundingMode.HALF_EVEN);
        //确保旷工费要小于提取币的数量
        if(coinAmount < 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.MINER_TOO_MUCH));
        }
        //要打币去的地址
        String toAddress = one.getOuterAddress();

        if(!BitcoinAddressValidator.validateBitcoinAddress(toAddress)){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.ERROR_BITADDRESS_FORMAT));
        }

        //币种类型1:usdg 2:bty
        Integer coinType = one.getCoinType();
        //申请该订单信息的人的uid
        Long uid = one.getUid();

        //查询到用户本地区块链的公私钥(走黄学忠接口)
        UserContract userContract = userContractSlave.findOne(uid);
        String privatekey = userContract.getPrivatekey();
        String publickey = userContract.getPublickey();

        //将旷工费给平台(黄学忠接口转)
        LOGGER.info("将旷工费给平台(黄学忠接口转)");
//        long minerFee = (long) (minerAmount * 100000000);
        int symboldId =(CoinType.BTY.equals(coinType) ? SymbolId.BTY : SymbolId.USDG);
        TransferCoin.transferCoin(publickey,privatekey,platformUsdgPubkey,minerAmount,symboldId);

        //将提取的币打给银行(黄学忠接口转)
        LOGGER.info("将提取的币打给银行(黄学忠接口转)");
        long amount = (long) (coinAmount * 100000000);
        ProtobufBean toBank = Protobuf4EdsaUtils.requestTransfer(privatekey, symboldId, publickey, bankPubkey, amount);
        String result2 = BlockUtils.sendPostParam(toBank);
        boolean flag2 = BlockUtils.vilaResult(result2);
        if(!flag2){
            String errorMessage2 = BlockUtils.getErrorMessage(result2);
            LOGGER.error("前台用户提币，将旷工费打给平台时，区块链出现错误：{}",errorMessage2);
            //出错还要将旷工费返打回去
            TransferCoin.transferCoin(platformUsdgPubkey,platformUsdgPrikey,publickey,minerAmount,symboldId);
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage2);
        }

        //将币通过总地址打给外部地址(李邦柱接口转)
        LOGGER.info("将币通过总地址打给外部地址(李邦柱接口转)");
        String hash;
        try {
            hash = USDGTXUtis.withdrawCoin(toAddress, amount, CoinType2.USDG);
        }catch (Exception e){
            //提币调用滨江出错，还要将旷工费返打回去，还要从银行将提的币返打回去
            LOGGER.error("提币调用滨江出错，还要将旷工费返打回去，还要从银行将提的币返打回去,币种symboldId：{}，旷工费：{}，提币数量(不含旷工费)：{}",symboldId,minerAmount,coinAmount);
            TransferCoin.transferCoin(platformUsdgPubkey,platformUsdgPrikey,publickey,minerAmount,symboldId);
            ProtobufBean bankToUser = Protobuf4EdsaUtils.requestTransfer(bankPrikey, symboldId, bankPubkey, publickey, amount);
            String result3 = BlockUtils.sendPostParam(bankToUser);
            boolean flag3 = BlockUtils.vilaResult(result3);
            if(!flag3){
                String errorMessage3 = BlockUtils.getErrorMessage(result3);
                LOGGER.error("前台用户提币调用滨江区块链出错后，从银行反打币给用户提的币出现区块链错误：{}",errorMessage3);
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage3);
            }

            throw new RuntimeException(e);
        }

        //将hash存入申请表中，同时将申请单的状态改成成功,同时补充复审人的人名和uid
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setHash(hash);
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.SECOND_PASS.getCode());
        frontUserWithdrawApply.setUtime(currentTime);
        frontUserWithdrawApply.setSecondCheckUid(UserInfoConfig.getUserInfo().getUid());
        frontUserWithdrawApply.setSecondCheckName(UserInfoConfig.getUserInfo().getPersonName());
        frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply,id,"applyStatus","secondCheckUid","secondCheckName","hash","utime");
    }


    /**
     * 王斌在提币申请的时候将用户的钱先往银行打，同时冻结了资产(包含手续费)
     * 这里如果拒绝的话直接解冻，同时将银行里的钱返还给用户即可
     * @param frontUserBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectWithdraw(FrontUserBO frontUserBO) {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        //提币申请单的id号
        Long id = frontUserBO.getId();
        //获取用户提币申请的详细信息
        FrontUserWithdrawApply one = frontUserCoinWithdrawSlave.findOne(id);
        if(one==null){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.APPLY_ORDER_NOT_EXIST));
        }
        //只有待审核和初审才有资格被撤销
        if(!one.getApplyStatus().equals(CoinWithdrawStatusEnum.TO_CHECK.getCode()) && !one.getApplyStatus().equals(CoinWithdrawStatusEnum.FIRST_PASS.getCode())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.APPLY_ORDER_FINISHED));
        }

        //更新申请信息的状态
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.REJECT.getCode());
        frontUserWithdrawApply.setUtime(currentTime);
        int success1 = 0;
        if(one.getApplyStatus().equals(CoinWithdrawStatusEnum.TO_CHECK.getCode())){
            frontUserWithdrawApply.setFirstCheckUid(UserInfoConfig.getUserInfo().getUid());
            frontUserWithdrawApply.setFirstCheckName(UserInfoConfig.getUserInfo().getPersonName());
            success1 = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","firstCheckUid","firstCheckName");
        }
        if(one.getApplyStatus().equals(CoinWithdrawStatusEnum.FIRST_PASS.getCode())){
            frontUserWithdrawApply.setSecondCheckUid(UserInfoConfig.getUserInfo().getUid());
            frontUserWithdrawApply.setSecondCheckName(UserInfoConfig.getUserInfo().getPersonName());
            success1 = frontUserCoinWithdrawMaster.updateById(frontUserWithdrawApply, id, "applyStatus","utime","secondCheckUid","secondCheckName");
        }
        if(success1 == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USER_APPLY_INFO_FAIL));
        }

        /*
         * 将钱和旷工费从冻结状态返还给该用户的账户
         */
        //提取的数量(数据库里面有coin_amount的备注，这个是包含了旷工费的，所以不需要再加旷工费miner_amount了)
        Double coinAmount = one.getCoinAmount();
        //旷工费
//        Double minerAmount = one.getMinerAmount();
        //币种类型
        Integer coinType = one.getCoinType();
        //申请该订单信息的人的uid
        Long uid = one.getUid();

        Query nativeQuery = entityManager.createNativeQuery("UPDATE usdg_user_account SET usable_amount = usable_amount + ?,freeze_amount = freeze_amount - ? WHERE uid = ? AND type = ?");
        nativeQuery.setParameter(1,coinAmount);
        nativeQuery.setParameter(2,coinAmount);
        nativeQuery.setParameter(3,uid);
        nativeQuery.setParameter(4,coinType);
        int success = nativeQuery.executeUpdate();
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USER_ACCOUNT_FAIL));
        }
    }

    @Override
    public PageDTO queryExchange(FrontUserBO frontUserBO) {
        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();
        frontUserBO.setStartPage((page-1)*rowNum);
        List<BtyUsdgTradeOrder> list = backAssetManagementMapper.queryExchange(frontUserBO);
        long totalPage = backAssetManagementMapper.queryExchangeCount(frontUserBO);
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

    @Override
    public List<BtyAddresses> queryAddresses() {
        List<BtyAddresses> all = btyAddressesSlave.findAll();
        return all;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddresses(AssetBO assetBO) {
        Long id = assetBO.getId();
        btyAddressesMaster.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAddresses(AssetBO assetBO) {
        BtyAddresses btyAddresses = new BtyAddresses();
        btyAddresses.setId(idGlobalGenerator.getSeqId(BtyAddresses.class));
        btyAddresses.setAddress(assetBO.getAddress());
        btyAddresses.setTag(assetBO.getTag());
        btyAddresses.setIsDefault(0);
        btyAddressesMaster.save(btyAddresses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(AssetBO assetBO) {
        List<BtyAddresses> all = btyAddressesSlave.findAll();
        for (BtyAddresses btyAddresses : all) {
            Integer isDefault = btyAddresses.getIsDefault();
            Long id = btyAddresses.getId();
            if(isDefault == 1){
                BtyAddresses addresses = new BtyAddresses();
                addresses.setIsDefault(0);
                btyAddressesMaster.updateById(addresses,id,"isDefault");
            }
        }
        Long targetId = assetBO.getId();
        BtyAddresses addresses = new BtyAddresses();
        addresses.setIsDefault(1);
        btyAddressesMaster.updateById(addresses,targetId,"isDefault");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawBTY(AssetBO assetBO) {
        /*
         1, 检查平台当前BTY资产是否充足（从数据库中查）
         2，检查手机验证码是否通过
         3，将用户bty资产冻结【注意：是冻结，因为滨江接口是异步调用】
         4，将用户的bty资产打入黄学忠的银行(合约接口)
         5，调用滨江的转币接口
         6，将滨江返回的hash值存入交易记录表中（方便王斌定时扫描，然后改变账户资金额）
         */
        String toAddress = assetBO.getToAddress();
        Double amount = assetBO.getAmount();
        int coinType = assetBO.getCoinType();
        //TODO 目前这个手续费是0
        //短信验证码
        String code = assetBO.getCode();
        HttpServletRequest request = assetBO.getRequest();

        //---->todo 获取平台超级管理员的详细信息(steven的个人账户信息)
        BackUser one = backUserSlave.findOne(1L);
        String phone = UserInfoConfig.getUserInfo().getContact();
        //*********************短信验证码的验证**********************
        String ip = IPUtil.getIpAddr(request);
        boolean flag = SMUtil.valiSMCode(code, phone, ip);
        if(!flag){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.SM_ERROR));
        }
        //*********************短信验证码的验证**********************

        //先将平台的账户币数量先冻结了，而不是直接先扣除 todo 【让王斌的定时器去处理】
        Query nativeQuery = entityManager.createNativeQuery("UPDATE usdg_official_account SET usable_amount = usable_amount - ?,freeze_amount = freeze_amount + ? WHERE type = ?");
        nativeQuery.setParameter(1,amount);
        nativeQuery.setParameter(2,amount);
        nativeQuery.setParameter(3,coinType);
        int success = nativeQuery.executeUpdate();
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USER_INFO_FAIL));
        }

        LOGGER.info("平台提币调用黄学忠接口：{}，提币数量为：{}",coinType,amount);
        int symbolId = (coinType == CoinType.BTY ? SymbolId.BTY : SymbolId.USDG);
        //平台提币，先要将币打入到该平台的虚拟银行账户下，然后再调公链的接口
        ProtobufBean protobufBean = Protobuf4EdsaUtils.requestTransfer(platformBtyPrikey, symbolId, platformBtyPubkey, bankPubkey, (long)(amount*100000000));
        //---------------------------------------------------------------------------------------->todo,第1个请求（调用黄学忠的接口）
        String jsonResult = BlockUtils.sendPostParam(protobufBean);
        boolean flag2 = BlockUtils.vilaResult(jsonResult);
        if(!flag2){
            String errorMessage = BlockUtils.getErrorMessage(jsonResult);
            LOGGER.error("平台提币调用黄学忠接口：{}，出现区块链错误",coinType);
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage);
        }

        String tokenSymbol = (coinType == CoinType.USDG ? CoinType2.USDG : CoinType2.BTY);
        String hash;
        try {
            hash = USDGTXUtis.withdrawCoin(toAddress, amount * 100000000, tokenSymbol);
        }catch (Exception e){
            //调滨江接口出现问题，需要调黄学忠接口将币从银行反打回用户去
            LOGGER.error("调滨江接口出现问题，需要调黄学忠接口将币从银行反打回用户去");
            ProtobufBean protobufBean2 = Protobuf4EdsaUtils.requestTransfer(bankPrikey, symbolId, bankPubkey, platformBtyPubkey, (long)(amount*100000000));
            String jsonResult2 = BlockUtils.sendPostParam(protobufBean2);
            boolean flag3 = BlockUtils.vilaResult(jsonResult2);
            if(!flag3){
                String errorMessage2 = BlockUtils.getErrorMessage(jsonResult2);
                LOGGER.error("平台提币调用滨江接口出错后反调黄学忠接口又出现错误，打币的币种为：{}，提币数量为：{}",coinType,amount);
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.BLOCK_CHIAN_ERROR)+errorMessage2);
            }
            throw new RuntimeException(e);
        }

        //对第三个请求返回的结果的hash值要存入交易信息中
        FrontUserWithdrawApply frontUserWithdrawApply = new FrontUserWithdrawApply();
        frontUserWithdrawApply.setId(idGlobalGenerator.getSeqId(FrontUserWithdrawApply.class));
        frontUserWithdrawApply.setUid(PlatformUid.LONGUID);//默认平台账户的前台账户uid号为1
        frontUserWithdrawApply.setCoinAmount(amount);
        frontUserWithdrawApply.setInnerAddress(null);
        frontUserWithdrawApply.setMinerAmount(0d);
        frontUserWithdrawApply.setPhoneNumber(phone);
        frontUserWithdrawApply.setPersonName(one.getPersonName());
        frontUserWithdrawApply.setFirstCheckUid(one.getUid());
        frontUserWithdrawApply.setFirstCheckName(one.getPersonName());
        frontUserWithdrawApply.setSecondCheckUid(one.getUid());
        frontUserWithdrawApply.setSecondCheckName(one.getPersonName());
        frontUserWithdrawApply.setApplyStatus(CoinWithdrawStatusEnum.SECOND_PASS.getCode());//-------状态必须是复审通过，让王斌去扫描
        frontUserWithdrawApply.setEmail(one.getContact());
        frontUserWithdrawApply.setCoinType(coinType);
        frontUserWithdrawApply.setOuterAddress(toAddress);
        frontUserWithdrawApply.setHash(hash);//-------把李邦柱返回的hash存入
        frontUserCoinWithdrawMaster.save(frontUserWithdrawApply);
    }


}
