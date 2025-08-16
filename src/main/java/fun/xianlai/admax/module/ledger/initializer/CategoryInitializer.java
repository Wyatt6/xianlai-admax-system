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
import fun.xianlai.admax.module.ledger.model.constant.CategoryInitConst;
import fun.xianlai.admax.module.ledger.model.constant.SubscriberInitConst;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/4/17
 */
@Slf4j
@Component
@Order(1003)
public class CategoryInitializer implements CommandLineRunner {
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
        log.info(">>>>> 记账本模块：加载“记账类别”的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    CategoryInitConst.ADD_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    CategoryInitConst.ADD,
                    CategoryInitConst.ADD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", CategoryInitConst.ADD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", CategoryInitConst.ADD);
        }
        try {
            permissionRepository.insert(
                    CategoryInitConst.DELETE_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    CategoryInitConst.DELETE,
                    CategoryInitConst.DELETE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", CategoryInitConst.DELETE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", CategoryInitConst.DELETE);
        }
        try {
            permissionRepository.insert(
                    CategoryInitConst.EDIT_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    CategoryInitConst.EDIT,
                    CategoryInitConst.EDIT_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", CategoryInitConst.EDIT);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", CategoryInitConst.EDIT);
        }
        try {
            permissionRepository.insert(
                    CategoryInitConst.QUERY_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    CategoryInitConst.QUERY,
                    CategoryInitConst.QUERY_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", CategoryInitConst.QUERY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", CategoryInitConst.QUERY);
        }
        log.info(">>>>> FINISHED: 记账本模块：加载“记账类别”的权限数据 >>>>>");
    }

    private void grantPermissions() throws Exception {
        log.info(">>>>> 记账本模块：为记账本模块订阅者授予“记账类别”权限 >>>>>");
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, CategoryInitConst.ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, CategoryInitConst.DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, CategoryInitConst.EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, CategoryInitConst.QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, CategoryInitConst.QUERY_ID);
        }
        log.info(">>>>> FINISHED: 记账本模块：为记账本模块订阅者授予“记账类别”权限 >>>>>");
    }
}
