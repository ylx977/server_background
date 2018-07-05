package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.ProblemBO;
import com.gws.entity.backstage.ProblemContent;

/**
 * Created by fuzamei on 2018/6/21.
 */
public interface BackProblemService {

    /**
     * 查询后台的常见问题
     * @param problemBO
     * @return
     */
    PageDTO queryProblem(ProblemBO problemBO);

    /**
     * 查询常见问题内容
     * @param problemBO
     * @return
     */
    ProblemContent queryProblemContent(ProblemBO problemBO);

    /**
     * 修改常见问题内容
     * @param problemBO
     */
    void updateProblem(ProblemBO problemBO);

    /**
     * 删除常见问题
     * @param problemBO
     */
    void deleteProblem(ProblemBO problemBO);

    /**
     * 发布常见问题
     * @param problemBO
     */
    void publishNotice(ProblemBO problemBO);
}
