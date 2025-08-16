package fun.xianlai.admax.module.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.iam.model.constant.AuthorityConst;
import fun.xianlai.admax.module.iam.model.entity.Role;
import fun.xianlai.admax.module.iam.model.form.GrantForm;
import fun.xianlai.admax.module.iam.model.form.RoleConditionForm;
import fun.xianlai.admax.module.iam.model.form.RoleForm;
import fun.xianlai.admax.module.iam.service.RoleService;

import java.util.List;

/**
 * @author WyattLau
 * @date 2024/3/13
 */
@Slf4j
@RestController
@RequestMapping("/api/iam/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 新增角色
     *
     * @param input 新角色信息
     * @return role 新角色对象
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_ADD)
    @PostMapping("/addRole")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res addRole(@RequestBody RoleForm input) {
        Assert.notNull(input, "新角色信息为空");
        Assert.hasText(input.getIdentifier(), "角色标识符为空");
        log.info("请求参数: {}", input);

        log.info("RoleForm转换为Role");
        Role roleInfo = input.convert();

        log.info("调用新建角色服务");
        Role role = roleService.createRole(roleInfo);

        return new Res().success().addData("role", role);
    }

    /**
     * 删除角色
     *
     * @param roleId 要删除的角色ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_DELETE)
    @GetMapping("/deleteRole")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res deleteRole(@RequestParam("roleId") Long roleId) {
        Assert.notNull(roleId, "角色ID为空");
        log.info("请求参数: roleId={}", roleId);

        log.info("调用删除角色服务");
        roleService.deleteRole(roleId);

        return new Res().success();
    }

    /**
     * 更新角色的授权
     *
     * @param input { roleId 角色ID, grant 授权ID列表, cancel 取消授权ID列表 }
     * @return { failGrant 授权失败ID列表, failCancel 取消授权失败ID列表 }
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.UPDATE_GRANTS)
    @PostMapping("/updateGrants")
    public Res updateGrants(@RequestBody GrantForm input) {
        Assert.notNull(input, "授权/取消授权表单为空");
        log.info("请求参数: {}", input);

        List<Long> failGrant = null;
        List<Long> failCancel = null;
        try {
            log.info("授权");
            failGrant = roleService.grant(input.getRoleId(), input.getGrant());
        } catch (IllegalArgumentException e) {
            log.info("无须授权");
        }
        try {
            log.info("解除授权");
            failCancel = roleService.cancelGrant(input.getRoleId(), input.getCancel());
        } catch (IllegalArgumentException e) {
            log.info("无须解除授权");
        }

        return new Res().success()
                .addData("failGrant", failGrant)
                .addData("failCancel", failCancel);
    }

    /**
     * 修改角色
     *
     * @param input 要修改的角色数据
     * @return role 新角色对象
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_EDIT)
    @PostMapping("/editRole")
    public Res editRole(@RequestBody RoleForm input) {
        Assert.notNull(input, "角色信息为空");
        Assert.notNull(input.getId(), "角色ID为空");
        log.info("请求参数: {}", input);

        log.info("RoleForm转换为Role");
        Role roleInfo = input.convert();

        log.info("调用角色更新服务");
        Role role = roleService.updateRole(roleInfo);

        return new Res().success().addData("role", role);
    }

    /**
     * 交换两个角色位置
     *
     * @param id1 角色1
     * @param id2 角色2
     * @return {role1 新角色1, role2 新角色2}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_EDIT)
    @GetMapping("/swapPosition")
    public Res swapPosition(@RequestParam("id1") Long id1, @RequestParam("id2") Long id2) {
        Assert.notNull(id1, "id1为空");
        Assert.notNull(id2, "id2为空");
        log.info("请求参数: id1={}, id2={}", id1, id2);

        log.info("获取两个角色数据");
        Role role1 = roleService.findRoleById(id1);
        Role role2 = roleService.findRoleById(id2);
        if (role1 != null && role2 != null) {
            log.info("(id1, sortId1)=({}, {})", id1, role1.getSortId());
            log.info("(id2, sortId2)=({}, {})", id2, role2.getSortId());

            log.info("交换sortId");
            Long tempSortId = role1.getSortId();
            role1.setSortId(role2.getSortId());
            role2.setSortId(tempSortId);

            Role newRole1 = roleService.updateRole(role1);
            Role newRole2 = roleService.updateRole(role2);
            log.info("交换sortId完成");

            return new Res().success()
                    .addData("role1", newRole1)
                    .addData("role2", newRole2);
        } else {
            throw new OnceException("无法找到角色数据");
        }
    }

    /**
     * 查询角色分页
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, roles 角色分页}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_QUERY)
    @GetMapping("/getRolesByPage")
    public Res getRolesByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        log.info("请求参数：pageNum={}, pageSize={}", pageNum, pageSize);

        log.info("调用查询角色分页服务");
        Page<Role> roles = roleService.getRolesByPage(pageNum, pageSize);

        return new Res().success()
                .addData("pageNum", roles.getPageable().getPageNumber())
                .addData("pageSize", roles.getPageable().getPageSize())
                .addData("totalPages", roles.getTotalPages())
                .addData("totalElements", roles.getTotalElements())
                .addData("roles", roles.getContent());
    }

    /**
     * 条件查询角色分页
     *
     * @param form 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, roles 角色分页}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_QUERY)
    @PostMapping("/getRolesByPageConditionally")
    public Res getRolesByPageConditionally(@RequestBody RoleConditionForm form) {
        Assert.notNull(form, "查询条件为空");
        log.info("请求参数：{}", form);

        log.info("调用条件查询角色分页服务");
        Page<Role> roles = roleService.getRolesByPageConditionally(
                form.getPageNum(),
                form.getPageSize(),
                form.getIdentifier(),
                form.getName(),
                form.getActivated(),
                form.getStTime(),
                form.getEdTime(),
                form.getPermission()
        );

        return new Res().success()
                .addData("pageNum", roles.getPageable().getPageNumber())
                .addData("pageSize", roles.getPageable().getPageSize())
                .addData("totalPages", roles.getTotalPages())
                .addData("totalElements", roles.getTotalElements())
                .addData("roles", roles.getContent());
    }

    /**
     * 获取全量角色列表
     *
     * @return roles 全量角色列表
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_QUERY)
    @GetMapping("/getRoles")
    public Res getRoles() {
        List<Role> roles = roleService.listRoles();
        return new Res().success().addData("roles", roles);
    }

    /**
     * 查询某用户所具有的角色ID列表
     *
     * @param userId 用户ID
     * @return 该用户所具有的角色ID列表
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_QUERY)
    @GetMapping("/getRoleIdsOfUser")
    public Res getRoleIdsOfUser(@RequestParam("userId") Long userId) {
        Assert.notNull(userId, "用户ID为空");

        List<Long> roleIds = roleService.getRoleIdsOfUser(userId);
        return new Res().success().addData("roleIds", roleIds);
    }

    /**
     * 查询角色的排名（从1开始）
     *
     * @param roleId 角色ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_QUERY)
    @GetMapping("/getRowNumStartFrom1")
    public Res getRowNumStartFrom1(@RequestParam("roleId") Long roleId) {
        Assert.notNull(roleId, "角色ID为空");

        return new Res().success().addData("rowNum", roleService.getRowNum(roleId));
    }
}
