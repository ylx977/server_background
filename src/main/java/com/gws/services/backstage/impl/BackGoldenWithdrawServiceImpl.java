package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.GoldenWithDrawEnum;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackGoldenCode;
import com.gws.entity.backstage.GoldenWithdraw;
import com.gws.entity.backstage.GoldenWithdrawBO;
import com.gws.repositories.master.backstage.BackGoldenCodeMaster;
import com.gws.repositories.master.backstage.BackGoldenWithdrawMaster;
import com.gws.repositories.query.backstage.BackGoldenWithdarwQuery;
import com.gws.repositories.slave.backstage.BackGoldenCodeSlave;
import com.gws.repositories.slave.backstage.BackGoldenWithdrawSlave;
import com.gws.services.backstage.BackGoldenWithdrawService;
import com.gws.utils.cache.IdGlobalGenerator;
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
public class BackGoldenWithdrawServiceImpl implements BackGoldenWithdrawService {

    private final BackGoldenWithdrawSlave backGoldenWithdrawSlave;

    private final BackGoldenWithdrawMaster backGoldenWithdrawMaster;

    private final BackGoldenCodeSlave backGoldenCodeSlave;

    private final BackGoldenCodeMaster backGoldenCodeMaster;

    private final IdGlobalGenerator idGlobalGenerator;

    @Autowired
    public BackGoldenWithdrawServiceImpl(BackGoldenWithdrawSlave backGoldenWithdrawSlave, BackGoldenWithdrawMaster backGoldenWithdrawMaster, BackGoldenCodeSlave backGoldenCodeSlave, BackGoldenCodeMaster backGoldenCodeMaster, IdGlobalGenerator idGlobalGenerator) {
        this.backGoldenWithdrawSlave = backGoldenWithdrawSlave;
        this.backGoldenWithdrawMaster = backGoldenWithdrawMaster;
        this.backGoldenCodeSlave = backGoldenCodeSlave;
        this.backGoldenCodeMaster = backGoldenCodeMaster;
        this.idGlobalGenerator = idGlobalGenerator;
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
            backGoldenCode.setGoldenWithdrawIid(id);
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
}
