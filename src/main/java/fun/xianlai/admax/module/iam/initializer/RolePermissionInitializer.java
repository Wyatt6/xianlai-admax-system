package fun.xianlai.admax.module.iam.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.model.constant.PermissionInitConst;
import fun.xianlai.admax.module.iam.model.constant.RoleInitConst;
import fun.xianlai.admax.module.iam.model.entity.RolePermission;
import fun.xianlai.admax.module.iam.repository.RolePermissionRepository;

/**
 * @author WyattLau
 * @date 2024/3/21
 */
@Slf4j
@Component
@Order(4)
public class RolePermissionInitializer implements CommandLineRunner {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public void run(String... args) throws Exception {
        grantToSuperAdmin();
        grantToAdmin();
    }

    private void grantToSuperAdmin() throws Exception {
        log.info(">>>>> IAM模块：为超级管理员角色授权 >>>>>");
        log.info("--> 授权菜单权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_IAM_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_IAM_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_IAM_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_USER_MANAGE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_USER_MANAGE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_USER_MANAGE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_ROLE_MANAGE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_ROLE_MANAGE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_ROLE_MANAGE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_PERMISSION_MANAGE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_PERMISSION_MANAGE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.MENU_PERMISSION_MANAGE_ID);
        }
        log.info("--> 授权permission资源权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_QUERY_ID);
        }
        log.info("--> 授权permission资源权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.ROLE_QUERY_ID);
        }
        log.info("--> 授权user资源权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.USER_QUERY_ID);
        }
        log.info("--> 授权API权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.UPDATE_GRANTS_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.UPDATE_GRANTS_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.UPDATE_GRANTS_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.UPDATE_BINDS_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.UPDATE_BINDS_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.SUPER_ADMIN_ROLE_ID, PermissionInitConst.UPDATE_BINDS_ID);
        }
        log.info(">>>>> FINISHED: IAM模块：为超级管理员角色授权 >>>>>");
    }

    private void grantToAdmin() throws Exception {
        log.info(">>>>> IAM模块：为管理员角色授权 >>>>>");
        log.info("--> 授权菜单权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_IAM_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_IAM_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_IAM_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_USER_MANAGE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_USER_MANAGE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_USER_MANAGE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_ROLE_MANAGE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_ROLE_MANAGE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_ROLE_MANAGE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_PERMISSION_MANAGE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_PERMISSION_MANAGE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.MENU_PERMISSION_MANAGE_ID);
        }
        log.info("--> 授权permission资源权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.PERMISSION_QUERY_ID);
        }
        log.info("--> 授权role资源权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.ROLE_QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.ROLE_QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.ROLE_QUERY_ID);
        }
        log.info("--> 授权user资源权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.USER_QUERY_ID);
        }
        log.info("--> 授权API权限");
        try {
            rolePermissionRepository.save(new RolePermission(RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.UPDATE_BINDS_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.UPDATE_BINDS_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", RoleInitConst.ADMIN_ROLE_ID, PermissionInitConst.UPDATE_BINDS_ID);
        }
        log.info(">>>>> FINISHED: IAM模块：为管理员角色授权 >>>>>");
    }
}
