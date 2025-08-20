package fun.xianlai.admax.modules.common.repository;

import fun.xianlai.admax.modules.common.model.entity.SystemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface SystemOptionRepository extends JpaRepository<SystemOption, String> {
}
