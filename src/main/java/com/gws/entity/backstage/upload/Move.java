package com.gws.entity.backstage.upload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/25.
 */
@Data
public class Move {

    /**
     * 该banner信息在数据库对应的id号
     */
    private Long id;

    /**
     * 该banner图在数据库对应的顺序
     */
    private Integer orders;

    /**
     * 如果是新文件，对应新文件的顺序
     */
    private Integer fileOrder;

    public static void main(String[] args) {
        List<Move> list = new ArrayList<>();
        Move m1 = new Move();
        System.out.println(m1.getId());
        m1.setOrders(1);
        m1.setFileOrder(1);
        Move m2 = new Move();
        m2.setOrders(2);
        m2.setFileOrder(2);
        Move m3 = new Move();
        m3.setOrders(3);
        m3.setFileOrder(3);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        String s = JSON.toJSONString(list);
        System.out.println(s);
        List<Long> longList = new ArrayList<>();
        longList.add(1L);
        longList.add(2L);
        longList.add(3L);
        longList.add(4L);
        System.out.println(JSON.toJSON(longList));
        Integer integer = null;
        System.out.println(integer);
    }

}
