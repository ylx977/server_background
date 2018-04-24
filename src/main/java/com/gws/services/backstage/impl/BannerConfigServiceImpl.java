package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.entity.backstage.BannerBO;
import com.gws.entity.backstage.BannerConfig;
import com.gws.entity.backstage.BannerTable;
import com.gws.entity.backstage.BannerVO;
import com.gws.repositories.master.backstage.BannerConfigMaster;
import com.gws.repositories.slave.backstage.BannerConfigSlave;
import com.gws.repositories.slave.backstage.BannerTableSlave;
import com.gws.services.backstage.BannerConfigService;
import com.gws.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Service
public class BannerConfigServiceImpl implements BannerConfigService {

    private final BannerConfigSlave bannerConfigSlave;

    private final BannerConfigMaster bannerConfigMaster;

    private final BannerTableSlave bannerTableSlave;

    private final RedisUtil redisUtil;

    @Autowired
    public BannerConfigServiceImpl(BannerConfigSlave bannerConfigSlave, BannerConfigMaster bannerConfigMaster, RedisUtil redisUtil, BannerTableSlave bannerTableSlave) {
        this.bannerConfigSlave = bannerConfigSlave;
        this.bannerConfigMaster = bannerConfigMaster;
        this.redisUtil = redisUtil;
        this.bannerTableSlave = bannerTableSlave;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void basicBannerConfig(BannerBO bannerBO) {
        Integer displayOrder = bannerBO.getDisplayOrder();
        Integer displayInterval = bannerBO.getDisplayInterval();
        String bannerConfigString = (String) redisUtil.get(RedisConfig.BANNER_BASIC_CONFIG);

        if(StringUtils.isEmpty(bannerConfigString)){
            BannerConfig one = bannerConfigSlave.findOne(1L);
            if(one != null){
                int flag = 0;
                List<String> properties = new ArrayList<>();
                BannerConfig bannerConfig = new BannerConfig();
                if(displayOrder != null){
                    one.setDisplayOrder(displayOrder);
                    bannerConfig.setDisplayOrder(displayOrder);
                    properties.add("displayOrder");
                    flag++;
                }
                if(displayInterval != null){
                    one.setDisplayInterval(displayInterval);
                    bannerConfig.setDisplayInterval(displayInterval);
                    properties.add("displayInterval");
                    flag++;
                }
                if(flag == 0){
                    throw new RuntimeException("播放顺序和播放间隔参数必须至少设置一个");
                }
                int success = bannerConfigMaster.updateById(bannerConfig, 1L, properties.toArray(new String[0]));
                if(success == 0){
                    throw new RuntimeException("更新基本配置失败");
                }
                redisUtil.set(RedisConfig.BANNER_BASIC_CONFIG,JSON.toJSONString(one));
                return;
            }else{
                if(displayOrder == null || displayInterval == null){
                    throw new RuntimeException("初次设置参数，播放顺序和播放间隔参数必须都设置");
                }
                BannerConfig bannerConfig = new BannerConfig();
                bannerConfig.setId(1L);
                bannerConfig.setDisplayOrder(displayOrder);
                bannerConfig.setDisplayInterval(displayInterval);
                bannerConfigMaster.save(bannerConfig);
                redisUtil.set(RedisConfig.BANNER_BASIC_CONFIG,JSON.toJSONString(bannerConfig));
                return;
            }
        }
        int flag = 0;
        List<String> properties = new ArrayList<>();
        BannerConfig bannerConfig = new BannerConfig();
        if(displayOrder != null){
            bannerConfig.setDisplayOrder(displayOrder);
            properties.add("displayOrder");
            flag++;
        }
        if(displayInterval != null){
            bannerConfig.setDisplayInterval(displayInterval);
            properties.add("displayInterval");
            flag++;
        }
        if(flag == 0){
            throw new RuntimeException("播放顺序和播放间隔参数必须至少设置一个");
        }
        int success = bannerConfigMaster.updateById(bannerConfig, 1L, properties.toArray(new String[0]));
        if(success == 0){
            throw new RuntimeException("更新基本配置失败");
        }
        redisUtil.delete(RedisConfig.BANNER_BASIC_CONFIG);
    }

    @Override
    public BannerVO queryBanners() {
        BannerVO bannerVO = new BannerVO();

        String bannerConfigString = (String) redisUtil.get(RedisConfig.BANNER_BASIC_CONFIG);

        if(StringUtils.isEmpty(bannerConfigString)){
            BannerConfig one = bannerConfigSlave.findOne(1L);
            if(one != null){
                bannerVO.setBannerConfig(one);
                redisUtil.set(RedisConfig.BANNER_BASIC_CONFIG,JSON.toJSONString(one));
            }
        }else{
            bannerVO.setBannerConfig(JSON.parseObject(bannerConfigString, BannerConfig.class));
        }

        List<BannerTable> all = bannerTableSlave.findAll();
        if(all == null || all.size() == 0){
            bannerVO.setBannerTables(Collections.emptyList());
        }else{
            bannerVO.setBannerTables(all);
        }
        return bannerVO;
    }
}
