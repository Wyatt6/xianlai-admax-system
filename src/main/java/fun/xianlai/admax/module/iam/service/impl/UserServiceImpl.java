package fun.xianlai.admax.module.iam.service.impl;

import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.logger.ServiceLog;
import fun.xianlai.admax.logger.SimpleServiceLog;
import fun.xianlai.admax.module.iam.model.entity.User;
import fun.xianlai.admax.module.iam.repository.UserRepository;
import fun.xianlai.admax.module.iam.service.UserService;
import fun.xianlai.admax.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Wyatt6
 * @date 2025/8/14
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final String USERNAME_REGEXP = "^[a-zA-Z][a-zA-Z_0-9]{4,19}$";
    private static final String PASSWORD_REGEXP = "^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$";

    @Autowired
    private UserRepository userRepository;

    @Override
    @SimpleServiceLog("检查用户名格式服务")
    public boolean checkUsernameFormat(String username) {
        return username.matches(USERNAME_REGEXP);
    }

    @Override
    @SimpleServiceLog("检查密码格式服务")
    public boolean checkPasswordFormat(String password) {
        return password.matches(PASSWORD_REGEXP);
    }

    @Override
    @ServiceLog("创建新用户服务")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createUser(String username, String password) {
        log.info("输入参数: username=[{}]", username);

        log.info("检查用户名是否已被注册");
        if (userRepository.findByUsername(username) != null) {
            throw new SystemException("用户名已被使用");
        }

        log.info("密码加密");
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encode(password, salt);

        User record = new User();
        record.setUsername(username);
        record.setPassword(encryptedPassword);
        record.setSalt(salt);
        record.setRegisterTime(new Date());
        User user = userRepository.save(record);
        log.info("成功创建新用户: id=[{}]", user.getId());
    }
}
