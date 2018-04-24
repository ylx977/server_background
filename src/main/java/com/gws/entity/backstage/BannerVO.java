package com.gws.entity.backstage;

import lombok.Data;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Data
public class BannerVO {

    private BannerConfig bannerConfig;

    private List<BannerTable> bannerTables;

}
