package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.FrontUserApplyEnum;
import com.gws.common.constants.backstage.FrontUserApplyStatusEnum;
import com.gws.common.constants.backstage.FrontUserStatus;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FrontUser;
import com.gws.entity.backstage.FrontUserApplyInfo;
import com.gws.entity.backstage.FrontUserBO;
import com.gws.repositories.master.backstage.FrontUserApplyInfoMaster;
import com.gws.repositories.master.backstage.FrontUserMaster;
import com.gws.repositories.query.backstage.FrontUserApplyInfoQuery;
import com.gws.repositories.query.backstage.FrontUserQuery;
import com.gws.repositories.slave.backstage.FrontUserApplyInfoSlave;
import com.gws.repositories.slave.backstage.FrontUserSlave;
import com.gws.services.backstage.FrontUserService;
import com.gws.utils.validate.ValidationUtil;
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
public class FrontUserServiceImpl implements FrontUserService {

    private final FrontUserSlave frontUserSlave;

    private final FrontUserMaster frontUserMaster;

    private final FrontUserApplyInfoSlave frontUserApplyInfoSlave;

    private final FrontUserApplyInfoMaster frontUserApplyInfoMaster;

    @Autowired
    public FrontUserServiceImpl(FrontUserSlave frontUserSlave, FrontUserMaster frontUserMaster, FrontUserApplyInfoSlave frontUserApplyInfoSlave, FrontUserApplyInfoMaster frontUserApplyInfoMaster) {
        this.frontUserSlave = frontUserSlave;
        this.frontUserMaster = frontUserMaster;
        this.frontUserApplyInfoSlave = frontUserApplyInfoSlave;
        this.frontUserApplyInfoMaster = frontUserApplyInfoMaster;
    }

    @Override
    public PageDTO queryFrontUsers(FrontUserBO frontUserBO) {

        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();

        FrontUserQuery frontUserQuery = new FrontUserQuery();

        if(!StringUtils.isEmpty(frontUserBO.getEmail())){
            frontUserQuery.setEmailAddressLike(frontUserBO.getEmail());
        }

        if(!StringUtils.isEmpty(frontUserBO.getUsername())){
            frontUserQuery.setUsernameLike(frontUserBO.getUsername());
        }

        if(!StringUtils.isEmpty(frontUserBO.getPhoneNumber())){
            frontUserQuery.setPhoneNumberLike(frontUserBO.getPhoneNumber());
        }

        if(!StringUtils.isEmpty(frontUserBO.getUid())){
            frontUserQuery.setUid(frontUserBO.getUid());
        }
        frontUserQuery.setCstartTime(frontUserBO.getStartTime());
        frontUserQuery.setCendTime(frontUserBO.getEndTime());

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<FrontUser> frontUserPage = frontUserSlave.findAll(frontUserQuery, pageable);

        List<FrontUser> list = frontUserPage == null ? Collections.emptyList() : frontUserPage.getContent();
        long totalPage = frontUserPage == null ? 0 : frontUserPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    public PageDTO queryApplyInfo(FrontUserBO frontUserBO) {

        Integer page = frontUserBO.getPage();
        Integer rowNum = frontUserBO.getRowNum();

        FrontUserApplyInfoQuery frontUserApplyInfoQuery = new FrontUserApplyInfoQuery();

        if(!StringUtils.isEmpty(frontUserBO.getPhoneNumber())){
            frontUserApplyInfoQuery.setPhoneNumberLike(frontUserBO.getPhoneNumber());
        }

        if(!StringUtils.isEmpty(frontUserBO.getUid())){
            frontUserApplyInfoQuery.setUid(frontUserBO.getUid());
        }
        frontUserApplyInfoQuery.setCstartTime(frontUserBO.getStartTime());
        frontUserApplyInfoQuery.setCendTime(frontUserBO.getEndTime());

        //按照创建时间倒序排列
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1,rowNum,sort);
        Page<FrontUserApplyInfo> frontUserApplyInfoPage = frontUserApplyInfoSlave.findAll(frontUserApplyInfoQuery, pageable);

        List<FrontUserApplyInfo> list = frontUserApplyInfoPage == null ? Collections.emptyList() : frontUserApplyInfoPage.getContent();
        long totalPage = frontUserApplyInfoPage == null ? 0 : frontUserApplyInfoPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveApply(FrontUserBO frontUserBO) {
        Long id = frontUserBO.getId();
        //先查询该申请信息进行检查
        FrontUserApplyInfo one = frontUserApplyInfoSlave.findOne(id);
        if(null == one){
            throw new RuntimeException("该申请单不存在");
        }
        if(!one.getApplyStatus().equals(FrontUserApplyStatusEnum.TO_CHECK.getCode())){
            throw new RuntimeException("该申请单已经审核过了");
        }

        Integer changeType = one.getChangeType();
        Long uid = one.getUid();
        int success = 0;

        FrontUserQuery frontUserQuery = new FrontUserQuery();
        frontUserQuery.setUid(uid);
        //确保不是去修改已经被删除的用户信息
        frontUserQuery.setUserStatus(FrontUserStatus.NORMAL);

        //如果是修改手机
        if(FrontUserApplyEnum.PHONE.getCode().equals(changeType)){
            FrontUser frontUser = new FrontUser();
            frontUser.setPhoneNumber(one.getNewInfo());
            success = frontUserMaster.update(frontUser, frontUserQuery,"phoneNumber");
        }

        //如果是修改邮箱
        if(FrontUserApplyEnum.EMAIL.getCode().equals(changeType)){
            FrontUser frontUser = new FrontUser();
            frontUser.setEmailAddress(one.getNewInfo());
            success = frontUserMaster.update(frontUser, frontUserQuery,"emailAddress");
        }
        if(success == 0){
            throw new RuntimeException("更新用户信息失败");
        }

        //再将申请信息的状态改成同意状态
        FrontUserApplyInfo frontUserApplyInfo = new FrontUserApplyInfo();
        frontUserApplyInfo.setApplyStatus(FrontUserApplyStatusEnum.APPROVE.getCode());
        int success2 = frontUserApplyInfoMaster.updateById(frontUserApplyInfo, id, "applyStatus");
        if(success2 == 0){
            throw new RuntimeException("更新状态失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectApply(FrontUserBO frontUserBO) {
        Long id = frontUserBO.getId();
        //先查询该申请信息进行检查
        FrontUserApplyInfo one = frontUserApplyInfoSlave.findOne(id);
        if(null == one){
            throw new RuntimeException("该申请单不存在");
        }
        if(!one.getApplyStatus().equals(FrontUserApplyStatusEnum.TO_CHECK.getCode())){
            throw new RuntimeException("该申请单已经审核过了");
        }

        FrontUserApplyInfo frontUserApplyInfo = new FrontUserApplyInfo();
        frontUserApplyInfo.setApplyStatus(FrontUserApplyStatusEnum.REJECT.getCode());
        int success = frontUserApplyInfoMaster.updateById(frontUserApplyInfo, id, "applyStatus");
        System.out.println("success:"+success);
        if(success == 0){
            throw new RuntimeException("更新状态失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFrontUserInfo(FrontUserBO frontUserBO) {
        Long uid = frontUserBO.getUid();
        String phoneNumber = frontUserBO.getPhoneNumber();
        String email = frontUserBO.getEmail();

        FrontUser one = frontUserSlave.findOne(uid);
        if(FrontUserStatus.FREEZE.equals(one.getUserStatus())){
            throw new RuntimeException("该用户已被删除");
        }

        FrontUser frontUser = new FrontUser();
        int flag = 0;
        List<String> properties = new ArrayList<>();
        if(!StringUtils.isEmpty(phoneNumber)){
            ValidationUtil.checkBlankString(phoneNumber, RegexConstant.PHONE_REGEX);
            frontUser.setPhoneNumber(phoneNumber);
            properties.add("phoneNumber");
            flag++;
        }

        if(!StringUtils.isEmpty(email)){
            ValidationUtil.checkBlankString(email, RegexConstant.EMAIL_REGEX);
            frontUser.setEmailAddress(email);
            properties.add("emailAddress");
            flag++;
        }

        if(flag == 0){
            throw new RuntimeException("请填写必要的参数信息");
        }

        int success = frontUserMaster.updateById(frontUser, uid, properties.toArray(new String[0]));
        if(success == 0){
            throw new RuntimeException("更新用户信息失败");
        }
    }


}
