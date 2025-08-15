package fun.xianlai.admax.module.iam.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import fun.xianlai.admax.module.iam.model.entity.pk.UserRolePK;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author Wyatt
 * @date 2024/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_user_role")
@IdClass(UserRolePK.class)
public class UserRole {
    @Id
    private Long userId;
    @Id
    private Long roleId;
}
