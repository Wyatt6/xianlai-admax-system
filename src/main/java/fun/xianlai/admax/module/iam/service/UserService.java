package fun.xianlai.admax.module.iam.service;

import org.springframework.data.domain.Page;
import fun.xianlai.admax.module.iam.model.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
public interface UserService {
    boolean checkUsernameFormat(String username);

    boolean checkPasswordFormat(String password);

    boolean checkPhoneFormat(String password);

    boolean checkEmailFormat(String password);

    /**
     * 创建新用户
     *
     * @param username 用户名
     * @param password 密码
     * @param phone    手机号码
     * @param email    电子邮箱
     * @return 用户对象
     */
    User createUser(String username, String password, String phone, String email);

    /**
     * 身份验证（用户名+密码）
     * 若认证成功则返回脱敏后的用户对象（密码和加密盐属性置为null）
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @return 用户对象
     */
    User authentication(String username, String password);

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     */
    void changePassword(Long id, String newPassword);

    /**
     * 绑定（绑定userId和roleIds）
     *
     * @param userId  用户ID
     * @param roleIds 该用户要绑定的角色ID
     * @return 绑定失败的角色ID列表
     */
    List<Long> bind(Long userId, List<Long> roleIds);

    /**
     * 解除绑定（取消绑定userId和roleIds）
     *
     * @param userId  用户ID
     * @param roleIds 该用户要解除绑定的角色ID列表
     * @return 解除绑定失败的角色ID列表
     */
    List<Long> cancelBind(Long userId, List<Long> roleIds);

    /**
     * 设置activated值
     *
     * @param userId    用户ID
     * @param activated 要设置的值
     */
    void setActivated(Long userId, Boolean activated);

    /**
     * 查询用户分页
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 用户分页
     */
    Page<User> getUsersByPageDataMask(int pageNum, int pageSize);

    /**
     * 条件查询用户分页
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 用户分页
     */
    Page<User> getUsersByPageDataMaskConditionally(int pageNum, int pageSize, String username, String phone, String email, Boolean activated, Date stTime, Date edTime, String role);
}
