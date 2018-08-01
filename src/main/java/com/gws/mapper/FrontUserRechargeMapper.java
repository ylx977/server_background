package com.gws.mapper;

import com.gws.entity.backstage.FrontUserBO;
import com.gws.entity.backstage.FrontUserRecharge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/7/27.
 */
@Mapper
public interface FrontUserRechargeMapper {


    List<FrontUserRecharge> queryFrontUserRecharge(FrontUserBO frontUserBO);

    long queryFrontUserRechargeCount(FrontUserBO frontUserBO);
}
