package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.entity.backstage.BannerTable;
import com.gws.entity.backstage.upload.Move;
import com.gws.repositories.master.backstage.BannerTableMaster;
import com.gws.repositories.query.backstage.BannerTableQuery;
import com.gws.repositories.slave.backstage.BannerTableSlave;
import com.gws.services.backstage.BannerTableService;
import com.gws.services.oss.AliossService;
import com.gws.utils.cache.IdGlobalGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Service
public class BannerTableServiceImpl implements BannerTableService {

    private final BannerTableSlave bannerTableSlave;

    private final BannerTableMaster bannerTableMaster;

    private final AliossService aliossService;

    private final IdGlobalGenerator idGlobalGenerator;

    @Value("${ali.oss.bucket}")
    public String bucket;

    @Autowired
    public BannerTableServiceImpl(BannerTableSlave bannerTableSlave, BannerTableMaster bannerTableMaster, AliossService aliossService, ExecutorService executorService, IdGlobalGenerator idGlobalGenerator) {
        this.bannerTableSlave = bannerTableSlave;
        this.bannerTableMaster = bannerTableMaster;
        this.aliossService = aliossService;
        this.idGlobalGenerator = idGlobalGenerator;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBanner(List<String> bannerUrls, String moveJson, String deleteJson) {
        //deleteJson非空的情况
        if(!StringUtils.isEmpty(deleteJson)){
            List<Long> deleteIds = JSON.parseArray(deleteJson,Long.class);
            if(deleteIds.size() != 0){
                //确保有id再删，没数据删个毛啊
                BannerTableQuery bannerTableQuery = new BannerTableQuery();
                bannerTableQuery.setIds(deleteIds);
                //将要删除的banner信息查询出来
                List<BannerTable> deleteList = bannerTableSlave.findAll(bannerTableQuery);
                //然后删除所有的banner信息
                bannerTableMaster.delete(deleteList);
            }
        }

        //moveJson非空的情况(那files必定是有文件的)
        if(!StringUtils.isEmpty(moveJson)){
            List<Move> moveList = JSON.parseArray(moveJson, Move.class);
            List<BannerTable> insertBannerTableList = new ArrayList<>();
            for(int i = 0 ; i < moveList.size(); i++){
                Move move = moveList.get(i);
                if(move.getId()==null){
                    Integer orders = move.getOrders();
                    BannerTable bannerTable = new BannerTable();
                    bannerTable.setId(idGlobalGenerator.getSeqId(BannerTable.class));
                    bannerTable.setOrders(orders);
                    bannerTable.setBannerUrl(bannerUrls.get(i));
                    insertBannerTableList.add(bannerTable);
                }
                if(move.getId()!=null){
                    Long id = move.getId();
                    Integer orders = move.getOrders();
                    BannerTable bannerTable = new BannerTable();
                    bannerTable.setOrders(orders);
                    bannerTableMaster.updateById(bannerTable,id,"orders");
                }
            }
            if(insertBannerTableList.size() != 0){
                bannerTableMaster.save(insertBannerTableList);
            }
        }
    }

    @Override
    public void checkParameter(MultipartFile[] files, String moveJson, String deleteJson) {
        int flag = 0;

        //deleteJson非空的情况
        if(!StringUtils.isEmpty(deleteJson)){
            JSON.parseArray(deleteJson,Long.class);
            flag++;
        }
        //moveJson非空的情况(那files必定是有文件的)
        if(!StringUtils.isEmpty(moveJson)){
            int count = 0;
            List<Move> moveList = JSON.parseArray(moveJson, Move.class);
            for(Move move : moveList){
                if(move.getId()==null){
                    count++;
                }
            }
            if(files.length != count){
                throw new RuntimeException("文件上传数量与实际不一致");
            }
            flag++;
        }
        if(flag==0){
            throw new RuntimeException("参数不能全为空");
        }
    }

}
