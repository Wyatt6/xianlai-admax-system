package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;

import java.util.Date;

/**
 * @author Wyatt
 * @date 2024/1/31
 */
@Data
public class UserForm {
    private Long id;            // 主键
    private String username;    // 用户名
    private String password;    // 密码
    private String phone;       // 手机号码
    private String email;       // 电子邮箱
    private Boolean activated;  // 激活标志
    private Date createTime;    // 创建时间
    // ----- 额外：验证码 -----
    private String captchaKey;
    private String captcha;
}
