package fun.xianlai.admax.modules.common.service;

import fun.xianlai.admax.modules.common.model.entity.SystemOption;

import java.util.List;

/**
 * @author WyattLau
 */
public interface SystemOptionService {
    /**
     * 更新系统参数缓存
     */
    void updateSystemOptionCache();

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
}
