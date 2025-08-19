package fun.xianlai.admax.modules.iam.model.entity.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class UserPositionPK implements Serializable {
    private Long userId;
    private Long positionId;
}
