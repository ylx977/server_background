package com.gws.utils.http;

import com.alibaba.fastjson.JSON;
import com.gws.configuration.backstage.ThreadPoolConfig;
import lombok.Data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/10.
 */
public class HttpRequest {

    /**
     * @author Jack
     */
    public static final String sendPost(String httpUrl,String json){
        DataOutputStream dos = null;
        BufferedReader in = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDefaultUseCaches(false);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.write(json.getBytes("utf-8"));
            dos.flush();
            dos.close();
            //开启输入流
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb=new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("message:"+e.getMessage()+",cause:"+e.getCause());
        }finally {
            if(dos!=null){
                try {
                    dos.close();
                } catch (IOException e) {
                    dos = null;
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
        }
    }

    /**
     * @author Jack
     */
    public static final String sendPostSM(String httpUrl,String params,String time,String ip,String sign,String appKey){
        DataOutputStream dos = null;
        BufferedReader in = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("FZM-Ca-Timestamp", time);
            conn.setRequestProperty("FZM-Ca-AppKey", appKey);
            conn.setRequestProperty("FZM-Ca-AppIp", ip);
            conn.setRequestProperty("FZM-Ca-Signature", sign);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDefaultUseCaches(false);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.write(params.getBytes("utf-8"));
            dos.flush();
            dos.close();
            //开启输入流
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb=new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("message:"+e.getMessage()+",cause:"+e.getCause());
        }finally {
            if(dos!=null){
                try {
                    dos.close();
                } catch (IOException e) {
                    dos = null;
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
        }
    }

    public static final String sendGet(String httpUrl,String... parameters){
        BufferedReader in = null;
        try {
            for(String parameter : parameters){
                httpUrl = httpUrl + "/" + parameter;
            }
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDefaultUseCaches(false);
            //开启输入流
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb=new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("message:"+e.getMessage()+",cause:"+e.getCause());
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
        }
    }


    /**
     * 专门为李邦柱提供的请求
     * @param httpUrl
     * @param json
     * @return
     */
    public static final String sendPost4LBZ(String httpUrl,String json){
        DataOutputStream dos = null;
        BufferedReader in = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Cache-Control", "no-cache");
            //这里的请求头是authorization，小写的开头字母，然后Basic后面的是base64编码的，原内容是[33exchange:^exchange@2018@btytoken@33^`]，你没看错，最后多了一个小顿号`
            conn.setRequestProperty("authorization", "Basic MzNleGNoYW5nZTpeZXhjaGFuZ2VAMjAxOEBidHl0b2tlbkAzM15g");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDefaultUseCaches(false);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.write(json.getBytes("utf-8"));
            dos.flush();
            dos.close();
            //开启输入流
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb=new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("message:"+e.getMessage()+",cause:"+e.getCause());
        }finally {
            if(dos!=null){
                try {
                    dos.close();
                } catch (IOException e) {
                    dos = null;
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
        }
    }

    private static final long[] uids = {1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,
            31,32,33,34,35,36,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,
            51,52,53,54,55,56,57,58,59,60,
            61,62,63,64,65,66,67,68,69,70,
            71,72,73,74,75,76,77,78,79,80,
            81,82,83,84,85,86,87,88,89,90,
            91,92,93,94,95,96,97,98,99,100};

    public static void main(String[] args) throws Exception{
//        MyRejectExecutionHandler handler = new MyRejectExecutionHandler();
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//        ExecutorService es = new ThreadPoolExecutor(200,500,60,
//                TimeUnit.SECONDS,new LinkedBlockingQueue<>(10),threadFactory,handler){
//            @Override
//            protected void terminated() {
//                System.out.println("线程池销毁了。。。");
//            }
//
//        };

        for (int i = 0;i<200;i++){
            new Thread(HttpRequest::send).start();
        }


    }

    public static void send(){
        for(int i = 0; i<100;i++){
//            long uid = uids[(int)(Math.random()*100)];
//            User user = new User();
//            user.setUid(uid);
//            es.execute(()->{
//                String s = sendPost("http://127.0.0.1:8101/localmq/api/miaosha3", JSON.toJSONString(user));
//                System.out.println(s);
//            });
            try {
                Thread.sleep(1);
            }catch (Exception e){
                System.out.println("hahah");
            }
            new Thread(()->{
                long uid = uids[(int)(Math.random()*100)];
                User user = new User();
                user.setUid(uid);
                String s = sendPost("http://127.0.0.1:8101/localmq/api/miaosha4", JSON.toJSONString(user));
                System.out.println(s);
            }).start();

        }
    }
    @Data
    public static class User{
        private Long uid;
        private String name;
        private String password;
        private Integer amount;
    }

    public static class MyRejectExecutionHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("我会继续执行的");
            executor.execute(r);
        }
    }

}
