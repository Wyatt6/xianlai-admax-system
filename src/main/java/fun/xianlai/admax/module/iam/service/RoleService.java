package fun.xianlai.admax.module.iam.service;

import org.springframework.data.domain.Page;
import fun.xianlai.admax.module.iam.model.entity.Role;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
public interface RoleService {
    /**
     * 新增角色
     *
     * @param role 新角色信息
     * @return 新角色对象
     */
    Role createRole(Role role);

    /**
     * 删除角色
     *
     * @param roleId 要删除的角色ID
     */
    void deleteRole(Long roleId);

    /**
     * 授权（绑定roleId和permissionIds）
     *
     * @param roleId        角色ID
     * @param permissionIds 该角色要绑定的权限ID
     * @return 授权失败的权限ID列表
     */
    List<Long> grant(Long roleId, List<Long> permissionIds);

    /**
     * 解除授权（取消绑定roleId和permissionIds）
     *
     * @param roleId        角色ID
     * @param permissionIds 该角色要解除绑定的权限ID列表
     * @return 解除授权失败的权限ID列表
     */
    List<Long> cancelGrant(Long roleId, List<Long> permissionIds);

    /**
     * 更新角色
     *
     * @param role 角色新数据（id非空）
     * @return 角色的新对象
     */
    Role updateRole(Role role);

    /**
     * 查询角色分页
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 角色分页
     */
    Page<Role> getRolesByPage(int pageNum, int pageSize);

    /**
     * 条件查询角色分页
     *
     * @param pageNum    页码
     * @param pageSize   页大小
     * @param identifier 角色标识（模糊）
     * @param name       角色名称（模糊）
     * @param activated  生效状态
     * @param stTime     创建时间左边界（含）
     * @param edTime     创建时间右边界（含）
     * @param permission 角色所包含的权限
     * @return 角色分页
     */
    Page<Role> getRolesByPageConditionally(int pageNum, int pageSize, String identifier, String name, Boolean activated, Date stTime, Date edTime, String permission);

    /**
     * 获取全量角色列表
     *
     * @return 全量角色列表
     */
    List<Role> listRoles();

    /**
     * 获取某用户的角色
     *
     * @param userId 用户ID
     * @return 某用户的角色ID列表
     */
    List<Long> getRoleIdsOfUser(Long userId);

    /**
     * 查询某角色的排名
     *
     * @param roleId 角色ID
     * @return 排名（从1开始）
     */
    Long getRowNum(Long roleId);

    /**
     * 根据ID查找角色
     *
     * @param roleId 角色ID
     * @return 角色对象
     */
    Role findRoleById(Long roleId);

    /**
     * 查询用户所有正在生效的角色标识
     *
     * @param userId 用户ID
     * @return 角色标识符列表
     */
    List<String> getActivatedRoleIdentifiers(Long userId);

    /**
     * 更新REFRESH_ROLE_AFTER时间戳
     */
    void updateRefreshRoleAfter(Date timestamp);

    /**
     * 更新ROLE_REFRESHED时间戳
     */
    void updateRoleRefreshed(Long userId, Date timestamp);
}
