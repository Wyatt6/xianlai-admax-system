package fun.xianlai.admax.module.iam.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.model.constant.RoleInitConst;
import fun.xianlai.admax.module.iam.repository.RoleRepository;

import java.util.Date;

/**
 * @author Wyatt
 * @date 2024/3/21
 */
@Slf4j
@Component
@Order(2)
public class RoleInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>> IAM模块：加载初始角色数据 >>>>>");
        try {
            roleRepository.insert(
                    RoleInitConst.SUPER_ADMIN_ROLE_ID,
                    RoleInitConst.SUPER_ADMIN_ROLE,
                    RoleInitConst.SUPER_ADMIN_ROLE_NAME,
                    1,
                    RoleInitConst.SUPER_ADMIN_ROLE_ID, new Date(), null
            );
            log.info("已加载: {} {}", RoleInitConst.SUPER_ADMIN_ROLE, RoleInitConst.SUPER_ADMIN_ROLE_NAME);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {} {}", RoleInitConst.SUPER_ADMIN_ROLE, RoleInitConst.SUPER_ADMIN_ROLE_NAME);
        }
        try {
            roleRepository.insert(
                    RoleInitConst.ADMIN_ROLE_ID,
                    RoleInitConst.ADMIN_ROLE,
                    RoleInitConst.ADMIN_ROLE_NAME,
                    1,
                    RoleInitConst.ADMIN_ROLE_ID, new Date(), null
            );
            log.info("已加载: {} {}", RoleInitConst.ADMIN_ROLE, RoleInitConst.ADMIN_ROLE_NAME);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: {} {}", RoleInitConst.ADMIN_ROLE, RoleInitConst.ADMIN_ROLE_NAME);
        }
        log.info(">>>>> FINISHED: IAM模块：加载初始角色数据 >>>>>");
    }
}
