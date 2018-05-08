package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.NoticeBO;
import com.gws.entity.backstage.NoticeContent;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
public interface BackNoticeService {

    /**
     * 根据条件查询公告栏的信息
     * @param noticeBO
     * @return
     */
    PageDTO queryNotices(NoticeBO noticeBO);

    /**
     * 根据公告编号id查询公告的详细内容
     * @param noticeBO
     * @return
     */
    NoticeContent queryNoticeContent(NoticeBO noticeBO);

    /**
     * 根据公告内容id修改公告内容
     * @param noticeBO
     */
    void updateNotice(NoticeBO noticeBO);

    /**
     * 根据公告id删除公告(从公告列表删除该条公告信息，公告内容暂时不删除)
     * @param noticeBO
     */
    void deleteNotice(NoticeBO noticeBO);

    /**
     * 发布公告
     * @param noticeBO
     */
    void publishNotice(NoticeBO noticeBO);

    /**
     * 修改公告的状态(包括是否显示和是否置顶的状态)
     * @param noticeBO
     */
    void updateNoticeStatus(NoticeBO noticeBO);
}
