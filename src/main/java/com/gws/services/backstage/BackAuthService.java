package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackUserBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
public interface BackAuthService {

    /**
     * 查询后台的权限信息（分页）
     * @param backUserBO
     * @return
     */
    PageDTO queryAuthorities(BackUserBO backUserBO);
}
