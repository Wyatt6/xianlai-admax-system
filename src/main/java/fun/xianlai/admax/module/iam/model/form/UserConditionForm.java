package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;

import java.util.Date;

/**
 * @author Wyatt
 * @date 2024/3/20
 */
@Data
public class UserConditionForm {
    private int pageNum;
    private int pageSize;
    private String username;        // 用户名
    private String phone;           // 电话号码
    private String email;           // 电子邮箱
    private Boolean activated;      // 启用/禁用
    private Date stTime;            // 开始时间
    private Date edTime;            // 结束时间
    private String role;            // 包含角色
}
