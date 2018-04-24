package com.gws.common.constants.backstage;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
public class BannerDisplayOrder {

    private BannerDisplayOrder(){
        throw new AssertionError("instantiation is not permitted");
    }

    /**
     * 播放顺序从左到右
     */
    public static final Integer LEFT_TO_RIGHT= 0;

    /**
     * 播放顺序从右到左
     */
    public static final Integer RIGHT_TO_LEFT= 1;

}
