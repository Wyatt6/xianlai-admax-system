package fun.xianlai.admax.modules.iam.model.entity;

import fun.xianlai.admax.modules.iam.model.entity.pk.UserGroupPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_user_group")
@IdClass(UserGroupPK.class)
public class UserGroup {
    @Id
    private Long userId;
    @Id
    private Long groupId;
}
