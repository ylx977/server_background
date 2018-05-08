package com.gws.services.backstage.impl;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.Feedback;
import com.gws.entity.backstage.FeedbackBO;
import com.gws.entity.backstage.FrontUserApplyInfo;
import com.gws.repositories.query.backstage.FeedBackQuery;
import com.gws.repositories.slave.backstage.BackFeedBackSlave;
import com.gws.services.backstage.BackFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@Service
public class FeedbackServiceImpl implements BackFeedbackService {

    private final BackFeedBackSlave backFeedBackSlave;

    @Autowired
    public FeedbackServiceImpl(BackFeedBackSlave backFeedBackSlave) {
        this.backFeedBackSlave = backFeedBackSlave;
    }

    @Override
    public PageDTO queryFeedbacks(FeedbackBO feedbackBO) {

        Integer page = feedbackBO.getPage();
        Integer rowNum = feedbackBO.getRowNum();

        FeedBackQuery feedBackQuery = new FeedBackQuery();
        if(feedbackBO.getProblemType() != null){
            feedBackQuery.setProblemType(feedbackBO.getProblemType());
        }
        feedBackQuery.setStartTime(feedbackBO.getStartTime());
        feedBackQuery.setEndTime(feedbackBO.getEndTime());

        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page-1,rowNum,sort);
        Page<Feedback> feedbackPage= backFeedBackSlave.findAll(feedBackQuery,pageable);

        List<Feedback> list = feedbackPage == null ? Collections.emptyList() : feedbackPage.getContent();
        long totalPage = feedbackPage == null ? 0 : feedbackPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }


}
