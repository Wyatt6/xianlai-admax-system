package fun.xianlai.admax.modules.system.service;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface RouteService {
    /**
     * 更新系统路由缓存
     */
    void updateRoutesCache();

    /**
     * 获取系统路由
     */
    List<Map<String, Object>> getRoutes();

    /**
     * 获取系统路由的checksum
     */
    String getRoutesChecksum();
}
