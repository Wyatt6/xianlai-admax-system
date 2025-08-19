package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.GroupPermission;
import fun.xianlai.admax.modules.iam.model.entity.pk.GroupPermissionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, GroupPermissionPK> {
}
