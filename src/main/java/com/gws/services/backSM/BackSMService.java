package com.gws.services.backSM;

import com.gws.entity.backstage.BackUserBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/15.
 */
public interface BackSMService {
    /**
     * 通过短信验证码验证的方式去冻结用户或者解冻用户
     * @param backUserBO
     */
    void updateBackUserStatus(BackUserBO backUserBO);
}
