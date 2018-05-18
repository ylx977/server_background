package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.CoinType;
import com.gws.common.constants.backstage.GoldenWithDrawEnum;
import com.gws.common.constants.backstage.SymbolId;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.master.backstage.BackGoldenCodeMaster;
import com.gws.repositories.master.backstage.BackGoldenWithdrawMaster;
import com.gws.repositories.query.backstage.BackGoldenCodeQuery;
import com.gws.repositories.query.backstage.BackGoldenWithdarwQuery;
import com.gws.repositories.query.backstage.UsdgOfficialAccountQuery;
import com.gws.repositories.query.backstage.UsdgUserAccountQuery;
import com.gws.repositories.slave.backstage.BackGoldenCodeSlave;
import com.gws.repositories.slave.backstage.BackGoldenWithdrawSlave;
import com.gws.repositories.slave.backstage.FrontUserAccountSlave;
import com.gws.repositories.slave.backstage.UsdgOfficialAccountSlave;
import com.gws.services.backstage.BackGoldenWithdrawService;
import com.gws.utils.blockchain.BlockUtils;
import com.gws.utils.blockchain.Protobuf4EdsaUtils;
import com.gws.utils.blockchain.ProtobufBean;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.decimal.DecimalUtil;
import com.gws.utils.http.ConfReadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private String BankAddress = ConfReadUtil.getProperty("blockchain.bankPubkey");

    private String BankPrikey = ConfReadUtil.getProperty("blockchain.bankPrikey");

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BackGoldenWithdrawServiceImpl(BackGoldenWithdrawSlave backGoldenWithdrawSlave, BackGoldenWithdrawMaster backGoldenWithdrawMaster, BackGoldenCodeSlave backGoldenCodeSlave, BackGoldenCodeMaster backGoldenCodeMaster, IdGlobalGenerator idGlobalGenerator, FrontUserAccountSlave frontUserAccountSlave, UsdgOfficialAccountSlave usdgOfficialAccountSlave) {
        this.backGoldenWithdrawSlave = backGoldenWithdrawSlave;
        this.backGoldenWithdrawMaster = backGoldenWithdrawMaster;
        this.backGoldenCodeSlave = backGoldenCodeSlave;
        this.backGoldenCodeMaster = backGoldenCodeMaster;
        this.idGlobalGenerator = idGlobalGenerator;
        this.frontUserAccountSlave = frontUserAccountSlave;
        this.usdgOfficialAccountSlave = usdgOfficialAccountSlave;
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
        if(!GoldenWithDrawEnum.APPLY.getStatus().equals(status)){
            throw new RuntimeException("该黄金提取单已经被申请提取过了");
        }

        //黄金编号重复检查？================>>>TODO

        //查看编号数量和对应的份数是否一致
        List<String> goldenCodes = goldenWithdrawBO.getGoldenCodes();
        Integer withdrawAmount = one.getWithdrawAmount();
        if(!withdrawAmount.equals(goldenCodes.size())){
            throw new RuntimeException("黄金编号的数量和提取的份数不一致");
        }

        //检查黄金提取时间是否大于当前时间(否则该订单不能成功)
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Integer withdrawTime = one.getWithdrawTime();
        if(withdrawTime <= currentTime){
            throw new RuntimeException("提取黄金时间必须大于当前时间");
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
            backGoldenCodeList.add(backGoldenCode);
        }
        backGoldenCodeMaster.save(backGoldenCodeList);
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
            throw new RuntimeException("该黄金提取单还未处于待提取状态");
        }
        //检查黄金提取时间是否已经过期
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Integer withdrawTime = one.getWithdrawTime();
        if(withdrawTime <= currentTime){
            throw new RuntimeException("您的黄金提取单已经过期");
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealWithOverdues(GoldenWithdrawBO goldenWithdrawBO) {
        Long uid = goldenWithdrawBO.getUid();
        Double chargeUSDG = goldenWithdrawBO.getChargeUSDG();
        Integer payUSDG = goldenWithdrawBO.getPayUSDG();
        //一半的手续费
        Double halfCharge = DecimalUtil.divide(chargeUSDG, 2, 8, RoundingMode.HALF_EVEN);
        //返还给可用资金部分
        Double backToUsable = DecimalUtil.add(payUSDG,halfCharge,8,RoundingMode.HALF_EVEN);
        //冻结金额部分扣除
        Double minuxFreeze = DecimalUtil.add(payUSDG,chargeUSDG,8,RoundingMode.HALF_EVEN);

        //用户的账户变动
        Query nativeQuery = em.createNativeQuery("UPDATE usdg_user_account SET real_amount = real_amount-?,usable_amount = usable_amount+?,freeze_amount = freeze_amount-? WHERE uid=? AND type=?");
        nativeQuery.setParameter(1,halfCharge);
        nativeQuery.setParameter(2,backToUsable);
        nativeQuery.setParameter(3,minuxFreeze);
        nativeQuery.setParameter(4,uid);
        nativeQuery.setParameter(5,CoinType.USDG);
        int success = nativeQuery.executeUpdate();
        if(success == 0){
            LOGGER.error("更新用户"+uid+"的USDG账户信息失败");
            throw new RuntimeException("更新用户账户信息失败");
        }

        //平台的账户变动
        Query nativeQuery2 = em.createNativeQuery("UPDATE usdg_officila_account SET real_amount = real_amount+?,usable_amount = usable_amount+? WHERE type=?");
        nativeQuery2.setParameter(1,halfCharge);
        nativeQuery2.setParameter(2,halfCharge);
        nativeQuery2.setParameter(3,CoinType.USDG);
        int success2 = nativeQuery2.executeUpdate();
        if(success2 == 0){
            LOGGER.error("更新平台账户信息失败");
            throw new RuntimeException("更新平台账户信息失败");
        }

        UsdgUserAccountQuery usdgUserAccountQuery = new UsdgUserAccountQuery();
        usdgUserAccountQuery.setUid(uid);
        usdgUserAccountQuery.setType(CoinType.USDG);
        //用户的账户信息
        UsdgUserAccount userAccount = frontUserAccountSlave.findOne(usdgUserAccountQuery);
        String publicKey = userAccount.getPublicKey();
        String privateKey = userAccount.getPrivateKey();
        UsdgOfficialAccountQuery usdgOfficialAccountQuery = new UsdgOfficialAccountQuery();
        usdgOfficialAccountQuery.setType(CoinType.USDG);
        UsdgOfficialAccount officialAccount = usdgOfficialAccountSlave.findOne(usdgOfficialAccountQuery);
        String officialPubKey = officialAccount.getPublicKey();
        long amount = (long)(DecimalUtil.multiply(halfCharge,100000000,8,RoundingMode.HALF_EVEN));


        //====================================================》todo 第1次调用区块链接口，用户打给银行
        ProtobufBean protobufBean = Protobuf4EdsaUtils.requestTransfer(privateKey, SymbolId.USDG, publicKey, BankAddress, amount);
        String jsonResult = BlockUtils.sendPostParam(protobufBean);
        boolean flag = BlockUtils.vilaResult(jsonResult);
        if(!flag){
            String errorMessage = BlockUtils.getErrorMessage(jsonResult);
            LOGGER.error("区块链出错:"+errorMessage);
            throw new RuntimeException();
        }

        //====================================================》todo 第2次调用区块链接口，用户打给银行
        ProtobufBean protobufBean2 = Protobuf4EdsaUtils.requestTransfer(BankPrikey, SymbolId.USDG, BankAddress, officialPubKey, amount);
        String jsonResult2 = BlockUtils.sendPostParam(protobufBean2);
        boolean flag2 = BlockUtils.vilaResult(jsonResult2);
        if(!flag2){
            String errorMessage = BlockUtils.getErrorMessage(jsonResult2);
            LOGGER.error("区块链出错:"+errorMessage);

            //-------》todo 如果银行打给平台出错这里还要银行返回刚才用户打给银行的钱
            ProtobufBean protobufBean3 = Protobuf4EdsaUtils.requestTransfer(BankPrikey, SymbolId.USDG, BankAddress, publicKey, amount);
            String jsonResult3 = BlockUtils.sendPostParam(protobufBean3);
            boolean flag3 = BlockUtils.vilaResult(jsonResult3);
            if(!flag3){
                String errorMessage3 = BlockUtils.getErrorMessage(jsonResult3);
                LOGGER.error("区块链出错:"+errorMessage3);
                //====>todo 这里再出错就麻烦了
            }

            throw new RuntimeException();
        }



    }
}
