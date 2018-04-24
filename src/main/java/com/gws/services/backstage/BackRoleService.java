package com.gws.services.backstage;

import com.gws.dto.backstage.BackAuthesVO;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackUserBO;

import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
public interface BackRoleService {
    /**
     * 创建新角色
     * @param backUserBO
     */
    void createRole(BackUserBO backUserBO);

    /**
     * 根据条件查询角色信息
     * @param backUserBO
     * @return
     */
    PageDTO queryRoles(BackUserBO backUserBO);

    /**
     * 根据roleId修改角色信息
     * @param backUserBO
     */
    void updateRole(BackUserBO backUserBO);

    /**
     * 批量删除角色信息
     * @param backUserBO
     */
    void deleteRoles(BackUserBO backUserBO);

    /**
     * 显示某个角色下的权限信息
     * @param backUserBO
     * @return
     */
    List<BackAuthesVO> showAuthoritiesUnderRole(BackUserBO backUserBO);

    /**
     * 为角色分配权限信息
     * @param backUserBO
     */
    void assignAuthorities4Role(BackUserBO backUserBO);
}
