package fun.xianlai.admax.module.iam.model.entity;

import fun.xianlai.admax.module.iam.model.entity.pk.UserDepartmentPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_user_department_attached")
@IdClass(UserDepartmentPK.class)
public class UserDepartmentAttached {
    @Id
    private Long userId;
    @Id
    private Long departmentId;
}
