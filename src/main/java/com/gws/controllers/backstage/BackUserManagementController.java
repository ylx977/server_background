package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.BackAuthesEnum;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.common.constants.backstage.Roles;
import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.BackAuthesVO;
import com.gws.dto.backstage.BackAuthgroupsVO;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;
import com.gws.enums.SystemCode;
import com.gws.exception.ExceptionUtils;
import com.gws.services.backstage.BackAuthService;
import com.gws.services.backstage.BackRoleService;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.http.LangReadUtil;
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
@RequestMapping("/back/api/backstage/userManagement")
public class BackUserManagementController extends BaseController{

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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},创建后台用户",uid);
        try {
            ValidationUtil.checkBlankAndAssignString(backUserBO.getUsername(),RegexConstant.USERNAME_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPassword(),RegexConstant.PWD_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getContact());
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPersonName(),RegexConstant.NAME_REGEX);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backUserService.saveUser(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询后台用户",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        backUserBO.setLang(lang);
        try {
            ValidationUtil.checkMinAndAssignInt(backUserBO.getPage(),1,lang);
            ValidationUtil.checkMinAndAssignInt(backUserBO.getRowNum(),1,lang);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getStartTime(),lang,0);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getEndTime(),lang,Integer.MAX_VALUE);
            backUserBO.setStartTime(startTime);
            if(startTime > endTime){
                backUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                backUserBO.setEndTime(endTime);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO backUserPageDTO = backUserService.queryUserInfo(backUserBO);
            return success(backUserPageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},删除后台用户",uid);
        try {
            List<Long> uids = backUserBO.getUids();
            if(uids.contains(uid)){
               throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_DELETE_YOUR_ACCOUNT));
            }
            if(uids.contains(1L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_DELETE_SUPERADMIN_ACCOUNT));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backUserService.deleteUsers(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改后台用户",uid);
        try {
            long beOperatedUid = ValidationUtil.checkAndAssignLong(backUserBO.getUid());
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPassword(),RegexConstant.PWD_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getUsername(),RegexConstant.USERNAME_REGEX);
            ValidationUtil.checkBlankAndAssignString(backUserBO.getContact());
            ValidationUtil.checkBlankAndAssignString(backUserBO.getPersonName(),RegexConstant.NAME_REGEX);
            if(beOperatedUid == uid){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_MODIFY_YOUR_ACCOUNT_INFO));
            }
            if(beOperatedUid == 1L){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_MODIFY_SUPERADMIN_ACCOUNT_INFO));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backUserService.updateUsers(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查看后台用户的角色信息",uid);
        try {
            Long beOperatedUid = ValidationUtil.checkAndAssignLong(backUserBO.getUid());
            if(beOperatedUid.equals(uid)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_QUERY_OWN_ROLEINFO));
            }
            if(beOperatedUid.equals(1L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_QUERY_SUPERADMIN_ROLEINFO));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            List<BackAuthgroupsVO> backAuthgroupsVOList = backUserService.showRoleInfoUnderAccount(backUserBO);
            return success(backAuthgroupsVOList);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},分配后台用户的角色信息",uid);
        try {
            Long beOperatedUid = ValidationUtil.checkAndAssignLong(backUserBO.getUid());
            List<Long> authgroupIds = backUserBO.getAuthgroupIds();
            if(authgroupIds.contains(1L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_ASSIGN_SUPERADMIN_ROLE));
            }
            if(beOperatedUid.equals(uid)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_MODIFY_YOUR_ROLEINFO));
            }
            if(beOperatedUid.equals(1L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_MODIFY_SUPERADMIN_ROLEINFO));
            }
            if(!uid.equals(1L)){
                if(authgroupIds.contains(2L)){
                    throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_ASSIGN_ROLE_OF_ADMIN));
                }
            }
            //还有管理员之间不能相互修改
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backUserService.assignRoles4Account(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }


    /**
     * 修改用户的状态：冻结=0 还是 正常使用=0
     * 支持批量修改
     * {
     *     "uids"
     *     "isFreezed"
     * }
     * @return
     */
    @RequestMapping("/users/updateUserStatus")
    public JsonResult updateUserStatus(@RequestBody BackUserBO backUserBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改后台用户的状态",uid);
        try {
            List<Long> uids = backUserBO.getUids();
            if(uids.contains(uid)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_UPDATE_YOUR_STATUS));
            }
            if(uids.contains(1L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_UPDATE_SUPERADMIN_STATUS));
            }
            ValidationUtil.checkRangeAndAssignInt(backUserBO.getIsFreezed(),0,1);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backUserService.updateUserStatus(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},创建后台用户的角色信息",uid);
        try {
            String roleName = ValidationUtil.checkBlankAndAssignString(backUserBO.getRoleName());
            if(Roles.ADMIN.equals(roleName) || Roles.SUPER_ADMIN.equals(roleName)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_CREATE_ADMIN_ROLES));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backRoleService.createRole(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询后台的角色信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(backUserBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(backUserBO.getPage(),1);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getStartTime(),0);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getEndTime(),Integer.MAX_VALUE);
            backUserBO.setStartTime(startTime);
            if(startTime > endTime){
                backUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                backUserBO.setEndTime(endTime);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO backRolePageDTO = backRoleService.queryRoles(backUserBO);
            return success(backRolePageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改后台的角色信息",uid);
        try {
            long roleId = ValidationUtil.checkAndAssignLong(backUserBO.getRoleId());
            String roleName = ValidationUtil.checkBlankAndAssignString(backUserBO.getRoleName());
            if(roleId == 1L || roleId == 2L){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_MODIFY_ADMIN_ROLES));
            }
            if(Roles.ADMIN.equals(roleName) || Roles.SUPER_ADMIN.equals(roleName)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_USER_ADMIN_ROLENAME));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backRoleService.updateRole(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},删除后台的角色信息",uid);
        try {
            List<Long> roleIds = backUserBO.getRoleIds();
            if(roleIds==null || roleIds.size()==0){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.INPUT_DELETED_ROLEID));
            }
            if(roleIds.contains(1L) || roleIds.contains(2L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_DELETE_ADMIN_ROLES));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backRoleService.deleteRoles(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查看后台的角色信息",uid);
        try {
            Long roleId = ValidationUtil.checkAndAssignLong(backUserBO.getRoleId());
            if(roleId.equals(1L)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_QUERY_SUPERADMIN_AUTHES));
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            List<BackAuthesVO> backAuthesVOList = backRoleService.showAuthoritiesUnderRole(backUserBO);
            return success(backAuthesVOList);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},分配后台角色的权限信息",uid);
        try {
            long roleId = ValidationUtil.checkAndAssignLong(backUserBO.getRoleId());
            if(roleId == 1L || roleId == 2L){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_ASSIGN_AUTH_FOR_ADMINS));
            }
            List<Long> authIds = backUserBO.getAuthIds();
            for(Long authId : BackAuthesEnum.FORBIDDEN_AUTH_INDEX){
                if(authIds.contains(authId)){
                    throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.CANT_ASSIGN_CORE_AUTHES));
                }
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backRoleService.assignAuthorities4Role(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
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
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查看后台的权限信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(backUserBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(backUserBO.getPage(),1);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getEndTime(),Integer.MAX_VALUE);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(backUserBO.getStartTime(),0);
            backUserBO.setStartTime(startTime);
            if(startTime > endTime){
                backUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                backUserBO.setEndTime(endTime);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO pageDTO = backAuthService.queryAuthorities(backUserBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }


}
