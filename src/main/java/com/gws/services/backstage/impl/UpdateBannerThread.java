package com.gws.services.backstage.impl;

import com.gws.services.backstage.BannerTableService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/25.
 */
public class UpdateBannerThread implements Runnable{

    public UpdateBannerThread(List<String> bannerUrls, String moveJson, String deleteJson, BannerTableService bannerTableService){
        this.bannerUrls = bannerUrls;
        this.moveJson = moveJson;
        this.deleteJson = deleteJson;
        this.bannerTableService = bannerTableService;
    }

    private List<String> bannerUrls;

    private String moveJson;

    private String deleteJson;

    private BannerTableService bannerTableService;

    @Override
    public void run() {
        //异步执行更新banner操作
        bannerTableService.updateBanner(bannerUrls,moveJson,deleteJson);
    }
}
