package fun.xianlai.admax.module.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fun.xianlai.admax.module.iam.model.entity.UserRole;
import fun.xianlai.admax.module.iam.model.entity.pk.UserRolePK;

/**
 * @author Wyatt
 * @date 2024/3/15
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
    void deleteByRoleId(Long roleId);
}
