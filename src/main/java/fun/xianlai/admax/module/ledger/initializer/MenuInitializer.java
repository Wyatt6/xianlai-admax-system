package fun.xianlai.admax.module.ledger.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.model.entity.RolePermission;
import fun.xianlai.admax.module.iam.model.enums.PermissionType;
import fun.xianlai.admax.module.iam.repository.PermissionRepository;
import fun.xianlai.admax.module.iam.repository.RolePermissionRepository;
import fun.xianlai.admax.module.ledger.model.constant.MenuInitConst;
import fun.xianlai.admax.module.ledger.model.constant.SubscriberInitConst;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/4/17
 */
@Slf4j
@Component
@Order(1001)
public class MenuInitializer implements CommandLineRunner {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPermissions();
        grantPermissions();
    }

    private void loadPermissions() throws Exception {
        log.info(">>>>> 记账本模块：加载模块的菜单权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    MenuInitConst.MODULE_ID,
                    "记账本",
                    PermissionType.view.ordinal(),
                    MenuInitConst.MODULE,
                    MenuInitConst.MODULE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", MenuInitConst.MODULE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", MenuInitConst.MODULE);
        }
        try {
            permissionRepository.insert(
                    MenuInitConst.DASHBOARD_ID,
                    "记账本",
                    PermissionType.view.ordinal(),
                    MenuInitConst.DASHBOARD,
                    MenuInitConst.DASHBOARD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", MenuInitConst.DASHBOARD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", MenuInitConst.DASHBOARD);
        }
        try {
            permissionRepository.insert(
                    MenuInitConst.DETAIL_ID,
                    "记账本",
                    PermissionType.view.ordinal(),
                    MenuInitConst.DETAIL,
                    MenuInitConst.DETAIL_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", MenuInitConst.DETAIL);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", MenuInitConst.DETAIL);
        }
        try {
            permissionRepository.insert(
                    MenuInitConst.BUDGET_ID,
                    "记账本",
                    PermissionType.view.ordinal(),
                    MenuInitConst.BUDGET,
                    MenuInitConst.BUDGET_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", MenuInitConst.BUDGET);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", MenuInitConst.BUDGET);
        }
        try {
            permissionRepository.insert(
                    MenuInitConst.ASSETS_ID,
                    "记账本",
                    PermissionType.view.ordinal(),
                    MenuInitConst.ASSETS,
                    MenuInitConst.ASSETS_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", MenuInitConst.ASSETS);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", MenuInitConst.ASSETS);
        }
        try {
            permissionRepository.insert(
                    MenuInitConst.SETTING_ID,
                    "记账本",
                    PermissionType.view.ordinal(),
                    MenuInitConst.SETTING,
                    MenuInitConst.SETTING_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", MenuInitConst.SETTING);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", MenuInitConst.SETTING);
        }
        log.info(">>>>> FINISH: 记账本模块：加载模块的菜单权限数据 >>>>>");
    }

    private void grantPermissions() {
        log.info(">>>>> 记账本模块：菜单权限授权给记账本模块订阅者 >>>>>");
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, MenuInitConst.MODULE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.MODULE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.MODULE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, MenuInitConst.DASHBOARD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.DASHBOARD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.DASHBOARD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, MenuInitConst.DETAIL_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.DETAIL_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.DETAIL_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, MenuInitConst.BUDGET_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.BUDGET_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.BUDGET_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, MenuInitConst.ASSETS_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.ASSETS_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.ASSETS_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, MenuInitConst.SETTING_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.SETTING_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, MenuInitConst.SETTING_ID);
        }
        log.info(">>>>> FINISH: 记账本模块：菜单权限授权给记账本模块订阅者 >>>>>");
    }
}
