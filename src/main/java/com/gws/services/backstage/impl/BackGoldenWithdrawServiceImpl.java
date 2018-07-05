package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.*;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.master.backstage.BackGoldenCodeMaster;
import com.gws.repositories.master.backstage.BackGoldenWithdrawMaster;
import com.gws.repositories.query.backstage.BackGoldenCodeQuery;
import com.gws.repositories.query.backstage.BackGoldenWithdarwQuery;
import com.gws.repositories.slave.backstage.*;
import com.gws.services.backstage.BackGoldenWithdrawService;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.decimal.DecimalUtil;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.transfer.TransferCoin;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gws.utils.sql.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Service
public class BackGoldenWithdrawServiceImpl implements BackGoldenWithdrawService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackGoldenWithdrawServiceImpl.class);

    private final BackGoldenWithdrawSlave backGoldenWithdrawSlave;

    private final BackGoldenWithdrawMaster backGoldenWithdrawMaster;

    private final BackGoldenCodeSlave backGoldenCodeSlave;

    private final BackGoldenCodeMaster backGoldenCodeMaster;

    private final IdGlobalGenerator idGlobalGenerator;

    private final FrontUserAccountSlave frontUserAccountSlave;

    private final UsdgOfficialAccountSlave usdgOfficialAccountSlave;

    private final UserContractSlave userContractSlave;

    private final SQLUtilsss sqlUtilsss;

    private String BankAddress = ConfReadUtil.getProperty("blockchain.bankPubkey");

    private String BankPrikey = ConfReadUtil.getProperty("blockchain.bankPrikey");

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

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BackGoldenWithdrawServiceImpl(BackGoldenWithdrawSlave backGoldenWithdrawSlave, BackGoldenWithdrawMaster backGoldenWithdrawMaster, BackGoldenCodeSlave backGoldenCodeSlave, BackGoldenCodeMaster backGoldenCodeMaster, IdGlobalGenerator idGlobalGenerator, FrontUserAccountSlave frontUserAccountSlave, UsdgOfficialAccountSlave usdgOfficialAccountSlave, UserContractSlave userContractSlave, SQLUtilsss sqlUtilsss) {
        this.backGoldenWithdrawSlave = backGoldenWithdrawSlave;
        this.backGoldenWithdrawMaster = backGoldenWithdrawMaster;
        this.backGoldenCodeSlave = backGoldenCodeSlave;
        this.backGoldenCodeMaster = backGoldenCodeMaster;
        this.idGlobalGenerator = idGlobalGenerator;
        this.frontUserAccountSlave = frontUserAccountSlave;
        this.usdgOfficialAccountSlave = usdgOfficialAccountSlave;
        this.userContractSlave = userContractSlave;
        this.sqlUtilsss = sqlUtilsss;
    }

    @Override
    public PageDTO queryGoldenWithdrawInfo(GoldenWithdrawBO goldenWithdrawBO) {

        Integer page = goldenWithdrawBO.getPage();
        Integer rowNum = goldenWithdrawBO.getRowNum();

        BackGoldenWithdarwQuery backGoldenWithdarwQuery = new BackGoldenWithdarwQuery();
        backGoldenWithdarwQuery.setCstartTime(goldenWithdrawBO.getStartTime());
        backGoldenWithdarwQuery.setCendTime(goldenWithdrawBO.getEndTime());

        if(!StringUtils.isEmpty(goldenWithdrawBO.getStatus())){
            backGoldenWithdarwQuery.setStatus(goldenWithdrawBO.getStatus());
        }

        if(!StringUtils.isEmpty(goldenWithdrawBO.getPersonName())){
            backGoldenWithdarwQuery.setPersonNameLike(goldenWithdrawBO.getPersonName());
        }

        if(!StringUtils.isEmpty(goldenWithdrawBO.getUid())){
            backGoldenWithdarwQuery.setUid(goldenWithdrawBO.getUid());
        }

        if(!StringUtils.isEmpty(goldenWithdrawBO.getPhoneNumber())){
            backGoldenWithdarwQuery.setPhoneNumber(goldenWithdrawBO.getPhoneNumber());
        }

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<GoldenWithdraw> goldenWithdrawPage = backGoldenWithdrawSlave.findAll(backGoldenWithdarwQuery, pageable);

        List<GoldenWithdraw> list = goldenWithdrawPage == null ? Collections.emptyList() : goldenWithdrawPage.getContent();

        long totalPage = goldenWithdrawPage == null ? 0 : goldenWithdrawPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertGoldenCode(GoldenWithdrawBO goldenWithdrawBO) {
        //该黄金提取记录的状态是否是申请中
        Long id = goldenWithdrawBO.getId();

        //获取该提取单的详细信息
        GoldenWithdraw one = backGoldenWithdrawSlave.findOne(id);
        Integer status = one.getStatus();
        if(GoldenWithDrawEnum.TO_WITHDRAW.getStatus().equals(status)||
           GoldenWithDrawEnum.CHECKOUT.getStatus().equals(status)||
           GoldenWithDrawEnum.TO_AUDIT.getStatus().equals(status)||
           GoldenWithDrawEnum.WITHDRAW_FAIL.getStatus().equals(status)){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.ORDER_NOT_IN_APPLY));
        }

        //黄金编号重复检查？================>>>TODO，index helps

        //查看编号数量和对应的份数是否一致
        List<String> goldenCodes = goldenWithdrawBO.getGoldenCodes();
        Integer withdrawAmount = one.getWithdrawAmount();
        if(!withdrawAmount.equals(goldenCodes.size())){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.GOLDE_CODES_NUM_INCONSISTENT));
        }

        //检查黄金提取时间是否大于当前时间(否则该订单不能成功)
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Integer withdrawTime = one.getWithdrawTime();
        if(withdrawTime <= currentTime){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.WRONG_WITHDRAW_TIME));
        }

        //改当前提取信息的状态
        GoldenWithdraw goldenWithdraw = new GoldenWithdraw();
        goldenWithdraw.setStatus(GoldenWithDrawEnum.TO_WITHDRAW.getStatus());
        backGoldenWithdrawMaster.updateById(goldenWithdraw,id,"status");

        //插入对应的黄金编号
        List<BackGoldenCode> backGoldenCodeList = new ArrayList<>();
        for(String goldenCode : goldenCodes){
            BackGoldenCode backGoldenCode = new BackGoldenCode();
            backGoldenCode.setId(idGlobalGenerator.getSeqId(BackGoldenCode.class));
            backGoldenCode.setGoldenCode(goldenCode);
            backGoldenCode.setGoldenWithdrawId(id);
            backGoldenCode.setCtime(currentTime);
            backGoldenCode.setUtime(currentTime);
            backGoldenCodeList.add(backGoldenCode);
        }
        int success = 0;
        try {
            success = sqlUtilsss.executeInsertMany(backGoldenCodeList);
            System.out.println("执行成功条数："+success);
        } catch (Exception e) {
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.DUPLICATE_GOLD_CODE));
        }
        if(!withdrawAmount.equals(success)){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.GOLDE_CODES_NUM_INCONSISTENT));
        }
//        backGoldenCodeMaster.save(backGoldenCodeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  confirmCheckout(GoldenWithdrawBO goldenWithdrawBO) {
        //该黄金提取记录的状态是否是申请中
        Long id = goldenWithdrawBO.getId();

        //获取该提取单的详细信息
        GoldenWithdraw one = backGoldenWithdrawSlave.findOne(id);
        Integer status = one.getStatus();
        if(!GoldenWithDrawEnum.TO_WITHDRAW.getStatus().equals(status)){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.GOLD_ORDER_IN_TO_WITHDRAW));
        }
        //检查黄金提取时间是否已经过期
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Integer withdrawTime = one.getWithdrawTime();
        if(withdrawTime <= currentTime){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.GOLD_ORDER_EXPIRE));
        }
        GoldenWithdraw goldenWithdraw = new GoldenWithdraw();
        goldenWithdraw.setStatus(GoldenWithDrawEnum.CHECKOUT.getStatus());
        backGoldenWithdrawMaster.updateById(goldenWithdraw,id,"status");
    }


    @Override
    public List<BackGoldenCode> queryGoldenCode(GoldenWithdrawBO goldenWithdrawBO) {
        Long id = goldenWithdrawBO.getId();

        BackGoldenCodeQuery backGoldenCodeQuery = new BackGoldenCodeQuery();
        backGoldenCodeQuery.setGoldenWithdrawId(id);

        List<BackGoldenCode> all = backGoldenCodeSlave.findAll(backGoldenCodeQuery);
        return all;
    }

    @Override
    public List<GoldenWithdraw> queryAllOverdues() {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        BackGoldenWithdarwQuery backGoldenWithdarwQuery = new BackGoldenWithdarwQuery();
        backGoldenWithdarwQuery.setOverWithdrawTime(currentTime);
        backGoldenWithdarwQuery.setStatus(GoldenWithDrawEnum.TO_WITHDRAW.getStatus());
        List<GoldenWithdraw> overdues = backGoldenWithdrawSlave.findAll(backGoldenWithdarwQuery);
        return overdues;
    }

    /**
     * 定时器的方法，不需要多国语言支持
     * @param goldenWithdrawBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  dealWithOverdues(GoldenWithdrawBO goldenWithdrawBO) {
        Long id = goldenWithdrawBO.getId();
        Long uid = goldenWithdrawBO.getUid();
        Double chargeUSDG = goldenWithdrawBO.getChargeUSDG();
        Integer payUSDG = goldenWithdrawBO.getPayUSDG();
        //一半的手续费
        Double halfCharge = DecimalUtil.divide(chargeUSDG, 2, 8, RoundingMode.HALF_EVEN);
        //返还给可用资金部分
        Double backToUsable = DecimalUtil.add(payUSDG,halfCharge,8,RoundingMode.HALF_EVEN);
        //冻结金额部分扣除
        Double minusFreeze = DecimalUtil.add(payUSDG,chargeUSDG,8,RoundingMode.HALF_EVEN);

        //用户的账户变动
        Query nativeQuery = em.createNativeQuery("UPDATE usdg_user_account SET real_amount = real_amount-?,usable_amount = usable_amount + ?,freeze_amount = freeze_amount - ? WHERE uid=? AND type=?");
        nativeQuery.setParameter(1,halfCharge);
        nativeQuery.setParameter(2,backToUsable);
        nativeQuery.setParameter(3,minusFreeze);
        nativeQuery.setParameter(4,uid);
        nativeQuery.setParameter(5,CoinType.USDG);
        int success = nativeQuery.executeUpdate();
        if(success == 0){
            LOGGER.error("更新用户"+uid+"的USDG账户信息失败");
            throw new RuntimeException("更新用户账户信息失败");
        }

        //平台的账户变动
        Query nativeQuery2 = em.createNativeQuery("UPDATE usdg_official_account SET real_amount = real_amount + ?,usable_amount = usable_amount + ? WHERE type=?");
        nativeQuery2.setParameter(1,halfCharge);
        nativeQuery2.setParameter(2,halfCharge);
        nativeQuery2.setParameter(3,CoinType.USDG);
        int success2 = nativeQuery2.executeUpdate();
        if(success2 == 0){
            LOGGER.error("更新平台账户信息失败");
            throw new RuntimeException("更新平台账户信息失败");
        }

        //用户提取的黄金单子状态改成【提取失败】
        Query nativeQuery3 = em.createNativeQuery("UPDATE back_golden_withdraw SET status = ? WHERE id=?");
        nativeQuery3.setParameter(1,GoldenWithDrawEnum.WITHDRAW_FAIL.getStatus());
        nativeQuery3.setParameter(2,id);
        int success3 = nativeQuery3.executeUpdate();
        if(success3 == 0){
            LOGGER.error("更新黄金单号状态失败");
            throw new RuntimeException("更新黄金单号状态失败");
        }

        //用户合约里的公私钥信息
        UserContract userContract = userContractSlave.findOne(uid);

        //【走合约接口】
        //走合约的私钥
        String prikey = userContract.getPrivatekey();
        //走合约的公钥
        String pubkey = userContract.getPublickey();

        //走合约接口的方法从一个地址转入另外一个地址------TODO 走的是合约
        TransferCoin.transferCoin(pubkey,prikey,platformUsdgPubkey,halfCharge,SymbolId.USDG);
    }

    @Override
    public List<GoldenWithdraw> queryAllOverduesUnhandled() {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        BackGoldenWithdarwQuery backGoldenWithdarwQuery = new BackGoldenWithdarwQuery();
        backGoldenWithdarwQuery.setOverWithdrawTime(currentTime);
        backGoldenWithdarwQuery.setStatus(GoldenWithDrawEnum.APPLY.getStatus());
        List<GoldenWithdraw> overdues = backGoldenWithdrawSlave.findAll(backGoldenWithdarwQuery);
        return overdues;
    }

    /**
     * 定时器的方法，不需要多国语言支持
     * @param goldenWithdrawBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealWithOverduesUnhandled(GoldenWithdrawBO goldenWithdrawBO) {
        Long id = goldenWithdrawBO.getId();
        Long uid = goldenWithdrawBO.getUid();
        Double totalUSDG = goldenWithdrawBO.getTotalUSDG();

        //用户的账户变动
        Query nativeQuery = em.createNativeQuery("UPDATE usdg_user_account SET usable_amount = usable_amount + ?,freeze_amount = freeze_amount - ? WHERE uid=? AND type=?");
        nativeQuery.setParameter(1,totalUSDG);
        nativeQuery.setParameter(2,totalUSDG);
        nativeQuery.setParameter(3,uid);
        nativeQuery.setParameter(4,CoinType.USDG);
        int success = nativeQuery.executeUpdate();
        if(success == 0){
            LOGGER.error("更新用户"+uid+"的USDG账户信息失败");
            throw new RuntimeException("更新用户账户信息失败");
        }

        //用户提取的黄金单子状态改成【提取失败】
        Query nativeQuery2 = em.createNativeQuery("UPDATE back_golden_withdraw SET status = ? WHERE id=?");
        nativeQuery2.setParameter(1,GoldenWithDrawEnum.WITHDRAW_FAIL.getStatus());
        nativeQuery2.setParameter(2,id);
        int success3 = nativeQuery2.executeUpdate();
        if(success3 == 0){
            LOGGER.error("更新黄金单号状态失败");
            throw new RuntimeException("更新黄金单号状态失败");
        }

    }
}
