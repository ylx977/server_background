package com.gws.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(params.getBytes("utf-8"));
            dos.flush();
            dos.close();
            //开启输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb=new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("message:"+e.getMessage()+",cause:"+e.getCause());
        }
    }

}