package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.UserPositionAttached;
import fun.xianlai.admax.modules.iam.model.entity.pk.UserPositionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface UserPositionRepository extends JpaRepository<UserPositionAttached, UserPositionPK> {
}
