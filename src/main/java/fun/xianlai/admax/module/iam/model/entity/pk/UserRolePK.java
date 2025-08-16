package fun.xianlai.admax.module.iam.model.entity.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 * @date 2024/1/30
 */
@Data
public class UserRolePK implements Serializable {
    private Long userId;
    private Long roleId;
}
