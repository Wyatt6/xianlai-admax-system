package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.UserGroup;
import fun.xianlai.admax.modules.iam.model.entity.pk.UserGroupPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupPK> {
}
