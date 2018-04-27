package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.BackAuthesEnum;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.common.constants.backstage.Roles;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.BackAuthesVO;
import com.gws.dto.backstage.BackAuthgroupsVO;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackAuthService;
import com.gws.services.backstage.BackRoleService;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/16.
 */
@RestController
@RequestMapping("/api/backstage/userManagement")
public class BackUserManagementController extends BaseApiController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackUserManagementController.class);

    private final BackRoleService backRoleService;

    private final BackUserService backUserService;

    private final BackAuthService backAuthService;

    private final HttpServletRequest request;

    @Autowired
    public BackUserManagementController(BackRoleService backRoleService, BackUserService backUserService, BackAuthService backAuthService, HttpServletRequest request) {
        this.backRoleService = backRoleService;
        this.backUserService = backUserService;
        this.backAuthService = backAuthService;
        this.request = request;
    }

    //===================================以下是账户模块=========================================================
    /**
     * 创建后台用户账户
     * {
     *     "username"
     *     "password"
     *     "contact"
     *     "personName"
     * }
     * @return
     */
    @RequestMapping("/users/createUser")
    public JsonResult createUser(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入username,password,contact等参数
         * 2：基本参数校验
         * 3：查询用户名是否重名，联系方式是否重名（是否要和已经删除的用户重名？）
         * 4：插入数据库
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},创建后台用户",uid);
        try {
            ValidationUtil.checkBlankAndAssignString(backUserBO.getUsername(), RegexConstant.USERNAME_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPassword(),RegexConstant.PWD_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getContact());
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPersonName(),RegexConstant.NAME_REGEX);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backUserService.saveUser(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }


    /**
     * 查询后台用户账户信息接口
     * {
     *     "page"
     *     "rowNum"
     *     "username"
     *     "startTime"
     *     "endTime"
     * }
     * @return
     */
    @RequestMapping("/users/queryUserInfo")
    public JsonResult queryUserInfo(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入page,rowNum,时间。。。等查询参数
         * 2：基本参数校验
         * 3：查询出指定参数的用户信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询后台用户",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(backUserBO.getPage(),1);
            ValidationUtil.checkMinAndAssignInt(backUserBO.getRowNum(),1);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getStartTime(),0);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getEndTime(),Integer.MAX_VALUE);
            if(startTime > endTime){
                backUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                backUserBO.setEndTime(endTime);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backUserBO.setOperatorUid(uid);
            PageDTO backUserPageDTO = backUserService.queryUserInfo(backUserBO);
            return success(backUserPageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 删除后台用户账户信息接口
     * {
     *     "uids"
     * }
     * @return
     */
    @RequestMapping("/users/deleteUsers")
    public JsonResult deleteUsers(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入一大堆uid 参数（如果支持批量删除）
         * 2：基本参数校验
         * 3：修改后台uid用户的is_delete状态
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},删除后台用户",uid);
        try {
            List<Long> uids = backUserBO.getUids();
            if(uids.contains(uid)){
                throw new RuntimeException("不能删除自己");
            }
            if(uids.contains(1L)){
                throw new RuntimeException("不能删除超级管理员");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backUserBO.setOperatorUid(uid);
            backUserService.deleteUsers(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 修改后台用户账户信息接口
     * {
     *     "uid"
     *     "username"
     *     "password"
     *     "contact"
     *     "personName"
     * }
     * @return
     */
    @RequestMapping("/users/updateUserInfo")
    public JsonResult updateUserInfo(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入一大堆uid，username，password，contact 等参数
         * 2：基本参数校验
         * 3：检查是否是已经被删除的用户
         * 4：修改后台uid用户的这些基本信息
         */
        long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},修改后台用户",uid);
        try {
            long beOperatedUid = ValidationUtil.checkAndAssignLong(backUserBO.getUid());
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPassword(),RegexConstant.PWD_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getUsername(), RegexConstant.USERNAME_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getContact());
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPersonName(),RegexConstant.NAME_REGEX);
            if(beOperatedUid == uid){
                throw new RuntimeException("不能修改自己的信息");
            }
            if(beOperatedUid == 1L){
                throw new RuntimeException("不能修改超级管理员的信息");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backUserBO.setOperatorUid(uid);
            backUserService.updateUsers(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 显示当前用户下的角色信息
     * {
     *     "uid"
     * }
     * @return
     */
    @RequestMapping("/users/showRoleInfoUnderUser")
    public JsonResult showRoleInfoUnderAccount(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入uid 参数
         * 2：基本参数校验
         * 3：检查是否是已经被删除的用户(否则不给看)
         * 4：显示当前用户拥有的角色信息(显示所有角色下用户拥有的角色信息)
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看后台用户的角色信息",uid);
        try {
            Long beOperatedUid = ValidationUtil.checkAndAssignLong(backUserBO.getUid());
            if(beOperatedUid.equals(uid)){
                throw new RuntimeException("无法查看自己的角色信息");
            }
            if(beOperatedUid.equals(1L)){
                throw new RuntimeException("无法查看超级管理员的角色信息");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            List<BackAuthgroupsVO> backAuthgroupsVOList = backUserService.showRoleInfoUnderAccount(backUserBO);
            return success(backAuthgroupsVOList);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 给当前用户分配角色信息
     * {
     *     "uid"
     *     "authgroupIds"
     * }
     * @return
     */
    @RequestMapping("/users/assignRoles4User")
    public JsonResult assignRoles4Account(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入uid，角色id【数组形式】 参数
         * 2：基本参数校验
         * 3：检查是否是已经被删除的用户(否则不给操作)
         * 4：将该账号id下的角色改为本次传入的roleIds【数组形式】
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},分配后台用户的角色信息",uid);
        try {
            Long beOperatedUid = ValidationUtil.checkAndAssignLong(backUserBO.getUid());
            List<Long> authgroupIds = backUserBO.getAuthgroupIds();
            if(authgroupIds.contains(1L)){
                throw new RuntimeException("无法分配超级管理员角色");
            }
            if(beOperatedUid.equals(uid)){
                throw new RuntimeException("无法修改自己的角色信息");
            }
            if(beOperatedUid.equals(1L)){
                throw new RuntimeException("无法修改超级管理员的角色信息");
            }
            if(!uid.equals(1L)){
                if(authgroupIds.contains(2L)){
                    throw new RuntimeException("无权分配管理员角色");
                }
            }
            //还有管理员之间不能相互修改
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backUserBO.setOperatorUid(uid);
            backUserService.assignRoles4Account(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }


    //===================================以下是角色模块=========================================================
    /**
     * 创建角色
     * {
     *     "roleName"
     * }
     * @return
     */
    @RequestMapping("/role/createRole")
    public JsonResult createRole(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入角色名
         * 2：基本参数校验
         * 3：查看是否重名，或者不能和
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},创建后台用户的角色信息",uid);
        try {
            String roleName = ValidationUtil.checkBlankAndAssignString(backUserBO.getRoleName());
            if(Roles.ADMIN.equals(roleName) || Roles.SUPER_ADMIN.equals(roleName)){
                throw new RuntimeException("不能创建超级管理员和管理员角色");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backRoleService.createRole(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 查询角色信息
     * {
     *     "page"
     *     "rowNum"
     *     "roleName"
     *     "startTime"
     *     "endTime"
     * }
     * @return
     */
    @RequestMapping("/role/queryRoles")
    public JsonResult queryRoles(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入page,rowNum,等查询参数
         * 2：基本参数校验
         * 3：查询出指定要查询的所有角色信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询后台的角色信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(backUserBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(backUserBO.getPage(),1);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getStartTime(),0);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getEndTime(),Integer.MAX_VALUE);
            if(startTime > endTime){
                backUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                backUserBO.setEndTime(endTime);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            PageDTO backRolePageDTO = backRoleService.queryRoles(backUserBO);
            return success(backRolePageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 修改角色信息
     * {
     *     "roleId"
     *     "roleName"
     * }
     * @return
     */
    @RequestMapping("/role/updateRole")
    public JsonResult updateRole(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入roleId,roleName,等修改参数
         * 2：基本参数校验
         * 3：判断角色名是否重复(特殊角色不能改动)
         * 4：创建新角色
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},修改后台的角色信息",uid);
        try {
            long roleId = ValidationUtil.checkAndAssignLong(backUserBO.getRoleId());
            String roleName = ValidationUtil.checkBlankAndAssignString(backUserBO.getRoleName());
            if(roleId == 1L || roleId == 2L){
                throw new RuntimeException("不能修改管理员角色信息");
            }
            if(Roles.ADMIN.equals(roleName) || Roles.SUPER_ADMIN.equals(roleName)){
                throw new RuntimeException("不能命名为管理员或超级管理员");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backRoleService.updateRole(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 删除角色信息（支持批量删除）
     * {
     *     "roleIds"
     * }
     * @return
     */
    @RequestMapping("/role/deleteRoles")
    public JsonResult deleteRoles(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入roleId【数组类型】参数
         * 2：基本参数校验
         * 3：判断角色id是否包含有一些特殊的角色，用来防止被删除的
         * 4：删除角色
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},删除后台的角色信息",uid);
        try {
            List<Long> roleIds = backUserBO.getRoleIds();
            if(roleIds==null || roleIds.size()==0){
                throw new RuntimeException("请输入要删除的角色id");
            }
            if(roleIds.contains(1L) || roleIds.contains(2L)){
                throw new RuntimeException("不能删除管理员和超级管理员角色");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backRoleService.deleteRoles(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 显示角色下的权限信息
     * {
     *     "roleId"
     * }
     * @return
     */
    @RequestMapping("/role/showAuthoritiesUnderRole")
    public JsonResult showAuthoritiesUnderRole(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入roleId参数
         * 2：基本参数校验
         * 3：显示该角色下的权限信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看后台的角色信息",uid);
        try {
            Long roleId = ValidationUtil.checkAndAssignLong(backUserBO.getRoleId());
            if(roleId.equals(1L)){
                throw new RuntimeException("无法查看超级管理员的权限信息");
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            List<BackAuthesVO> backAuthesVOList = backRoleService.showAuthoritiesUnderRole(backUserBO);
            return success(backAuthesVOList);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 给角色分配权限信息
     * {
     *     "roleId"
     *     "authIds"
     * }
     * @return
     */
    @RequestMapping("/role/assignAuthorities4Role")
    public JsonResult assignAuthorities4Role(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入roleId，authorityIds【数组类型】参数
         * 2：基本参数校验
         * 3：确保该角色id是否是一些特殊的角色id（否则不能给修改）
         * 4：修改掉该角色id下的权限信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},分配后台角色的权限信息",uid);
        try {
            long roleId = ValidationUtil.checkAndAssignLong(backUserBO.getRoleId());
            if(roleId == 1L || roleId == 2L){
                throw new RuntimeException("不能给管理员或超级管理员分配权限信息");
            }
            List<Long> authIds = backUserBO.getAuthIds();
            for(Long authId : BackAuthesEnum.FORBIDDEN_AUTH_INDEX){
                if(authIds.contains(authId)){
                    throw new RuntimeException("不能分配核心权限");
                }
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            backRoleService.assignAuthorities4Role(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    //===================================以下是权限模块=========================================================
    /**
     * 查看权限
     * {
     *     "page"
     *     "rowNum"
     *     "authName"
     *     "startTime"
     *     "endTime"
     * }
     * @return
     */
    @RequestMapping("/auth/queryAuthorities")
    public JsonResult queryAuthorities(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前台传入page，rowNum等查询参数
         * 2：基本参数校验
         * 3：查询指定条件下的权限信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看后台的权限信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(backUserBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(backUserBO.getPage(),1);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getEndTime(),Integer.MAX_VALUE);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getStartTime(),0);
            if(startTime > endTime){
                backUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                backUserBO.setEndTime(endTime);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            PageDTO pageDTO = backAuthService.queryAuthorities(backUserBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }


}
