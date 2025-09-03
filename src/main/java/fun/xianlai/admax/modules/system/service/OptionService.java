package fun.xianlai.admax.modules.system.service;

import java.util.Map;

/**
 * @author WyattLau
 */
public interface OptionService {
    /**
     * 更加载到前端的参数缓存
     */
    void updateFrontLoadOptionsCache();

    /**
     * 获取加载到前端的参数
     */
    Map<String, Map<String, String>> getFrontLoadOptions();

    /**
     * 获取加载到前端的参数的checksum
     */
    String getFrontLoadOptionsChecksum();

    /**
     * 更新加载到后端的参数缓存
     */
    void updateBackLoadOptionCache();

//    /**
//     * 更新某个参数缓存
//     */
//    void updateCertainOptionCache(String optionKey);
//
//    /**
//     * 删除某个参数缓存
//     */
//    void removeCertainOptionCache(String optionKey);
//
//    /**
//     * 添加参数
//     */
//    void addOption(Option option);
//
//    /**
//     * 删除参数
//     */
//    void removeOption(String optionKey);
//
//    /**
//     * 修改参数
//     *
//     * @param option 参数Key必须，其他要修改的属性非空
//     */
//    void updateOption(Option option);
//
//    /**
//     * 根据Key获取参数值
//     */
//    String getActiveOptionValue(String optionKey);
//
//    /**
//     * 以String类型读取参数值
//     */
//    Optional<String> readOptionValueForString(String optionKey);
//
//    /**
//     * 以Integer类型读取参数值
//     */
//    Optional<Integer> readOptionValueForInteger(String optionKey);
//
//    /**
//     * 以Long类型读取参数值
//     */
//    Optional<Long> readOptionValueForLong(String optionKey);
//
//    /**
//     * 以Boolean类型读取参数值
//     */
//    Boolean readOptionValueForBoolean(String optionKey);
}
