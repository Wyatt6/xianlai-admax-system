package fun.xianlai.admax.module.iam.model.entity;

import fun.xianlai.admax.module.iam.model.entity.pk.GroupPermissionPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wyatt6
 * @date 2025/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_group_permission")
@IdClass(GroupPermissionPK.class)
public class GroupPermission {
    @Id
    private Long groupId;
    @Id
    private Long permissionId;
}
