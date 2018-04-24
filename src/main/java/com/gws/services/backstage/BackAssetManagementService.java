package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FrontUserBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
public interface BackAssetManagementService {

    /**
     * 查询每个uid的详细资产信息
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUserAccount(FrontUserBO frontUserBO);

    /**
     * 查询前台用户的充币记录
     * @param frontUserBO
     * @return
     */
    PageDTO queryFrontUserRecharge(FrontUserBO frontUserBO);
}
