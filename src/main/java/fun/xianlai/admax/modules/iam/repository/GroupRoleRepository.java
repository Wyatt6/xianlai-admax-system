package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.GroupRole;
import fun.xianlai.admax.modules.iam.model.entity.pk.GroupRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, GroupRolePK> {
}
