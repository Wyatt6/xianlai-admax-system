package fun.xianlai.admax.module.iam.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.model.constant.PermissionInitConst;
import fun.xianlai.admax.module.iam.model.enums.PermissionType;
import fun.xianlai.admax.module.iam.repository.PermissionRepository;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/3/21
 */
@Slf4j
@Component
@Order(1)
public class PermissionInitializer implements CommandLineRunner {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        loadMenuPermissions();
        loadPermissionPermissions();
        loadRolePermissions();
        loadUserPermissions();
        loadAPIPermissions();
    }

    private void loadMenuPermissions() throws Exception {
        log.info(">>>>> IAM模块：加载本模块菜单的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    PermissionInitConst.MENU_IAM_ID,
                    "IAM",
                    PermissionType.view.ordinal(),
                    PermissionInitConst.MENU_IAM,
                    PermissionInitConst.MENU_IAM_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.MENU_IAM);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.MENU_IAM);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.MENU_USER_MANAGE_ID,
                    "IAM",
                    PermissionType.view.ordinal(),
                    PermissionInitConst.MENU_USER_MANAGE,
                    PermissionInitConst.MENU_USER_MANAGE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.MENU_USER_MANAGE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.MENU_USER_MANAGE);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.MENU_ROLE_MANAGE_ID,
                    "IAM",
                    PermissionType.view.ordinal(),
                    PermissionInitConst.MENU_ROLE_MANAGE,
                    PermissionInitConst.MENU_ROLE_MANAGE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.MENU_ROLE_MANAGE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.MENU_ROLE_MANAGE);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.MENU_PERMISSION_MANAGE_ID,
                    "IAM",
                    PermissionType.view.ordinal(),
                    PermissionInitConst.MENU_PERMISSION_MANAGE,
                    PermissionInitConst.MENU_PERMISSION_MANAGE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.MENU_PERMISSION_MANAGE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.MENU_PERMISSION_MANAGE);
        }
        log.info(">>>>> FINISHED: IAM模块：加载本模块菜单的权限数据 >>>>>");
    }

    private void loadPermissionPermissions() throws Exception {
        log.info(">>>>> IAM模块：加载permission资源的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    PermissionInitConst.PERMISSION_ADD_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.PERMISSION_ADD,
                    PermissionInitConst.PERMISSION_ADD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.PERMISSION_ADD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.PERMISSION_ADD);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.PERMISSION_DELETE_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.PERMISSION_DELETE,
                    PermissionInitConst.PERMISSION_DELETE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.PERMISSION_DELETE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.PERMISSION_DELETE);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.PERMISSION_EDIT_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.PERMISSION_EDIT,
                    PermissionInitConst.PERMISSION_EDIT_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.PERMISSION_EDIT);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.PERMISSION_EDIT);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.PERMISSION_QUERY_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.PERMISSION_QUERY,
                    PermissionInitConst.PERMISSION_QUERY_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.PERMISSION_QUERY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.PERMISSION_QUERY);
        }
        log.info(">>>>> FINISHED: IAM模块：加载permission资源的权限数据 >>>>>");
    }

    private void loadRolePermissions() throws Exception {
        log.info(">>>>> IAM模块：加载role资源的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    PermissionInitConst.ROLE_ADD_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.ROLE_ADD,
                    PermissionInitConst.ROLE_ADD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.ROLE_ADD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.ROLE_ADD);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.ROLE_DELETE_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.ROLE_DELETE,
                    PermissionInitConst.ROLE_DELETE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.ROLE_DELETE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.ROLE_DELETE);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.ROLE_EDIT_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.ROLE_EDIT,
                    PermissionInitConst.ROLE_EDIT_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.ROLE_EDIT);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.ROLE_EDIT);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.ROLE_QUERY_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.ROLE_QUERY,
                    PermissionInitConst.ROLE_QUERY_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.ROLE_QUERY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.ROLE_QUERY);
        }
        log.info(">>>>> FINISHED: IAM模块：加载role资源的权限数据 >>>>>");
    }

    private void loadUserPermissions() throws Exception {
        log.info(">>>>> IAM模块：加载user资源的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    PermissionInitConst.USER_ADD_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.USER_ADD,
                    PermissionInitConst.USER_ADD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.USER_ADD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.USER_ADD);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.USER_DELETE_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.USER_DELETE,
                    PermissionInitConst.USER_DELETE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.USER_DELETE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.USER_DELETE);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.USER_EDIT_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.USER_EDIT,
                    PermissionInitConst.USER_EDIT_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.USER_EDIT);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.USER_EDIT);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.USER_QUERY_ID,
                    "IAM",
                    PermissionType.resource.ordinal(),
                    PermissionInitConst.USER_QUERY,
                    PermissionInitConst.USER_QUERY_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.USER_QUERY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.USER_QUERY);
        }
        log.info(">>>>> FINISHED: IAM模块：加载user资源的权限数据 >>>>>");
    }

    private void loadAPIPermissions() throws Exception {
        log.info(">>>>> IAM模块：加载API的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    PermissionInitConst.UPDATE_GRANTS_ID,
                    "IAM",
                    PermissionType.api.ordinal(),
                    PermissionInitConst.UPDATE_GRANTS,
                    PermissionInitConst.UPDATE_GRANTS_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.UPDATE_GRANTS);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.UPDATE_GRANTS);
        }
        try {
            permissionRepository.insert(
                    PermissionInitConst.UPDATE_BINDS_ID,
                    "IAM",
                    PermissionType.api.ordinal(),
                    PermissionInitConst.UPDATE_BINDS,
                    PermissionInitConst.UPDATE_BINDS_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", PermissionInitConst.UPDATE_BINDS);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", PermissionInitConst.UPDATE_BINDS);
        }
        log.info(">>>>> FINISHED: IAM模块：加载API的权限数据 >>>>>");
    }
}
