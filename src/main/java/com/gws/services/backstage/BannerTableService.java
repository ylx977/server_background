package com.gws.services.backstage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
public interface BannerTableService {

    /**
     * 更新整个banner表的信息(包括新增的文件，排序后的参数，以及要删除的信息)
     * @param files
     * @param moveJson
     * @param deleteJson
     */
    void updateBanner(List<String> bannerUrls, String moveJson, String deleteJson);

    /**
     * 校验updateBanner方法中的所有参数是否正确
     * @param files
     * @param moveJson
     * @param deleteJson
     */
    void checkParameter(MultipartFile[] files, String moveJson, String deleteJson);
}
