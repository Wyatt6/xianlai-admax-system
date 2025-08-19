package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
}
