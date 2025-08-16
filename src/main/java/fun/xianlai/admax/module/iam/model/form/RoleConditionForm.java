package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/3/13
 */
@Data
public class RoleConditionForm {
    private int pageNum;
    private int pageSize;
    private String identifier;      // 标识符
    private String name;            // 名称
    private Boolean activated;      // 启用/禁用
    private Date stTime;            // 开始时间
    private Date edTime;            // 结束时间
    private String permission;      // 包含权限
}
