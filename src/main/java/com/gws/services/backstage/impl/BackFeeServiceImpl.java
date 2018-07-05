package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.CoinType2;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.configuration.backstage.LangConfig;
import com.gws.entity.GoldFee;
import com.gws.entity.backstage.CoinFee;
import com.gws.entity.backstage.FeeBO;
import com.gws.repositories.master.backstage.BackCoinFeeMaster;
import com.gws.repositories.master.backstage.BackGoldFeeMaster;
import com.gws.repositories.query.backstage.BackCoinQuery;
import com.gws.repositories.slave.backstage.BackCoinFeeSlave;
import com.gws.repositories.slave.backstage.BackGoldFeeSlave;
import com.gws.services.backstage.BackFeeService;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/27.
 */
@Service
public class BackFeeServiceImpl implements BackFeeService{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackFeeServiceImpl.class);

    private final BackCoinFeeMaster backCoinFeeMaster;

    private final BackCoinFeeSlave backCoinFeeSlave;

    private final BackGoldFeeMaster backGoldFeeMaster;

    private final BackGoldFeeSlave backGoldFeeSlave;

    private final RedisUtil redisUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BackFeeServiceImpl(BackCoinFeeMaster backCoinFeeMaster, BackCoinFeeSlave backCoinFeeSlave, BackGoldFeeMaster backGoldFeeMaster, BackGoldFeeSlave backGoldFeeSlave, RedisUtil redisUtil){
        this.backCoinFeeMaster = backCoinFeeMaster;
        this.backCoinFeeSlave = backCoinFeeSlave;
        this.backGoldFeeMaster = backGoldFeeMaster;
        this.backGoldFeeSlave = backGoldFeeSlave;
        this.redisUtil = redisUtil;
    }

    @Override
    public CoinFee showBTYUSDGTradeFee() {
        Object coinFeeString = redisUtil.get(RedisConfig.BTY_COIN_FEE);
        if(StringUtils.isEmpty(coinFeeString)){
            BackCoinQuery backCoinQuery = new BackCoinQuery();
            backCoinQuery.setName(CoinType2.BTY);
            CoinFee one = backCoinFeeSlave.findOne(backCoinQuery);
            redisUtil.set(RedisConfig.BTY_COIN_FEE,JSON.toJSONString(one));
            return one;
        }else{
            return JSON.parseObject(coinFeeString.toString(),CoinFee.class);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setBTYUSDGTradeFee(FeeBO feeBO) {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Double buyTradeFee = feeBO.getBuyTradeFee();
        Double sellTradeFee = feeBO.getSellTradeFee();

        BackCoinQuery backCoinQuery = new BackCoinQuery();
        backCoinQuery.setName(CoinType2.BTY);
        CoinFee one = backCoinFeeSlave.findOne(backCoinQuery);
        if(one == null){
            CoinFee coinFee = new CoinFee();
            coinFee.setName(CoinType2.BTY);
            coinFee.setBuyTrade(buyTradeFee);
            coinFee.setSellTrade(sellTradeFee);
            backCoinFeeMaster.save(coinFee);
            return;
        }

        CoinFee coinFee = new CoinFee();
        coinFee.setBuyTrade(buyTradeFee);
        coinFee.setSellTrade(sellTradeFee);
        coinFee.setUtime(currentTime);

        int success = backCoinFeeMaster.update(coinFee, backCoinQuery, "buyTrade", "sellTrade","utime");
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_COINFEE_FAIL));
        }

        //要删缓存中的数据
        redisUtil.delete(RedisConfig.BTY_COIN_FEE);
    }

    @Override
    public Map<String, Object> showDrawFee() {
        Object coinDrawFeeString = redisUtil.get(RedisConfig.COIN_DRAW_FEE);
        if(StringUtils.isEmpty(coinDrawFeeString)){
            List<CoinFee> all = backCoinFeeSlave.findAll();
            Map<String,Object> map = new HashMap<>();
            for (CoinFee coinFee : all){
                String name = coinFee.getName();
                Double draw = coinFee.getDraw();
                map.put(name,draw);
            }
            redisUtil.set(RedisConfig.COIN_DRAW_FEE,JSON.toJSONString(map));
            return map;
        }else{
            return JSON.parseObject(coinDrawFeeString.toString(),Map.class);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDrawFee(FeeBO feeBO) {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        String coinName = feeBO.getCoinType();
        Double draw = feeBO.getDraw();

        BackCoinQuery backCoinQuery1 = new BackCoinQuery();
        backCoinQuery1.setName(coinName);
        CoinFee one = backCoinFeeSlave.findOne(backCoinQuery1);
        if(one == null){
            CoinFee coinFee = new CoinFee();
            coinFee.setName(coinName);
            coinFee.setDraw(draw);
            backCoinFeeMaster.save(coinFee);
            return;
        }

        CoinFee coinFee = new CoinFee();
        coinFee.setDraw(draw);
        coinFee.setUtime(currentTime);

        BackCoinQuery backCoinQuery = new BackCoinQuery();
        backCoinQuery.setName(coinName);
        int success = backCoinFeeMaster.update(coinFee, backCoinQuery, "draw", "utime");
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_COINFEE_FAIL));
        }

        //删缓存中的数据
        redisUtil.delete(RedisConfig.COIN_DRAW_FEE);
    }

    @Override
    public Map<String, Object> showGoldDrawFee(FeeBO feeBO) {
        Object goldDrawFeeString = redisUtil.get(RedisConfig.GOLD_DRAW_FEE);
        if(StringUtils.isEmpty(goldDrawFeeString)){
            List<GoldFee> all = backGoldFeeSlave.findAll();
            Map<String,Object> map = new HashMap<>();
            for (GoldFee goldFee : all){
                String name = "G"+goldFee.getName();
                Double amount = goldFee.getAmount();
                map.put(name,amount);
            }
            redisUtil.set(RedisConfig.GOLD_DRAW_FEE,JSON.toJSONString(map));
            return map;
        }else{
            return JSON.parseObject(goldDrawFeeString.toString(),Map.class);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setGoldDrawFee(FeeBO feeBO) {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        int type = feeBO.getType();
        Double fee = feeBO.getFee();
        GoldFee one = backGoldFeeSlave.findOne((long) type);
        if(one==null){
            GoldFee goldFee = new GoldFee();
            goldFee.setId((long)type);
            goldFee.setAmount(fee);
            String name = (type == 1 ? "100g" : "1000g");
            goldFee.setName(name);
            Double gold = (type == 1 ? 100d : 1000d);
            goldFee.setGold(gold);
            Query nativeQuery = entityManager.createNativeQuery("INSERT INTO gold_fee(id,name,gold,amount,ctime,utime) VALUES (?,?,?,?,?,?)");
            nativeQuery.setParameter(1,type);
            nativeQuery.setParameter(2,name);
            nativeQuery.setParameter(3,gold);
            nativeQuery.setParameter(4,fee);
            nativeQuery.setParameter(5,currentTime);
            nativeQuery.setParameter(6,currentTime);
            nativeQuery.executeUpdate();
//            backGoldFeeMaster.save(goldFee);
            return;
        }else{
            Query nativeQuery = entityManager.createNativeQuery("UPDATE gold_fee SET amount = ? WHERE id = ?");
            nativeQuery.setParameter(1,fee);
            nativeQuery.setParameter(2,type);

            int success = nativeQuery.executeUpdate();
            if(success == 0){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_GOLDFEE_FAIL));
            }
        }
        //删缓存中的数据
        redisUtil.delete(RedisConfig.GOLD_DRAW_FEE);
    }
}
