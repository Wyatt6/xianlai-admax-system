package fun.xianlai.admax.modules.system.repository;

import fun.xianlai.admax.modules.system.entity.SystemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface SystemOptionRepository extends JpaRepository<SystemOption, String> {
    List<SystemOption> findByActiveAndFrontLoad(Boolean active, Boolean frontLoad);

    Optional<SystemOption> findByOptionKeyAndActive(String optionKey, Boolean active);
}
