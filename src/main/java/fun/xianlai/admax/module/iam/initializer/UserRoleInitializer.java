package fun.xianlai.admax.module.iam.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.model.constant.RoleInitConst;
import fun.xianlai.admax.module.iam.model.constant.UserInitConst;
import fun.xianlai.admax.module.iam.model.entity.UserRole;
import fun.xianlai.admax.module.iam.repository.UserRoleRepository;

/**
 * @author Wyatt
 * @date 2024/3/21
 */
@Slf4j
@Component
@Order(5)
public class UserRoleInitializer implements CommandLineRunner {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>> IAM模块：为超级管理员用户绑定超级管理员角色 >>>>>");
        // 超级管理员用户绑定超级管理员角色
        try {
            userRoleRepository.save(new UserRole(UserInitConst.SUPER_ADMIN_USER_ID, RoleInitConst.SUPER_ADMIN_ROLE_ID));
            log.info("已完成: (USER, ROLE) = ({}, {})", UserInitConst.SUPER_ADMIN_USER_ID, RoleInitConst.SUPER_ADMIN_ROLE_ID);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: (USER, ROLE) = ({}, {})", UserInitConst.SUPER_ADMIN_USER_ID, RoleInitConst.SUPER_ADMIN_ROLE_ID);
        }
        log.info(">>>>> FINISHED: IAM模块：为超级管理员用户绑定超级管理员角色 >>>>>");
    }
}
