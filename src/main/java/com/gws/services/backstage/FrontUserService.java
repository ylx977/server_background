package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FrontUserBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
public interface FrontUserService {

    /**
     * 后台用户查看前台用户信息
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUsers(FrontUserBO frontUserBO);

    /**
     * 查询前台用户修改的申请信息
     * @param frontUserBO
     * @return
     */
    PageDTO queryApplyInfo(FrontUserBO frontUserBO);

    /**
     * 同意前台用户的修改申请
     * @param frontUserBO
     */
    void approveApply(FrontUserBO frontUserBO);

    /**
     * 拒绝前台用户的修改申请
     * @param frontUserBO
     */
    void rejectApply(FrontUserBO frontUserBO);

    /**
     * 更新前台用户的联系方式(手机号和邮箱地址)
     * @param frontUserBO
     */
    void updateFrontUserInfo(FrontUserBO frontUserBO);

    /**
     * 查询前台用户修改的申请过的历史记录信息
     * @param frontUserBO
     * @return
     */
    PageDTO queryApplyHistory(FrontUserBO frontUserBO);
}
