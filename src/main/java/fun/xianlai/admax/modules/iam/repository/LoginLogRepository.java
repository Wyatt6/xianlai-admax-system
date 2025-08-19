package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
