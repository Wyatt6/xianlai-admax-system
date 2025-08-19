package fun.xianlai.admax.modules.iam.repository;

import fun.xianlai.admax.modules.iam.model.entity.UserDepartmentAttached;
import fun.xianlai.admax.modules.iam.model.entity.pk.UserDepartmentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartmentAttached, UserDepartmentPK> {
}
