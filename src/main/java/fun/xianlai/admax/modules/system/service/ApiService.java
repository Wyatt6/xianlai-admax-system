package fun.xianlai.admax.modules.system.service;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface ApiService {
    /**
     * 更新系统接口缓存
     */
    void updateApisCache();

    /**
     * 获取系统接口
     */
    List<Map<String, Object>> getApis();

    /**
     * 获取系统接口的checksum
     */
    String getApisChecksum();
}
