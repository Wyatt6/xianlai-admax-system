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
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.iam.model.constant.AuthorityConst;
import fun.xianlai.admax.module.iam.model.entity.Permission;
import fun.xianlai.admax.module.iam.model.form.PermissionConditionForm;
import fun.xianlai.admax.module.iam.model.form.PermissionForm;
import fun.xianlai.admax.module.iam.service.PermissionService;

import java.util.List;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Slf4j
@RestController
@RequestMapping("/api/iam/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 新增权限
     *
     * @param input 新权限信息
     * @return permission 新权限对象
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_ADD)
    @PostMapping("/addPermission")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res addPermission(@RequestBody PermissionForm input) {
        Assert.notNull(input, "新权限信息为空");
        Assert.hasText(input.getIdentifier(), "权限标识符为空");
        log.info("请求参数: {}", input);

        log.info("PermissionForm转换为Permission");
        Permission permissionInfo = input.convert();

        log.info("调用新建权限服务");
        Permission permission = permissionService.createPermission(permissionInfo);

        return new Res().success().addData("permission", permission);
    }

    /**
     * 删除权限
     *
     * @param permissionId 要删除的权限ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_DELETE)
    @GetMapping("/deletePermission")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res deletePermission(@RequestParam("permissionId") Long permissionId) {
        Assert.notNull(permissionId, "权限ID为空");
        log.info("请求参数: permissionId={}", permissionId);

        log.info("调用删除权限服务");
        permissionService.deletePermission(permissionId);

        return new Res().success();
    }

    /**
     * 修改权限
     *
     * @param input 要修改的权限数据
     * @return permission 新权限对象
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_EDIT)
    @PostMapping("/editPermission")
    public Res editPermission(@RequestBody PermissionForm input) {
        Assert.notNull(input, "权限信息为空");
        Assert.notNull(input.getId(), "权限ID为空");
        log.info("请求参数: {}", input);

        log.info("PermissionForm转换为Permission");
        Permission permissionInfo = input.convert();

        log.info("调用权限更新服务");
        Permission permission = permissionService.updatePermission(permissionInfo);

        return new Res().success().addData("permission", permission);
    }

    /**
     * 查询权限分页
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, permissions 权限分页}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_QUERY)
    @GetMapping("/getPermissionsByPage")
    public Res getPermissionsByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        log.info("请求参数：pageNum={}, pageSize={}", pageNum, pageSize);

        log.info("调用查询权限分页服务");
        Page<Permission> permissions = permissionService.getPermissionsByPage(pageNum, pageSize);

        return new Res().success()
                .addData("pageNum", permissions.getPageable().getPageNumber())
                .addData("pageSize", permissions.getPageable().getPageSize())
                .addData("totalPages", permissions.getTotalPages())
                .addData("totalElements", permissions.getTotalElements())
                .addData("permissions", permissions.getContent());
    }

    /**
     * 条件查询权限分页
     *
     * @param form 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, permissions 权限分页}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_QUERY)
    @PostMapping("/getPermissionsByPageConditionally")
    public Res getPermissionsByPageConditionally(@RequestBody PermissionConditionForm form) {
        Assert.notNull(form, "查询条件为空");
        log.info("请求参数：{}", form);

        log.info("调用条件查询权限分页服务");
        Page<Permission> permissions = permissionService.getPermissionsByPageConditionally(
                form.getPageNum(),
                form.getPageSize(),
                form.getModule(),
                form.getType(),
                form.getIdentifier(),
                form.getName(),
                form.getActivated(),
                form.getStTime(),
                form.getEdTime());

        return new Res().success()
                .addData("pageNum", permissions.getPageable().getPageNumber())
                .addData("pageSize", permissions.getPageable().getPageSize())
                .addData("totalPages", permissions.getTotalPages())
                .addData("totalElements", permissions.getTotalElements())
                .addData("permissions", permissions.getContent());
    }

    /**
     * 获取全量权限数据
     *
     * @return 全量权限数据
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.ROLE_QUERY)
    @GetMapping("/getPermissions")
    public Res getPermissions() {
        List<Permission> permissions = permissionService.listPermissions();
        return new Res().success().addData("permissions", permissions);
    }

    /**
     * 查询某角色所具有的权限ID列表
     *
     * @param roleId 角色ID
     * @return 该角色所具有的权限ID列表
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_QUERY)
    @GetMapping("/getPermissionIdsOfRole")
    public Res getPermissionIdsOfRole(@RequestParam("roleId") Long roleId) {
        Assert.notNull(roleId, "角色ID为空");

        List<Long> permissionIds = permissionService.getPermissionIdsOfRole(roleId);
        return new Res().success().addData("permissionIds", permissionIds);
    }

    /**
     * 查询权限的排名（从1开始）
     *
     * @param permissionId 权限ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.PERMISSION_QUERY)
    @GetMapping("/getRowNumStartFrom1")
    public Res getRowNumStartFrom1(@RequestParam("permissionId") Long permissionId) {
        Assert.notNull(permissionId, "权限ID为空");

        return new Res().success().addData("rowNum", permissionService.getRowNum(permissionId));
    }
}
