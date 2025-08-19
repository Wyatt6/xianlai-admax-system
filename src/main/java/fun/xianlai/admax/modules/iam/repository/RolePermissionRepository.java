package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.RolePermission;
import fun.xianlai.admax.modules.iam.model.entity.pk.RolePermissionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionPK> {
}
