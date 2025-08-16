package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;
import fun.xianlai.admax.module.iam.model.entity.Permission;
import fun.xianlai.admax.module.iam.model.enums.PermissionType;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Data
public class PermissionForm {
    private Long id;            // 主键
    private String module;      // 所属模块
    private PermissionType type;      // 类型
    private String identifier;  // 标识符
    private String name;        // 名称
    private Boolean activated;  // 启用/禁用
    private Date createTime;    // 创建时间
    private String remark;      // 备注

    public Permission convert() {
        Permission result = new Permission();

        result.setId(id);
        result.setModule(module != null ? module.trim() : null);
        result.setType(type);
        result.setIdentifier(identifier != null ? identifier.trim() : null);
        result.setName(name != null ? name.trim() : null);
        result.setActivated(activated);
        result.setCreateTime(createTime);
        result.setRemark(remark != null ? remark.trim() : null);

        return result;
    }
}
