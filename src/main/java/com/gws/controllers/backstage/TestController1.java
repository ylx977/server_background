package com.gws.controllers.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.entity.backstage.BackAuthes;
import com.gws.entity.backstage.BackAuthgroups;
import com.gws.repositories.query.backstage.BackAuthgroupsQuery;
import com.gws.repositories.slave.backstage.BackAuthgroupsSlave;
import com.gws.repositories.slave.backstage.BackUserTokenSlave;
import org.hibernate.internal.QueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@RestController
@RequestMapping("/api")
public class TestController1 {

    @Autowired
    private BackAuthgroupsSlave backAuthgroupsSlave;

    @PersistenceContext
    private EntityManager em;

    @RequestMapping("/test")
    public List<BackAuthgroups> test(@RequestBody BackAuthes backAuthes){
        BackAuthgroupsQuery backAuthgroupsQuery = new BackAuthgroupsQuery();
        backAuthgroupsQuery.setAuthgroupIds(Arrays.asList(new Long[]{1L,2L}));
        List<BackAuthgroups> all = backAuthgroupsSlave.findAll(backAuthgroupsQuery);
        System.out.println(backAuthes);
        return all;
    }

    @RequestMapping("/go")
    public String test2(@RequestBody BackAuthes backAuthes){
        Query query = em.createNativeQuery("select uid from back_users_authgroups where authgroup_id not in (1,2)");
        List<Long> resultList = (List<Long>) query.getResultList();
        System.out.println(resultList);
        return "ok";
    }

}
