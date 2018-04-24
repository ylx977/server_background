package com.gws.services.backstage;

import org.springframework.web.multipart.MultipartFile;

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
    void updateBanner(MultipartFile[] files, String moveJson, String deleteJson);
}
