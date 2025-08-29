package fun.xianlai.admax.modules.system.repository;

import fun.xianlai.admax.modules.system.model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author WyattLau
 */
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByActive(Boolean active);
}
