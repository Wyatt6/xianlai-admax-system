package fun.xianlai.admax.modules.system.service;

import fun.xianlai.admax.modules.system.entity.SystemOption;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
public interface SystemOptionService {
    /**
     * 更新全量系统参数缓存
     */
    void updateSystemOptionsCache();

    /**
     * 更新某个系统参数缓存
     *
     * @param optionKey 要更新缓存的参数Key
     */
    void updateCertainSystemOptionCache(String optionKey);

    /**
     * 删除某个系统参数缓存
     *
     * @param optionKey 要删除缓存的参数Key
     */
    void removeCertainSystemOptionCache(String optionKey);

    /**
     * 添加系统参数
     *
     * @param option 新参数
     */
    void addSystemOption(SystemOption option);

    /**
     * 删除系统参数
     *
     * @param optionKey 要删除的参数Key
     */
    void removeSystemOption(String optionKey);

    /**
     * 修改系统参数
     *
     * @param option 参数Key必须，其他要修改的属性非空
     */
    void updateSystemOption(SystemOption option);

    /**
     * 获取全量系统参数
     * 先访问缓存，缓存没有再访问数据库
     *
     * @return 全量系统参数列表
     */
    List<SystemOption> getAllSystemOptions();

    /**
     * 根据Key获取系统参数
     *
     * @param optionKey 参数Key
     * @return 目标系统参数
     */
    SystemOption getSystemOption(String optionKey);

    /**
     * 以String类型读取系统参数值
     *
     * @param optionKey 参数Key
     * @return String类型的值
     */
    Optional<String> readOptionValueForString(String optionKey);

    /**
     * 以Integer类型读取系统参数值
     *
     * @param optionKey 参数Key
     * @return Integer类型的值
     */
    Optional<Integer> readOptionValueForInteger(String optionKey);

    /**
     * 以Long类型读取系统参数值
     *
     * @param optionKey 参数Key
     * @return Long类型的值
     */
    Optional<Long> readOptionValueForLong(String optionKey);

    /**
     * 以Boolean类型读取系统参数值
     *
     * @param optionKey 参数Key
     * @return Boolean类型的值
     */
    Boolean readOptionValueForBoolean(String optionKey);
}
