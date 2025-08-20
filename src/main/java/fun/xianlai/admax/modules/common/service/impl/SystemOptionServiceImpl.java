package fun.xianlai.admax.modules.common.service.impl;

import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.loggers.ServiceLog;
import fun.xianlai.admax.loggers.SimpleServiceLog;
import fun.xianlai.admax.modules.common.model.entity.SystemOption;
import fun.xianlai.admax.modules.common.repository.SystemOptionRepository;
import fun.xianlai.admax.modules.common.service.SystemOptionService;
import fun.xianlai.admax.utils.EntityRenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class SystemOptionServiceImpl implements SystemOptionService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private SystemOptionService self;
    @Autowired
    private SystemOptionRepository soRepo;

    @Override
    @SimpleServiceLog("更新系统参数缓存")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateSystemOptionCache() {
        Sort sort = Sort.by(
                Sort.Order.asc("sortId"),
                Sort.Order.asc("optionKey")
        );
        List<SystemOption> options = soRepo.findAll(sort);
        redis.opsForValue().set("systemOptions", options);
    }

    @Override
    @ServiceLog("添加系统参数")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addSystemOption(SystemOption option) {
        Assert.hasText(option.getOptionKey(), "参数Key为空");
        Assert.notNull(option.getOptionValue(), "参数Value为空");
        Assert.notNull(option.getName(), "参数名为空");
        Optional<SystemOption> exists = soRepo.findById(option.getOptionKey());
        if (exists.isPresent()) {
            throw new SystemException("参数Key已存在");
        } else {
            soRepo.save(option);
            log.info("系统参数已添加到数据库");
            self.updateSystemOptionCache();
        }
    }

    @Override
    @ServiceLog("删除系统参项")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeSystemOption(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        soRepo.deleteById(optionKey);
        log.info("系统参数已从数据库删除");
        self.updateSystemOptionCache();
    }

    @Override
    @ServiceLog("修改系统参数")
    public void updateSystemOption(SystemOption option) {
        Assert.hasText(option.getOptionKey(), "参数Key为空");
        Optional<SystemOption> oldOption = soRepo.findById(option.getOptionKey());
        if (oldOption.isPresent()) {
            SystemOption newOption = oldOption.get();
            EntityRenderUtil.renderNotNullFields(newOption, option);
            soRepo.save(option);
            log.info("系统参数已更新到数据库");
            self.updateSystemOptionCache();
        } else {
            throw new SystemException("参数不存在");
        }
    }

    @Override
    @SimpleServiceLog("获取全量系统参数")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<SystemOption> getAllSystemOptions() {
        List<SystemOption> cachedOptions = (List<SystemOption>) redis.opsForValue().get("systemOptions");
        if (cachedOptions != null) {
            return cachedOptions;
        } else {
            self.updateSystemOptionCache();
            return (List<SystemOption>) redis.opsForValue().get("systemOptions");
        }
    }

    @Override
    @ServiceLog("根据Key获取系统参数")
    public SystemOption getSystemOption(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        List<SystemOption> cachedOptions = self.getAllSystemOptions();
        if (cachedOptions != null) {
            for (SystemOption option : cachedOptions) {
                if (option.getOptionKey().equals(optionKey)) {
                    return option;
                }
            }
        }
        throw new SystemException("参数不存在");
    }
}
