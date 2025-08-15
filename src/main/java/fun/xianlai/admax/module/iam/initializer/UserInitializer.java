package fun.xianlai.admax.module.iam.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.model.constant.UserInitConst;
import fun.xianlai.admax.module.iam.repository.UserRepository;
import fun.xianlai.admax.util.PasswordUtil;

import java.util.Date;

/**
 * @author Wyatt
 * @date 2024/3/21
 */
@Slf4j
@Component
@Order(3)
public class UserInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Value("${init.super-admin.username}")
    private String superAdminUsername;
    @Value("${init.super-admin.password}")
    private String superAdminPassword;
    @Value("${init.super-admin.phone:unknown}")
    private String superAdminPhone;
    @Value("${init.super-admin.email:unknown}")
    private String superAdminEmail;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>> IAM模块：加载初始用户数据 >>>>>");
        try {
            log.info("密码加密");
            String salt = PasswordUtil.generateSalt();
            String encryptedPassword = PasswordUtil.encode(superAdminPassword, salt);
            if (superAdminPhone.equals("unknown")) superAdminPhone = null;
            if (superAdminEmail.equals("unknown")) superAdminEmail = null;

            userRepository.insert(
                    UserInitConst.SUPER_ADMIN_USER_ID,
                    superAdminUsername,
                    encryptedPassword,
                    salt,
                    superAdminPhone,
                    superAdminEmail,
                    1, new Date()
            );
            log.info("已加载：超级管理员用户（{}）", superAdminUsername);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在：超级管理员用户");
        }
        log.info(">>>>> FINISHED: IAM模块：加载初始用户数据 >>>>>");
    }
}
