package com.gws.common.constants.backstage;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
public class FrontUserStatus {

    private FrontUserStatus() {
        throw new AssertionError("instantiation is not permitted");
    }

    /**
     * 前台用户0表示账户冻结（删除）状态
     */
    public static final Integer FREEZE = 1;

    /**
     * 前台用户1表示账户可用状态
     */
    public static final Integer NORMAL = 0;

}
