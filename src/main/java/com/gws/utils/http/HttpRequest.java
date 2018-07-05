package com.gws.utils.http;

import com.alibaba.fastjson.JSON;
import com.gws.entity.backstage.price.Price;
import com.gws.entity.backstage.price.PriceResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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


    public static void main(String[] args) {
//        Price price = new Price();
//        price.setCnysgdbuy(4.7);
//        price.setCnysgdsell(4.8);
//        price.setSgdusdbuy(1.34);
//        price.setSgdusdsell(1.35);
//        price.setSpread(0.2);
//        String s = sendPost("http://server.codingfine.com:9090/profile/setting", JSON.toJSONString(price));
//        System.out.println(s);
//        String s = sendGet("http://market.codingfine.com:9090/data/price");
//        System.out.println(s);
//        PriceResult priceResult = JSON.parseObject(s, PriceResult.class);
//        List<PriceResult.Trade> data = priceResult.getData();
//        System.out.println(data);
//        for(int i = 0;i < 200;i++){
//            final int x = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    sendPost("http://localhost:8100/mq/api/add","{\"age\":40,\"name\":\"kelly"+x+"\"}");
//                }
//            }).start();
//        }


    }

}
