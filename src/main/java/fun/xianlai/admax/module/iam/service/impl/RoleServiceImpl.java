package fun.xianlai.admax.module.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.iam.model.constant.RoleRedisConst;
import fun.xianlai.admax.module.iam.model.entity.Role;
import fun.xianlai.admax.module.iam.model.entity.RolePermission;
import fun.xianlai.admax.module.iam.repository.RolePermissionRepository;
import fun.xianlai.admax.module.iam.repository.RoleRepository;
import fun.xianlai.admax.module.iam.repository.UserRoleRepository;
import fun.xianlai.admax.module.iam.service.PermissionService;
import fun.xianlai.admax.module.iam.service.RoleService;
import fun.xianlai.admax.util.DateFormatter;
import fun.xianlai.admax.util.PrimaryKeyGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public Role createRole(Role role) {
        Assert.notNull(role, "新角色数据为空");
        Assert.hasText(role.getIdentifier(), "新权限标识符为空");
        log.info("输入参数: {}", role);
        try {
            log.info("插入记录");
            role.setId(null);
            if (role.getActivated() == null) role.setActivated(false);
            role.setSortId(new PrimaryKeyGenerator().next());
            role.setCreateTime(new Date());
            role = roleRepository.save(role);
            log.info("新角色成功保存到数据库: id={}", role.getId());
            return role;
        } catch (DataIntegrityViolationException e) {
            log.info(e.getMessage());
            throw new OnceException("角色标识符重复");
        }
    }

    @Override
    public void deleteRole(Long roleId) {
        Assert.notNull(roleId, "角色ID据为空");
        log.info("输入参数: roleId={}", roleId);

        log.info("删除与本角色相关的用户-角色关系");
        userRoleRepository.deleteByRoleId(roleId);
        log.info("删除与本角色相关的角色-权限关系");
        rolePermissionRepository.deleteByRoleId(roleId);
        log.info("删除角色数据库记录");
        roleRepository.deleteById(roleId);

        log.info("更新公共标记REFRESH_ROLE_AFTER");
        updateRefreshRoleAfter(new Date());
    }

    @Override
    public List<Long> grant(Long roleId, List<Long> permissionIds) {
        Assert.notNull(roleId, "roleId为空");
        Assert.notNull(permissionIds, "permissionIds为空");
        log.info("输入参数: roleId={}, permissionIds={}", roleId, permissionIds);

        List<Long> failList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            try {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissionRepository.save(rp);
                log.info("授权成功: (roleId={}, permissionId={})", roleId, permissionId);
            } catch (Exception e) {
                failList.add(permissionId);
            }
        }

        if (failList.size() < permissionIds.size()) {
            log.info("有授权成功，要更新REFRESH_PERMISSION_AFTER时间戳，以动态更新用户权限缓存");
            permissionService.updateRefreshPermissionAfter(new Date());
        }

        log.info("授权失败的权限ID：{}", failList);
        return failList;
    }

    @Override
    public List<Long> cancelGrant(Long roleId, List<Long> permissionIds) {
        Assert.notNull(roleId, "roleId为空");
        Assert.notNull(permissionIds, "permissionIds为空");
        log.info("输入参数: roleId={}, permissionIds={}", roleId, permissionIds);

        List<Long> failList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            try {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissionRepository.delete(rp);
                log.info("解除授权成功: (roleId={}, permissionId={})", roleId, permissionId);
            } catch (Exception e) {
                failList.add(permissionId);
            }
        }

        if (failList.size() < permissionIds.size()) {
            log.info("有解除授权成功，要更新REFRESH_PERMISSION_AFTER时间戳，以动态更新用户权限缓存");
            permissionService.updateRefreshPermissionAfter(new Date());
        }

        log.info("解除授权失败的权限ID：{}", failList);
        return failList;
    }

    @Override
    public Role updateRole(Role role) {
        Assert.notNull(role, "角色数据为空");
        Assert.notNull(role.getId(), "角色id为空");
        log.info("输入参数: {}", role);
        String identifier = role.getIdentifier();
        String name = role.getName();
        Boolean activated = role.getActivated();
        Long sortId = role.getSortId();
        Date createTime = role.getCreateTime();
        String remark = role.getRemark();

        log.info("查询是否存在该角色");
        Optional<Role> oldRole = roleRepository.findById(role.getId());
        if (oldRole.isPresent()) {
            log.info("角色存在，组装用来更新的对象");
            Role newRole = oldRole.get();
            if (identifier != null) newRole.setIdentifier(identifier);
            if (name != null) newRole.setName(name);
            if (activated != null) newRole.setActivated(activated);
            if (sortId != null) newRole.setSortId(sortId);
            if (createTime != null) newRole.setCreateTime(createTime);
            if (remark != null) newRole.setRemark(remark);

            try {
                log.info("更新数据库");
                newRole = roleRepository.save(newRole);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new OnceException("角色标识重复");
            }

            boolean critical = false;
            if (identifier != null) critical = true;
            if (activated != null) critical = true;
            if (critical) {
                log.info("编辑此角色数据影响到用户权限控制，需要更新缓存");
                log.info("更新公共标记REFRESH_ROLE_AFTER");
                updateRefreshRoleAfter(new Date());
            }
            return newRole;
        } else {
            throw new OnceException("要修改的角色不存在");
        }
    }

    @Override
    public Page<Role> getRolesByPage(int pageNum, int pageSize) {
        log.info("输入参数: pageNum={}, pageSize={}", pageNum, pageSize);

        Sort sort = Sort.by(Sort.Order.asc("sortId"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Role> rolePage = roleRepository.findAll(pageable);
        log.info("查询结果: {}", rolePage);

        return rolePage;
    }

    @Override
    public Page<Role> getRolesByPageConditionally(
            int pageNum,
            int pageSize,
            String identifier,
            String name,
            Boolean activated,
            Date stTime,
            Date edTime,
            String permission) {
        log.info("输入参数: pageNum={}, pageSize={}, identifier={}, name={}, activated={}, stTime={}, edTime={}, permission={}",
                pageNum, pageSize, identifier, name, activated, DateFormatter.commonFormat(stTime), DateFormatter.commonFormat(edTime), permission);

        Sort sort = Sort.by(Sort.Order.asc("sortId"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Role> rolePage = roleRepository.findConditionally(
                identifier,
                name,
                activated == null ? null : (activated ? 1 : 0),
                stTime,
                edTime,
                permission,
                pageable);
        log.info("查询结果: {}", rolePage);

        return rolePage;
    }

    @Override
    public List<Role> listRoles() {
        Sort sort = Sort.by(Sort.Order.asc("sortId"));
        List<Role> roles = roleRepository.findAll(sort);
        log.info("查询结果: {}", roles);
        return roles;
    }

    @Override
    public List<Long> getRoleIdsOfUser(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);

        return roleRepository.findIdsByUserId(userId);
    }

    @Override
    public Long getRowNum(Long roleId) {
        return roleRepository.findRowNumById(roleId);
    }

    @Override
    public Role findRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    /**
     * 公共标记：REFRESH_ROLE_AFTER   应刷新角色时间戳
     * 用户标记：ROLE_REFRESHED       已刷新角色时间戳
     * <p>
     * 一、由于角色的数据库变更造成的需要大范围用户刷新角色缓存的场景
     * 1、角色的identifier、activated变更时，更新公共标记REFRESH_ROLE_AFTER。
     * 2、若用户标记ROLE_REFRESHED >= REFRESH_ROLE_AFTER
     * 则先查询缓存的角色数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新ROLE_REFRESHED。
     * <p>
     * 二、由于用户自身变更造成的自己需要刷新角色缓存的场景
     * 1、用户自身变更时，置用户标记ROLE_REFRESHED为0。
     * 2、若用户标记ROLE_REFRESHED >= REFRESH_ROLE_AFTER
     * 则先查询缓存的角色数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新ROLE_REFRESHED。
     */
    @Override
    public List<String> getActivatedRoleIdentifiers(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);
        List<String> roles = null;

        Date t1 = (Date) redis.opsForValue().get(RoleRedisConst.REFRESH_ROLE_AFTER); // 公共标记
        Date t2 = (Date) StpUtil.getSessionByLoginId(userId).get(RoleRedisConst.ROLE_REFRESHED);    // 用户标记
        log.info("公共标记REFRESH_ROLE_AFTER: {}", t1 == null ? null : DateFormatter.commonFormat(t1));
        log.info("用户标记ROLE_REFRESHED: {}", t2 == null ? null : DateFormatter.commonFormat(t2));

        if (t1 == null || (t2 != null && t2.compareTo(t1) >= 0)) {
            log.info("先查询Session缓存的角色数据");
            roles = (List<String>) StpUtil.getSessionByLoginId(userId).get(RoleRedisConst.USER_ROLES_KEY);
        }

        if (roles == null) {
            log.info("无缓存或缓存不是最新，查询数据库，并更新缓存");
            List<Role> activatedRoles = roleRepository.findActivatedByUserId(userId);
            log.info("提取标识符字符串列表");
            roles = new ArrayList<>();
            for (Role item : activatedRoles) {
                roles.add(item.getIdentifier());
            }
            log.info("更新缓存");
            StpUtil.getSessionByLoginId(userId).set(RoleRedisConst.USER_ROLES_KEY, roles);
            log.info("更新用户标记ROLE_REFRESHED");
            updateRoleRefreshed(userId, new Date());
        }

        log.info("用户的角色标识列表: {}", roles);
        return roles;
    }

    @Override
    public void updateRefreshRoleAfter(Date timestamp) {
        redis.opsForValue().set(RoleRedisConst.REFRESH_ROLE_AFTER, timestamp);
        log.info("已更新REFRESH_ROLE_AFTER为: {}", DateFormatter.commonFormat(timestamp));
    }

    @Override
    public void updateRoleRefreshed(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set(RoleRedisConst.ROLE_REFRESHED, timestamp);
        log.info("已更新用户标记ROLE_REFRESHED为: {}", DateFormatter.commonFormat(timestamp));
    }
}
