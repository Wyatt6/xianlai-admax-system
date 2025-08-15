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
import fun.xianlai.admax.module.ledger.model.constant.ChannelInitConst;
import fun.xianlai.admax.module.ledger.model.constant.SubscriberInitConst;

import java.util.Date;

/**
 * @author Wyatt
 * @date 2024/4/17
 */
@Slf4j
@Component
@Order(1002)
public class ChannelInitializer implements CommandLineRunner {
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
        log.info(">>>>> 记账本模块：加载“动账渠道”的权限数据 >>>>>");
        try {
            permissionRepository.insert(
                    ChannelInitConst.ADD_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    ChannelInitConst.ADD,
                    ChannelInitConst.ADD_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", ChannelInitConst.ADD);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", ChannelInitConst.ADD);
        }
        try {
            permissionRepository.insert(
                    ChannelInitConst.DELETE_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    ChannelInitConst.DELETE,
                    ChannelInitConst.DELETE_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", ChannelInitConst.DELETE);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", ChannelInitConst.DELETE);
        }
        try {
            permissionRepository.insert(
                    ChannelInitConst.EDIT_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    ChannelInitConst.EDIT,
                    ChannelInitConst.EDIT_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", ChannelInitConst.EDIT);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", ChannelInitConst.EDIT);
        }
        try {
            permissionRepository.insert(
                    ChannelInitConst.QUERY_ID,
                    "记账本",
                    PermissionType.resource.ordinal(),
                    ChannelInitConst.QUERY,
                    ChannelInitConst.QUERY_NAME,
                    1, new Date(), null
            );
            log.info("已加载: {}", ChannelInitConst.QUERY);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {}", ChannelInitConst.QUERY);
        }
        log.info(">>>>> FINISHED: 记账本模块：加载“动账渠道”的权限数据 >>>>>");
    }

    private void grantPermissions() throws Exception {
        log.info(">>>>> 记账本模块：为记账本模块订阅者授予“动账渠道”权限 >>>>>");
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, ChannelInitConst.ADD_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.ADD_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.ADD_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, ChannelInitConst.DELETE_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.DELETE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.DELETE_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, ChannelInitConst.EDIT_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.EDIT_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.EDIT_ID);
        }
        try {
            rolePermissionRepository.save(new RolePermission(SubscriberInitConst.ID, ChannelInitConst.QUERY_ID));
            log.info("已完成: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.QUERY_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (ROLE, PERM) = ({}, {})", SubscriberInitConst.ID, ChannelInitConst.QUERY_ID);
        }
        log.info(">>>>> FINISHED: 记账本模块：为记账本模块订阅者授予“动账渠道”权限 >>>>>");
    }
}
