package fun.xianlai.admax.module.iam.service;

import org.springframework.data.domain.Page;
import fun.xianlai.admax.module.iam.model.entity.Permission;
import fun.xianlai.admax.module.iam.model.enums.PermissionType;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
public interface PermissionService {
    /**
     * 新增权限
     *
     * @param permission 新权限信息
     * @return 新权限对象
     */
    Permission createPermission(Permission permission);

    /**
     * 删除权限
     *
     * @param permissionId 要删除的权限ID
     */
    void deletePermission(Long permissionId);

    /**
     * 更新权限
     *
     * @param permission 新权限数据
     * @return 权限对象
     */
    Permission updatePermission(Permission permission);

    /**
     * 分页查询权限列表
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 权限分页
     */
    Page<Permission> getPermissionsByPage(int pageNum, int pageSize);

    /**
     * 条件查询权限分页
     *
     * @param pageNum    页码
     * @param pageSize   页大小
     * @param module     所属模块（模糊）
     * @param type       类型
     * @param identifier 权限标识（模糊）
     * @param name       权限名称（模糊）
     * @param activated  生效状态
     * @param stTime     创建时间左边界（含）
     * @param edTime     创建时间右边界（含）
     * @return 权限分页
     */
    Page<Permission> getPermissionsByPageConditionally(int pageNum, int pageSize, String module, PermissionType type, String identifier, String name, Boolean activated, Date stTime, Date edTime);

    /**
     * 获取全量权限数据
     *
     * @return 全量权限数据
     */
    List<Permission> listPermissions();

    /**
     * 获取某角色的权限
     *
     * @param roleId 角色ID
     * @return 某角色的权限ID列表
     */
    List<Long> getPermissionIdsOfRole(Long roleId);

    /**
     * 查询某权限的排名
     *
     * @param permissionId 权限ID
     * @return 排名（从1开始）
     */
    Long getRowNum(Long permissionId);

    /**
     * 查询用户所有正在生效的权限标识
     *
     * @param userId 用户ID
     * @return 权限标识符列表
     */
    List<String> getActivatedPermissionIdentifiers(Long userId);

    /**
     * 更新REFRESH_PERMISSION_AFTER时间戳
     */
    void updateRefreshPermissionAfter(Date timestamp);

    /**
     * 更新PERMISSION_REFRESHED时间戳
     */
    void updatePermissionRefreshed(Long userId, Date timestamp);
}
