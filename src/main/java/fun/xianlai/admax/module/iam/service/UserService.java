package fun.xianlai.admax.module.iam.service;

import fun.xianlai.admax.module.iam.model.entity.User;

/**
 * @author Wyatt6
 * @date 2025/8/14
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
    void createUser(String username, String password);
}
