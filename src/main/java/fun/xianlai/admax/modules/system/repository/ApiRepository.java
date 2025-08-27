package fun.xianlai.admax.modules.system.repository;

import fun.xianlai.admax.modules.system.model.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {
}
