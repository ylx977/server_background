package com.gws.controllers.backstage;

import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.BannerBO;
import com.gws.entity.backstage.FrontUserBO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@RestController
@RequestMapping("/api/backstage/bannerConfig")
public class BackBannerConfigController {

    /**
     * banner图的基本参数配置(播放顺序和播放间隔)
     * @param bannerBO
     * @return
     */
    @RequestMapping("/basicConfig")
    public JsonResult approveApply(@RequestBody BannerBO bannerBO){
        return null;
    }

}
