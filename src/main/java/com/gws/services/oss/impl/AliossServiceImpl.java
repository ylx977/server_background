package com.gws.services.oss.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.gws.GwsWebApplication;
import com.gws.services.oss.AliossService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author:wangdong
 * @description:阿里云上传文件的实现类
 */
@Configuration
@Service
public class AliossServiceImpl implements AliossService {

    static Logger logger = Logger.getLogger(GwsWebApplication.class);

    @Value("${ali.oss.endpoint}")
    private String aliOssEndpoint;

    @Value("${ali.accessKey}")
    private String aliAccessKey;

    @Value("${ali.secretKey}")
    private String aliSecretKey;

    @Value("${ali.oss.cdnHttpsHost}")
    private String cdnHttpsHost;

    private String http = "http://";

    @PostConstruct
    public void init() {
        logger.info("ossClient Started");
    }

    /**
     * 上传单个文件
     *
     * @param file   文件
     * @param bucket 存储空间
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String bucket) {

        if (file.isEmpty()) {
            return null;
        }

        StringBuilder key = new StringBuilder();

        String fileName = file.getOriginalFilename();
        String postfix = getPostfix(fileName);

        if (!StringUtils.isEmpty(postfix)) {
            key.append(postfix).append("/");
        }
        key.append(UUID.randomUUID().toString());
        if (!StringUtils.isEmpty(postfix)) {
            key.append(".").append(postfix);
        }
        String fixKey = String.valueOf(key);
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(aliOssEndpoint, aliAccessKey, aliSecretKey);

        try {
            ossClient.putObject(bucket, fixKey, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭client
        ossClient.shutdown();

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer downCdnHttpsHost = stringBuffer.append(http).append(bucket).append(cdnHttpsHost);

        return new StringBuffer().append(downCdnHttpsHost).append("/").append(key).toString();
    }

    /**
     * 上传一个字符串
     *
     * @param file
     * @param bucket
     * @return
     */
    @Override
    public String uploadStringFile(String file, String bucket) {
        if (StringUtils.isEmpty(file) || StringUtils.isEmpty(bucket)) {
            return null;
        }

        StringBuilder key = new StringBuilder();

        String fileName = file;
        String postfix = getPostfix(fileName);

        if (!StringUtils.isEmpty(postfix)) {
            key.append(postfix).append("/");
        }
        key.append(UUID.randomUUID().toString());
        if (!StringUtils.isEmpty(postfix)) {
            key.append(".").append(postfix);
        }
        String fixKey = String.valueOf(key);
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(aliOssEndpoint, aliAccessKey, aliSecretKey);

        ossClient.putObject(bucket, fixKey, new ByteArrayInputStream(file.getBytes()));
        // 关闭client
        ossClient.shutdown();

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer downCdnHttpsHost = stringBuffer.append(http).append(bucket).append(cdnHttpsHost);

        return new StringBuffer().append(downCdnHttpsHost).append("/").append(key).toString();
    }

    /**
     * 流式下载
     *
     * @param key
     * @return
     */
    @Override
    public InputStream downByStream(String bucket,String key) throws IOException {
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(aliOssEndpoint, aliAccessKey, aliSecretKey);
        OSSObject ossObject = ossClient.getObject(bucket, key);

        // 读Object内容
        System.out.println("Object content:");
        InputStream inputStream = ossObject.getObjectContent();
        //关闭流
        inputStream.close();
        // 关闭client
        ossClient.shutdown();
        return inputStream;
    }

    /**
     * 批量上传
     * 上传多个文件
     *
     * @param files
     * @param bucket
     * @return
     */
    @Override
    public List<String> uploadFiles(MultipartFile[] files, String bucket) {

        if (null == files || StringUtils.isEmpty(bucket)){
            return Collections.EMPTY_LIST;
        }

        List<String> result = new ArrayList<>();

        for (MultipartFile file : files){
            String download = uploadFile(file,bucket);
            result.add(download);
        }

        return CollectionUtils.isEmpty(result) ? Collections.EMPTY_LIST : result;
    }


    private String getPostfix (String file){
        if (null == file) {
            return "";
        }
        int postfixIdx = file.lastIndexOf(".");
        if (-1 == postfixIdx) {
            return "";
        } else {
            return file.substring(postfixIdx + 1);
        }
    }
}
