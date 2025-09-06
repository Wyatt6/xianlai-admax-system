package fun.xianlai.admax.modules.iam.service.impl;

import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.loggers.ServiceLog;
import fun.xianlai.admax.loggers.SimpleServiceLog;
import fun.xianlai.admax.modules.iam.model.entity.User;
import fun.xianlai.admax.modules.iam.repository.UserRepository;
import fun.xianlai.admax.modules.iam.service.UserService;
import fun.xianlai.admax.modules.system.service.OptionService;
import fun.xianlai.admax.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserRepository userRepository;

    @Override
    @SimpleServiceLog("检查用户名格式服务")
    public boolean checkUsernameFormat(String username) {
        String USERNAME_REGEXP = optionService.readOptionValueForString("user.username.regexp").orElse("^[a-zA-Z][a-zA-Z_0-9]{4,19}$");
        return username.matches(USERNAME_REGEXP);
    }

    @Override
    @SimpleServiceLog("检查密码格式服务")
    public boolean checkPasswordFormat(String password) {
        String PASSWORD_REGEXP = optionService.readOptionValueForString("user.password.regexp").orElse("^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$");
        return password.matches(PASSWORD_REGEXP);
    }

    @Override
    @ServiceLog("创建新用户服务")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public User createUser(String username, String password) {
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
        record.setActive(true);
        record.setRegisterTime(new Date());
        User user = userRepository.save(record);
        log.info("成功创建新用户: id=[{}]", user.getId());


        // 注意：JPA框架save持久化并不是即时写入数据库的，
        // 因此脱敏是不能在原对象上赋值为null，
        // 否则定义为非空的属性会报错。
        log.info("新用户数据脱敏后返回");
        User result = new User();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setRegisterTime(user.getRegisterTime());
        return result;
    }
}
