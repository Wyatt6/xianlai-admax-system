package fun.xianlai.admax.modules.system.service;

import fun.xianlai.admax.modules.system.entity.Option;

import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
public interface OptionService {
    /**
     * 更新允许前端加载的系统参数缓存（仅缓存Key和Value）
     */
    void updateFrontLoadOptionsCache();

    /**
     * 获取允许前端加载的系统参数
     */
    Map<String, String> getFrontLoadOptions();

    /**
     * 获取允许前端加载的系统参数的checksum
     */
    String getFrontLoadOptionsChecksum();

    /**
     * 更新某个系统参数缓存
     */
    void updateCertainOptionCache(String optionKey);

    /**
     * 删除某个系统参数缓存
     */
    void removeCertainOptionCache(String optionKey);

    /**
     * 添加系统参数
     */
    void addOption(Option option);

    /**
     * 删除系统参数
     */
    void removeOption(String optionKey);

    /**
     * 修改系统参数
     *
     * @param option 参数Key必须，其他要修改的属性非空
     */
    void updateOption(Option option);

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
