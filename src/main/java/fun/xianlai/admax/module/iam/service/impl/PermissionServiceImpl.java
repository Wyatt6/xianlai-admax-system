package fun.xianlai.admax.module.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.iam.model.constant.PermissionRedisConst;
import fun.xianlai.admax.module.iam.model.constant.RoleRedisConst;
import fun.xianlai.admax.module.iam.model.entity.Permission;
import fun.xianlai.admax.module.iam.model.enums.PermissionType;
import fun.xianlai.admax.module.iam.repository.PermissionRepository;
import fun.xianlai.admax.module.iam.repository.RolePermissionRepository;
import fun.xianlai.admax.module.iam.service.PermissionService;
import fun.xianlai.admax.util.DateFormatter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public Permission createPermission(Permission permission) {
        Assert.notNull(permission, "新权限数据为空");
        Assert.hasText(permission.getIdentifier(), "新权限标识符为空");
        log.info("输入参数: {}", permission);
        try {
            log.info("插入记录");
            permission.setId(null);
            if (permission.getActivated() == null) permission.setActivated(false);
            permission.setCreateTime(new Date());
            permission = permissionRepository.save(permission);
            log.info("新权限成功保存到数据库: id={}", permission.getId());
            return permission;
        } catch (DataIntegrityViolationException e) {
            log.info(e.getMessage());
            throw new OnceException("权限标识符重复");
        }
    }

    @Override
    public void deletePermission(Long permissionId) {
        Assert.notNull(permissionId, "权限ID据为空");
        log.info("输入参数: permissionId={}", permissionId);

        log.info("删除与本权限相关的角色-权限关系");
        rolePermissionRepository.deleteByPermissionId(permissionId);
        log.info("数据库删除本权限数据");
        permissionRepository.deleteById(permissionId);

        log.info("更新标记REFRESH_PERMISSION_AFTER，表示此时间后应当刷新缓存的权限数据");
        updateRefreshPermissionAfter(new Date());
    }

    @Override
    public Permission updatePermission(Permission permission) {
        Assert.notNull(permission, "权限数据为空");
        Assert.notNull(permission.getId(), "权限ID为空");
        log.info("输入参数: {}", permission);
        String module = permission.getModule();
        PermissionType type = permission.getType();
        String identifier = permission.getIdentifier();
        String name = permission.getName();
        Boolean activated = permission.getActivated();
        Date createTime = permission.getCreateTime();
        String remark = permission.getRemark();

        log.info("查询是否存在该权限");
        Optional<Permission> oldPermission = permissionRepository.findById(permission.getId());
        if (oldPermission.isPresent()) {
            log.info("权限存在，组装用来更新的对象");
            Permission newPermission = oldPermission.get();
            if (module != null) newPermission.setModule(module);
            if (type != null) newPermission.setType(type);
            if (identifier != null) newPermission.setIdentifier(identifier);
            if (name != null) newPermission.setName(name);
            if (activated != null) newPermission.setActivated(activated);
            if (createTime != null) newPermission.setCreateTime(createTime);
            if (remark != null) newPermission.setRemark(remark);

            try {
                log.info("更新数据库");
                newPermission = permissionRepository.save(newPermission);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new OnceException("权限标识重复");
            }

            boolean critical = false;
            if (identifier != null) critical = true;
            if (activated != null) critical = true;
            if (critical) {
                log.info("编辑此权限数据影响到用户权限控制，需要更新缓存");
                log.info("更新公共标记REFRESH_PERMISSION_AFTER");
                updateRefreshPermissionAfter(new Date());
            }
            return newPermission;
        } else {
            throw new OnceException("要修改的权限不存在");
        }
    }

    @Override
    public Page<Permission> getPermissionsByPage(int pageNum, int pageSize) {
        log.info("输入参数: pageNum={}, pageSize={}", pageNum, pageSize);

        Sort sort = Sort.by(
                Sort.Order.asc("module"),
                Sort.Order.asc("type"),
                Sort.Order.asc("identifier")
        );
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Permission> permissionPage = permissionRepository.findAll(pageable);
        log.info("查询结果: {}", permissionPage);

        return permissionPage;
    }

    @Override
    public Page<Permission> getPermissionsByPageConditionally(
            int pageNum,
            int pageSize,
            String module,
            PermissionType type,
            String identifier,
            String name,
            Boolean activated,
            Date stTime,
            Date edTime) {
        log.info("输入参数: pageNum={}, pageSize={}, module={}, type={}, identifier={}, name={}, activated={}, stTime={}, edTime={}",
                pageNum, pageSize, module, type, identifier, name, activated,
                DateFormatter.commonFormat(stTime), DateFormatter.commonFormat(edTime));

        Sort sort = Sort.by(
                Sort.Order.asc("module"),
                Sort.Order.asc("type"),
                Sort.Order.asc("identifier")
        );
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Specification<Permission> spec = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<String> recordModule = root.get("module");
                Path<PermissionType> recordType = root.get("type");
                Path<String> recordIdentifier = root.get("identifier");
                Path<String> recordName = root.get("name");
                Path<Boolean> recordActivated = root.get("activated");
                Path<Date> recordCreateTime = root.get("createTime");

                List<Predicate> p = new ArrayList<>();
                if (module != null && !module.isBlank()) {
                    p.add(criteriaBuilder.like(recordModule, "%" + module + "%"));
                }
                if (type != null) {
                    p.add(criteriaBuilder.equal(recordType, type));
                }
                if (identifier != null && !identifier.isBlank()) {
                    p.add(criteriaBuilder.like(recordIdentifier, "%" + identifier + "%"));
                }
                if (name != null && !name.isBlank()) {
                    p.add(criteriaBuilder.like(recordName, "%" + name + "%"));
                }
                if (activated != null) {
                    p.add(criteriaBuilder.equal(recordActivated, activated));
                }
                if (stTime != null) {
                    p.add(criteriaBuilder.greaterThanOrEqualTo(recordCreateTime, stTime));
                }
                if (edTime != null) {
                    p.add(criteriaBuilder.lessThanOrEqualTo(recordCreateTime, edTime));
                }

                return criteriaBuilder.and(p.toArray(new Predicate[0]));
            }
        };

        Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
        log.info("查询结果: {}", permissionPage);

        return permissionPage;
    }

    @Override
    public List<Permission> listPermissions() {
        Sort sort = Sort.by(
                Sort.Order.asc("module"),
                Sort.Order.asc("type"),
                Sort.Order.asc("identifier")
        );
        return permissionRepository.findAll(sort);
    }

    @Override
    public List<Long> getPermissionIdsOfRole(Long roleId) {
        Assert.notNull(roleId, "角色ID为空");
        log.info("输入参数: roleId={}", roleId);

        return permissionRepository.findIdsByRoleId(roleId);
    }

    @Override
    public Long getRowNum(Long permissionId) {
        return permissionRepository.findRowNumById(permissionId);
    }

    /**
     * 公共标记：REFRESH_ROLE_AFTER         应刷新角色时间戳
     * 公共标记：REFRESH_PERMISSION_AFTER   应刷新权限时间戳
     * 用户标记：PERMISSION_REFRESHED       已刷新权限时间戳
     * <p>
     * 一、由于角色、权限的数据库变更造成的需要大范围用户刷新权限缓存的场景
     * 1、权限的identifier、activated变更时，更新公共标记REFRESH_PERMISSION_AFTER。
     * 2、若用户标记PERMISSION_REFRESHED >= REFRESH_ROLE_AFTER
     * 且PERMISSION_REFRESHED >= REFRESH_PERMISSION_AFTER
     * 则先查询缓存的权限数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新PERMISSION_REFRESHED。
     * <p>
     * 二、由于用户自身变更造成的自己需要刷新权限缓存的场景
     * 1、用户自身变更时，置用户标记PERMISSION_REFRESHED为0。
     * 2、若用户标记PERMISSION_REFRESHED >= REFRESH_ROLE_AFTER
     * 且PERMISSION_REFRESHED >= REFRESH_PERMISSION_AFTER
     * 则先查询缓存的权限数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新PERMISSION_REFRESHED。
     */
    @Override
    public List<String> getActivatedPermissionIdentifiers(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);
        List<String> permissions = null;

        Date t1 = (Date) redis.opsForValue().get(RoleRedisConst.REFRESH_ROLE_AFTER);        // 公共标记
        Date t2 = (Date) redis.opsForValue().get(PermissionRedisConst.REFRESH_PERMISSION_AFTER);  // 公共标记
        Date t3 = (Date) StpUtil.getSessionByLoginId(userId).get(PermissionRedisConst.PERMISSION_REFRESHED);  // 用户标记
        log.info("公共标记REFRESH_ROLE_AFTER: {}", t1 == null ? null : DateFormatter.commonFormat(t1));
        log.info("公共标记REFRESH_PERMISSION_AFTER: {}", t2 == null ? null : DateFormatter.commonFormat(t2));
        log.info("用户标记PERMISSION_REFRESHED: {}", t3 == null ? null : DateFormatter.commonFormat(t3));

        if ((t1 == null || (t3 != null && t3.compareTo(t1) >= 0)) && (t2 == null || (t3 != null && t3.compareTo(t2) >= 0))) {
            log.info("先查询Session缓存的权限数据");
            permissions = (List<String>) StpUtil.getSessionByLoginId(userId).get(PermissionRedisConst.USER_PERMISSIONS_KEY);
        }

        if (permissions == null) {
            log.info("无缓存或缓存不是最新，查询数据库，并更新缓存");
            List<Permission> activatedPermissions = permissionRepository.findActivatedByUserId(userId);
            log.info("提取标识符字符串列表");
            permissions = new ArrayList<>();
            for (Permission item : activatedPermissions) {
                permissions.add(item.getIdentifier());
            }
            log.info("更新缓存");
            StpUtil.getSessionByLoginId(userId).set(PermissionRedisConst.USER_PERMISSIONS_KEY, permissions);
            log.info("更新用户标记PERMISSION_REFRESHED");
            updatePermissionRefreshed(userId, new Date());
        }

        log.info("用户的权限标识列表: {}", permissions);
        return permissions;
    }

    @Override
    public void updateRefreshPermissionAfter(Date timestamp) {
        redis.opsForValue().set(PermissionRedisConst.REFRESH_PERMISSION_AFTER, timestamp);
        log.info("已更新REFRESH_PERMISSION_AFTER为: {}", DateFormatter.commonFormat(timestamp));
    }

    @Override
    public void updatePermissionRefreshed(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set(PermissionRedisConst.PERMISSION_REFRESHED, timestamp);
        log.info("已更新用户标记PERMISSION_REFRESHED为: {}", DateFormatter.commonFormat(timestamp));
    }
}
