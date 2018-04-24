package com.gws.services.backstage;

import com.gws.entity.backstage.BannerBO;
import com.gws.entity.backstage.BannerVO;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
public interface BannerConfigService {

    /**
     * 轮播图的基本配置
     * @param bannerBO
     */
    void basicBannerConfig(BannerBO bannerBO);

    /**
     * 查询所有轮播图信息
     * @return
     */
    BannerVO queryBanners();
}
