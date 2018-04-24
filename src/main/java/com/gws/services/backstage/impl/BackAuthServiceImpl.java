package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackAuthes;
import com.gws.entity.backstage.BackUserBO;
import com.gws.repositories.master.backstage.*;
import com.gws.repositories.query.backstage.BackAuthesQuery;
import com.gws.repositories.slave.backstage.*;
import com.gws.services.backstage.BackAuthService;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Service
public class BackAuthServiceImpl implements BackAuthService{

    private final BackAuthesSlave backAuthesSlave;

    private final RedisTemplate<Object,Object> redisTemplate;

    private final RedisUtil redisUtil;

    @Autowired
    public BackAuthServiceImpl(BackAuthesSlave backAuthesSlave, RedisTemplate<Object, Object> redisTemplate, RedisUtil redisUtil) {
        this.backAuthesSlave = backAuthesSlave;
        this.redisTemplate = redisTemplate;
        this.redisUtil = redisUtil;
    }

    @Override
    public PageDTO queryAuthorities(BackUserBO backUserBO) {
        Integer page = backUserBO.getPage();
        Integer rowNum = backUserBO.getRowNum();

//        String allAuthesString = (String) redisUtil.get(RedisConfig.ALL_BACK_AUTHES_PREFIX);

//        if(StringUtils.isEmpty(allAuthesString)){
            BackAuthesQuery backAuthesQuery = new BackAuthesQuery();
            if(!StringUtils.isEmpty(backUserBO.getAuthName())){
                backAuthesQuery.setAuthNameLike(backUserBO.getAuthName());
            }
            backAuthesQuery.setCstartTime(backUserBO.getStartTime());
            backAuthesQuery.setCendTime(backUserBO.getEndTime());
            Sort sort = new Sort(Sort.Direction.DESC,"ctime");
            Pageable pageable = new PageRequest(page-1,rowNum,sort);
            Page<BackAuthes> backAuthesPage = backAuthesSlave.findAll(backAuthesQuery, pageable);
            List<BackAuthes> list = backAuthesPage == null ? Collections.EMPTY_LIST : backAuthesPage.getContent();
            long totalPage = backAuthesPage == null ? 0 : backAuthesPage.getTotalElements();
            return PageDTO.getPagination(totalPage,list);
//        }

    }

}
