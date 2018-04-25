package com.gws.services.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author:wangdong
 * @description:文件上传的接口
 */
public interface AliossService {

    /**
     * 上传单个文件
     * @param file 文件
     * @param bucket 存储空间
     * @return
     */
    String uploadFile(MultipartFile file, String bucket);

    /**
     * 上传一个字符串
     * @param file
     * @param bucket
     * @return
     */
    String uploadStringFile(String file, String bucket);
    /**
     * 流式下载
     * @param bucket 存储空间
     * @param key 存储的key
     * @return
     */
    InputStream downByStream(String bucket, String key) throws IOException;

    /**
     * 批量上传
     * 上传多个文件
     * @param files
     * @param bucket
     * @return
     */
    List<String> uploadFiles(MultipartFile[] files, String bucket);
}
