package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;

/**
 * @author WyattLau
 * @date 2024/4/15
 */
@Data
public class ChangePasswordForm {
    private String username;       // 用户名
    private String oldPassword;    // 旧密码
    private String newPassword;    // 新密码
}
