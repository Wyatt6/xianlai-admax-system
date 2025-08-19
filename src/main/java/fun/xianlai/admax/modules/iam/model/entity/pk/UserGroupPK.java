package fun.xianlai.admax.modules.iam.model.entity.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class UserGroupPK implements Serializable {
    private Long userId;
    private Long groupId;
}
