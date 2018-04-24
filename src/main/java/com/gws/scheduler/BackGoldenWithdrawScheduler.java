package com.gws.scheduler;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
@Component
@EnableScheduling
@Configurable
public class BackGoldenWithdrawScheduler {

    @Scheduled(cron = "0 */1 * * * * ")
    public void test(){
        System.out.println("我是springboot的定时器");
    }

}
