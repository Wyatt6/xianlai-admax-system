package fun.xianlai.admax.modules.system.service;

import fun.xianlai.admax.modules.system.entity.SystemOption;

import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
public interface SystemOptionService {
    /**
     * 更新允许前端加载的系统参数缓存（仅缓存Key和Value）
     */
    void updateFrontLoadSystemOptionsCache();

    /**
     * 获取允许前端加载的系统参数
     */
    Map<String, String> getFrontLoadSystemOptions();

    /**
     * 获取允许前端加载的系统参数的checksum
     */
    String getFrontLoadSystemOptionsChecksum();

    /**
     * 更新某个系统参数缓存
     */
    void updateCertainSystemOptionCache(String optionKey);

    /**
     * 删除某个系统参数缓存
     */
    void removeCertainSystemOptionCache(String optionKey);

    /**
     * 添加系统参数
     */
    void addSystemOption(SystemOption option);

    /**
     * 删除系统参数
     */
    void removeSystemOption(String optionKey);

    /**
     * 修改系统参数
     *
     * @param option 参数Key必须，其他要修改的属性非空
     */
    void updateSystemOption(SystemOption option);

    /**
     * 根据Key获取系统参数值
     */
    String getActiveOptionValue(String optionKey);

    /**
     * 以String类型读取系统参数值
     */
    Optional<String> readOptionValueForString(String optionKey);

    /**
     * 以Integer类型读取系统参数值
     */
    Optional<Integer> readOptionValueForInteger(String optionKey);

    /**
     * 以Long类型读取系统参数值
     */
    Optional<Long> readOptionValueForLong(String optionKey);

    /**
     * 以Boolean类型读取系统参数值
     */
    Boolean readOptionValueForBoolean(String optionKey);
}
