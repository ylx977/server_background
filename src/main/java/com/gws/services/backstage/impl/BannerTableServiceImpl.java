package com.gws.services.backstage.impl;

import com.gws.repositories.master.backstage.BannerTableMaster;
import com.gws.repositories.slave.backstage.BannerTableSlave;
import com.gws.services.backstage.BannerTableService;
import com.gws.services.oss.AliossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Service
public class BannerTableServiceImpl implements BannerTableService {

    private final BannerTableSlave bannerTableSlave;

    private final BannerTableMaster bannerTableMaster;

    private final AliossService aliossService;

    @Value("${ali.oss.bucket}")
    public String bucket;

    @Autowired
    public BannerTableServiceImpl(BannerTableSlave bannerTableSlave, BannerTableMaster bannerTableMaster, AliossService aliossService) {
        this.bannerTableSlave = bannerTableSlave;
        this.bannerTableMaster = bannerTableMaster;
        this.aliossService = aliossService;
    }

    @Override
    public void updateBanner(MultipartFile[] files, String moveJson, String deleteJson) {
        aliossService.uploadFiles(files,bucket);
    }
}
