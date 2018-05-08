package com.gws.services.backstage.impl;

import com.gws.dto.backstage.PageDTO;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.Notice;
import com.gws.entity.backstage.NoticeBO;
import com.gws.entity.backstage.NoticeContent;
import com.gws.repositories.master.backstage.BackNoticeContentMaster;
import com.gws.repositories.master.backstage.BackNoticeMaster;
import com.gws.repositories.query.backstage.NoticeQuery;
import com.gws.repositories.slave.backstage.BackNoticeContentSlave;
import com.gws.repositories.slave.backstage.BackNoticeSlave;
import com.gws.services.backstage.BackNoticeService;
import com.gws.utils.cache.IdGlobalGenerator;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@Service
public class BackNoticeServiceImpl implements BackNoticeService {

    private final BackNoticeSlave backNoticeSlave;

    private final BackNoticeMaster backNoticeMaster;

    private final BackNoticeContentSlave backNoticeContentSlave;

    private final BackNoticeContentMaster backNoticeContentMaster;

    private final IdGlobalGenerator idGlobalGenerator;

    @Autowired
    public BackNoticeServiceImpl(BackNoticeSlave backNoticeSlave, BackNoticeMaster backNoticeMaster, BackNoticeContentSlave backNoticeContentSlave, BackNoticeContentMaster backNoticeContentMaster, IdGlobalGenerator idGlobalGenerator) {
        this.backNoticeSlave = backNoticeSlave;
        this.backNoticeMaster = backNoticeMaster;
        this.backNoticeContentSlave = backNoticeContentSlave;
        this.backNoticeContentMaster = backNoticeContentMaster;
        this.idGlobalGenerator = idGlobalGenerator;
    }

    @Override
    public PageDTO queryNotices(NoticeBO noticeBO) {
        Integer page = noticeBO.getPage();
        Integer rowNum = noticeBO.getRowNum();

        NoticeQuery noticeQuery = new NoticeQuery();
        if(!StringUtils.isEmpty(noticeBO.getAuthor())){
            noticeQuery.setAuthor(noticeBO.getAuthor());
        }
        if(!StringUtils.isEmpty(noticeBO.getTitle())){
            noticeQuery.setTitle(noticeBO.getTitle());
        }
        if(noticeBO.getId() != null){
            noticeQuery.setId(noticeBO.getId());
        }
        //删除的公告不去用显示
        noticeQuery.setDeleted(1);

        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page-1,rowNum,sort);

        Page<Notice> noticePage = backNoticeSlave.findAll(noticeQuery,pageable);

        List<Notice> list = noticePage == null ? Collections.emptyList() : noticePage.getContent();
        long totalPage = noticePage == null ? 0 : noticePage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    public NoticeContent queryNoticeContent(NoticeBO noticeBO) {
        Long contentId = noticeBO.getContentId();
        NoticeContent one = backNoticeContentSlave.findOne(contentId);
        return one;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(NoticeBO noticeBO) {
        Long contentId = noticeBO.getContentId();
        String title = noticeBO.getTitle();
        String content = noticeBO.getNoticeContent();

        NoticeContent noticeContent = new NoticeContent();
        noticeContent.setContent(content);

        int success = backNoticeContentMaster.updateById(noticeContent, contentId,"content");
        if(success == 0){
            throw new RuntimeException("更新公告内容失败");
        }

        Notice notice = new Notice();
        notice.setTitle(title);
        int success2 = backNoticeMaster.updateById(notice, contentId,"title");
        if(success2 == 0){
            throw new RuntimeException("更新公告列表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(NoticeBO noticeBO) {
        Long id = noticeBO.getId();

        Notice notice = new Notice();
        notice.setDeleted(0);
        int success = backNoticeMaster.updateById(notice, id, "deleted");
        if(success == 0){
            throw new RuntimeException("公告删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(NoticeBO noticeBO) {
        Long id = idGlobalGenerator.getSeqId(NoticeContent.class);
        String content = noticeBO.getNoticeContent();
        String title = noticeBO.getTitle();
        UserDetailDTO userDetailDTO = noticeBO.getUserDetailDTO();
        Long uid = userDetailDTO.getUid();
        String author = userDetailDTO.getPersonName();

        NoticeContent noticeContent = new NoticeContent();
        noticeContent.setId(id);
        noticeContent.setContent(content);
        backNoticeContentMaster.save(noticeContent);

        Notice notice = new Notice();
        notice.setId(id);
        notice.setTitle(title);
        notice.setDeleted(1);
        notice.setAuthor(author);
        notice.setShow(1);
        notice.setTop(0);
        notice.setUid(uid);
        backNoticeMaster.save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNoticeStatus(NoticeBO noticeBO) {
        Integer currentTime = (int)(System.currentTimeMillis()/1000);
        Long id = noticeBO.getId();
        Integer type = noticeBO.getType();
        Integer status = noticeBO.getStatus();

        if(type == 1){
            //表示修改是否置顶
            Notice notice = new Notice();
            notice.setTop(status);
            notice.setUtime(currentTime);
            int success = backNoticeMaster.updateById(notice, id, "top","utime");
            if(success==0){
                throw new RuntimeException("更新置顶状态失败");
            }
        }

        if(type == 2){
            //表示修改是否显示
            Notice notice = new Notice();
            notice.setShow(status);
            notice.setUtime(currentTime);
            int success = backNoticeMaster.updateById(notice,id,"show","utime");
            if(success==0){
                throw new RuntimeException("更新显示状态失败");
            }
        }
    }
}
