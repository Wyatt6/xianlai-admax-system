package fun.xianlai.admax.module.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fun.xianlai.admax.module.iam.model.entity.RolePermission;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    void deleteByRoleId(Long roleId);

    void deleteByPermissionId(Long permissionId);
}
