package fun.xianlai.admax.modules.iam.model.entity.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class GroupPermissionPK implements Serializable {
    private Long groupId;
    private Long permissionId;
}
