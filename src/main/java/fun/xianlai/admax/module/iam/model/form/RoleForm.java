package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;
import fun.xianlai.admax.module.iam.model.entity.Role;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/3/14
 */
@Data
public class RoleForm {
    private Long id;            // 主键
    private String identifier;  // 标识符
    private String name;        // 名称
    private Boolean activated;  // 启用/禁用
    private Long sortId;        // 排序号
    private Date createTime;    // 创建时间
    private String remark;      // 备注

    public Role convert() {
        Role result = new Role();

        result.setId(id);
        result.setIdentifier(identifier != null ? identifier.trim() : null);
        result.setName(name != null ? name.trim() : null);
        result.setActivated(activated);
        result.setSortId(sortId);
        result.setCreateTime(createTime);
        result.setRemark(remark != null ? remark.trim() : null);

        return result;
    }
}
