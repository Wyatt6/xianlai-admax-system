package fun.xianlai.admax.module.iam.model.entity.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
@Data
public class UserPositionPK implements Serializable {
    private Long userId;
    private Long positionId;
}
