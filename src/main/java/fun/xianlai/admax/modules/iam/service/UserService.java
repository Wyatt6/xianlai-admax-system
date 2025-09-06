package fun.xianlai.admax.modules.iam.service;

import fun.xianlai.admax.modules.iam.model.entity.User;

/**
 * @author WyattLau
 */
public interface UserService {
    boolean checkUsernameFormat(String username);

    boolean checkPasswordFormat(String password);

    /**
     * 创建新用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 新用户对象
     */
    User createUser(String username, String password);
}
