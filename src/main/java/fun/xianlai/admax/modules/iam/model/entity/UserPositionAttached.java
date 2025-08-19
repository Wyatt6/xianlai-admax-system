package fun.xianlai.admax.modules.iam.model.entity;

import fun.xianlai.admax.modules.iam.model.entity.pk.UserPositionPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户的兼任职务/岗位
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_user_position_attached")
@IdClass(UserPositionPK.class)
public class UserPositionAttached {
    @Id
    private Long userId;
    @Id
    private Long positionId;
}
