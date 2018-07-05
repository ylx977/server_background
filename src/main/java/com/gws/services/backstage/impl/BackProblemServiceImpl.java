package com.gws.services.backstage.impl;

import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.configuration.backstage.UserInfoConfig;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.*;
import com.gws.repositories.master.backstage.BackProblemContentMaster;
import com.gws.repositories.master.backstage.BackProblemMaster;
import com.gws.repositories.query.backstage.ProblemQuery;
import com.gws.repositories.slave.backstage.BackProblemContentSlave;
import com.gws.repositories.slave.backstage.BackProblemSlave;
import com.gws.services.backstage.BackProblemService;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.redis.RedisUtil;
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
 * Created by fuzamei on 2018/6/21.
 */
@Service
public class BackProblemServiceImpl implements BackProblemService {

    private final BackProblemMaster backProblemMaster;

    private final BackProblemSlave backProblemSlave;

    private final BackProblemContentMaster backProblemContentMaster;

    private final BackProblemContentSlave backProblemContentSlave;

    private final IdGlobalGenerator idGlobalGenerator;

    private final RedisUtil redisUtil;

    @Autowired
    public BackProblemServiceImpl(BackProblemMaster backProblemMaster, BackProblemSlave backProblemSlave, BackProblemContentMaster backProblemContentMaster, BackProblemContentSlave backProblemContentSlave, IdGlobalGenerator idGlobalGenerator, RedisUtil redisUtil) {
        this.backProblemMaster = backProblemMaster;
        this.backProblemSlave = backProblemSlave;
        this.backProblemContentMaster = backProblemContentMaster;
        this.backProblemContentSlave = backProblemContentSlave;
        this.idGlobalGenerator = idGlobalGenerator;
        this.redisUtil = redisUtil;
    }

    @Override
    public PageDTO queryProblem(ProblemBO problemBO) {
        Integer page = problemBO.getPage();
        Integer rowNum = problemBO.getRowNum();

        ProblemQuery problemQuery = new ProblemQuery();
        if(!StringUtils.isEmpty(problemBO.getAuthor())){
            problemQuery.setAuthor(problemBO.getAuthor());
        }
        if(!StringUtils.isEmpty(problemBO.getTitle())){
            problemQuery.setTitle(problemBO.getTitle());
        }
        if(problemBO.getId() != null){
            problemQuery.setId(problemBO.getId());
        }
        //删除的公告不去用显示
        problemQuery.setDeleted(1);

        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page-1,rowNum,sort);

        Page<Problem> problemPage = backProblemSlave.findAll(problemQuery,pageable);

        List<Problem> list = problemPage == null ? Collections.emptyList() : problemPage.getContent();
        long totalPage = problemPage == null ? 0 : problemPage.getTotalElements();

        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    public ProblemContent queryProblemContent(ProblemBO problemBO) {
        Long contentId = problemBO.getContentId();

        //先去缓存拿数据
        Object content = redisUtil.get(RedisConfig.PROBLEM_CONTENT + contentId);
        if(content!=null){
            ProblemContent problemContent = new ProblemContent();
            problemContent.setId(contentId);
            problemContent.setContent(content.toString());
            return problemContent;
        }else{
            ProblemContent one = backProblemContentSlave.findOne(contentId);

            //将缓存存入
            redisUtil.set(RedisConfig.PROBLEM_CONTENT + contentId,one.getContent());

            return one;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProblem(ProblemBO problemBO) {
        Long contentId = problemBO.getContentId();
        String title = problemBO.getTitle();
        String content = problemBO.getProblemContent();

        ProblemContent problemContent = new ProblemContent();
        problemContent.setContent(content);

        int success = backProblemContentMaster.updateById(problemContent, contentId, "content");
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_PROBLEM_FAIL));
        }

        Problem problem = new Problem();
        problem.setTitle(title);
        int success2 = backProblemMaster.updateById(problem, contentId,"title");
        if(success2 == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_PROBLEM_LIST_FAIL));
        }

        //删除缓存
        redisUtil.delete(RedisConfig.PROBLEM_CONTENT + contentId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProblem(ProblemBO problemBO) {
        Long id = problemBO.getId();

        Problem problem = new Problem();
        problem.setDeleted(0);
        int success = backProblemMaster.updateById(problem, id, "deleted");
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.DELETE_PROBLEM_FAIL));
        }

        //删除缓存
        redisUtil.delete(RedisConfig.PROBLEM_CONTENT + id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(ProblemBO problemBO) {
        Long id = idGlobalGenerator.getSeqId(NoticeContent.class);
        String content = problemBO.getProblemContent();
        String title = problemBO.getTitle();

        ProblemContent problemContent = new ProblemContent();
        problemContent.setId(id);
        problemContent.setContent(content);
        backProblemContentMaster.save(problemContent);

        Problem problem = new Problem();
        problem.setId(id);
        problem.setTitle(title);
        problem.setDeleted(1);
        problem.setAuthor(UserInfoConfig.getUserInfo().getPersonName());//从threadlocal拿
        problem.setUid(UserInfoConfig.getUserInfo().getUid());//从threadlocal拿
        backProblemMaster.save(problem);
    }
}
