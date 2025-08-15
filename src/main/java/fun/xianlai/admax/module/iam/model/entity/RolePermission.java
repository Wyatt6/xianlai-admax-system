package fun.xianlai.admax.module.iam.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import fun.xianlai.admax.module.iam.model.entity.pk.RolePermissionPK;

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
@Table(name = "tb_iam_role_permission")
@IdClass(RolePermissionPK.class)
public class RolePermission {
    @Id
    private Long roleId;
    @Id
    private Long permissionId;
}
