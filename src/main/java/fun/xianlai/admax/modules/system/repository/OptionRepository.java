package fun.xianlai.admax.modules.system.repository;

import fun.xianlai.admax.modules.system.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, String> {
    List<Option> findByActiveAndFrontLoad(Boolean active, Boolean frontLoad);

    Optional<Option> findByOptionKeyAndActive(String optionKey, Boolean active);
}
