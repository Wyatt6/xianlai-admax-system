package fun.xianlai.admax.module.iam.repository;

import fun.xianlai.admax.module.iam.model.entity.UserPositionAttached;
import fun.xianlai.admax.module.iam.model.entity.pk.UserPositionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
@Repository
public interface UserPositionRepository extends JpaRepository<UserPositionAttached, UserPositionPK> {
}
