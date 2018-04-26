package com.gws.configuration.backstage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/25.
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService createThreadPool(){
        MyRejectExecutionHandler handler = new MyRejectExecutionHandler();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ExecutorService es = new ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>(10),threadFactory,handler){

            private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

            @Override
            protected void beforeExecute(Thread t, Runnable r) {//线程执行前的操作
                threadLocal.set(System.currentTimeMillis());
                System.out.println(t.getId()+"我(线程)准备执行哦。。。");
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {//线程执行后的操作
                Long startTime = threadLocal.get();
                threadLocal.remove();
                Long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getId()+"我(线程)执行好了。。。,耗时:"+(endTime-startTime)+"ms");
            }

            @Override
            protected void terminated() {
                System.out.println("线程池销毁了。。。");
            }

        };
        return es;
    }

    public static class MyRejectExecutionHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("哥们我累了，你过会儿再来执行吧。。。");
        }
    }

}
