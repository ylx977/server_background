package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.BannerDisplayOrder;
import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.BannerBO;
import com.gws.entity.backstage.BannerVO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BannerConfigService;
import com.gws.services.backstage.BannerTableService;
import com.gws.services.backstage.impl.UpdateBannerThread;
import com.gws.services.oss.AliossService;
import com.gws.utils.file.FileTransferUtil;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@RestController
@RequestMapping("/back/api/backstage/bannerConfig")
@PropertySource(value = {"classpath:application.properties"},encoding = "utf-8")
public class BackBannerConfigController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackBannerConfigController.class);

    private final HttpServletRequest request;

    private final BannerConfigService bannerConfigService;

    private final BannerTableService bannerTableService;

    private final ExecutorService executorService;

    private final AliossService aliossService;

    @Value("${ali.oss.bucket}")
    public String bucket;

    @Autowired
    public BackBannerConfigController(HttpServletRequest request, BannerConfigService bannerConfigService, BannerTableService bannerTableService, ExecutorService executorService, AliossService aliossService) {
        this.request = request;
        this.bannerConfigService = bannerConfigService;
        this.bannerTableService = bannerTableService;
        this.executorService = executorService;
        this.aliossService = aliossService;
    }

    /**
     * 查询后台轮播图的所有信息
     * @return
     */
    @RequestMapping(value = "/queryBanners",method = RequestMethod.POST)
    public JsonResult queryBanners(@RequestBody BannerBO bannerBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改后台banner基本配置信息",uid);
        try {
            BannerVO bannerVO = bannerConfigService.queryBanners();
            return success(bannerVO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }


    /**
     * banner图的基本参数配置(播放顺序和播放间隔)
     * {
     *      "displayOrder"
     *      "displayInterval"
     * }
     * @param bannerBO
     * @return
     */
    @RequestMapping(value = "/basicBannerConfig",method = RequestMethod.POST)
    public JsonResult basicBannerConfig(@RequestBody BannerBO bannerBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改后台banner基本配置信息",uid);
        try {
            Integer displayOrder = bannerBO.getDisplayOrder();
            Integer displayInterval = bannerBO.getDisplayInterval();
            if(null != displayOrder){
                ValidationUtil.checkRangeAndAssignInt(displayOrder, BannerDisplayOrder.LEFT_TO_RIGHT,BannerDisplayOrder.RIGHT_TO_LEFT);
            }
            if(null != displayInterval){
                //不能小于1
                ValidationUtil.checkMinAndAssignInt(displayInterval,1);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            bannerConfigService.basicBannerConfig(bannerBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * banner图的更新操作及顺序和删除操作
     * {
     *      "pics":[多个文件数组]
     *      "moveJson":[文件移动的json数据(包括新增的文件)]
     *      "deleteJson":[文件删除的json数据,long类型数组]
     * }
     * @return
     */
    @RequestMapping(value = "/updateBanner",method = RequestMethod.POST)
    public JsonResult updateBanner(@RequestParam(value = "pics",required = false) MultipartFile[] files,
                                    @RequestParam(value = "moveJson",required = false) String moveJson,
                                    @RequestParam(value = "deleteJson",required = false) String deleteJson){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},对后台banner进行全局设置",uid);
        try {
            if(files != null && files.length > 0){
                FileTransferUtil.checkIfHasEmptyFile(files);
                FileTransferUtil.checkMultipleUploadFilesSuffixes(files,"bmp","jpg","jpeg","png","tiff","gif");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            bannerTableService.checkParameter(files,moveJson,deleteJson);
            List<String> bannerUrls = new ArrayList<>();
            if(files!= null && files.length > 0){
                bannerUrls = aliossService.uploadFiles(files, bucket);
            }
            executorService.execute(new UpdateBannerThread(bannerUrls,moveJson,deleteJson,bannerTableService));
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }


}
