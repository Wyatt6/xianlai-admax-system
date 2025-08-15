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
import fun.xianlai.admax.module.ledger.model.constant.JournalInitConst;
import fun.xianlai.admax.module.ledger.model.constant.SubscriberInitConst;

import java.util.Date;

/**
 * @author Wyatt
 * @date 2024/4/23
 */
@Slf4j
@Component
@Order(1004)
public class JournalInitializer implements CommandLineRunner {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPermissions();
        grantPermissions();
    }

    private void loadPermissions() {
        log.info(">>>>> 记账本模块：加载“记账流水”的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    JournalInitConst.ADD_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    JournalInitConst.ADD,
                    JournalInitConst.ADD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", JournalInitConst.ADD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", JournalInitConst.ADD);
        }
        try {
            permissionRepository.insert(
                    JournalInitConst.DELETE_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    JournalInitConst.DELETE,
                    JournalInitConst.DELETE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", JournalInitConst.DELETE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", JournalInitConst.DELETE);
        }
        try {
            permissionRepository.insert(
                    JournalInitConst.EDIT_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    JournalInitConst.EDIT,
                    JournalInitConst.EDIT_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", JournalInitConst.EDIT);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", JournalInitConst.EDIT);
        }
        try {
            permissionRepository.insert(
                    JournalInitConst.QUERY_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    JournalInitConst.QUERY,
                    JournalInitConst.QUERY_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", JournalInitConst.QUERY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", JournalInitConst.QUERY);
        }
        try {
            permissionRepository.insert(
                    JournalInitConst.GET_SUM_CONDITIONALLY_ID,
                    "记账本",
                    PermissionType.api.ordinal(),
                    JournalInitConst.GET_SUM_CONDITIONALLY,
                    "条件查询总收入和总支出",
                    1, new Date(), null
            );
            log.info("已加载: {}", JournalInitConst.GET_SUM_CONDITIONALLY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", JournalInitConst.GET_SUM_CONDITIONALLY);
        }
        log.info(">>>>> FINISHED: 记账本模块：加载“记账流水”的权限数据 >>>>>");
    }

    private void grantPermissions() {
        log.info(">>>>> 记账本模块：为记账本模块订阅者授予“记账流水”权限 >>>>>");
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, JournalInitConst.ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, JournalInitConst.DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, JournalInitConst.EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, JournalInitConst.QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.QUERY_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, JournalInitConst.GET_SUM_CONDITIONALLY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.GET_SUM_CONDITIONALLY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, JournalInitConst.GET_SUM_CONDITIONALLY_ID);
        }
        log.info(">>>>> FINISHED: 记账本模块：为记账本模块订阅者授予“记账流水”权限 >>>>>");
    }
}
