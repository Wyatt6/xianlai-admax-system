package fun.xianlai.admax.modules.system.service.impl;

import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.loggers.ServiceLog;
import fun.xianlai.admax.loggers.SimpleServiceLog;
import fun.xianlai.admax.modules.system.entity.SystemOption;
import fun.xianlai.admax.modules.system.repository.SystemOptionRepository;
import fun.xianlai.admax.modules.system.service.SystemOptionService;
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
    private SystemOptionRepository soRepository;

    @Override
    @SimpleServiceLog("更新全量系统参数缓存")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateSystemOptionsCache() {
        Sort sort = Sort.by(
                Sort.Order.asc("sortId"),
                Sort.Order.asc("optionKey")
        );
        List<SystemOption> options = soRepository.findAll(sort);
        redis.opsForValue().set("systemOptions", options);
    }

    @Override
    @SimpleServiceLog("更新某个系统参数缓存")
    public void updateCertainSystemOptionCache(String optionKey) {
        Optional<SystemOption> option = soRepository.findById(optionKey);
        redis.opsForValue().set(optionKey, option.orElse(null));
    }

    @Override
    @SimpleServiceLog("删除某个系统参数缓存")
    public void removeCertainSystemOptionCache(String optionKey) {
        redis.delete(optionKey);
    }

    @Override
    @ServiceLog("添加系统参数")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addSystemOption(SystemOption option) {
        Assert.hasText(option.getOptionKey(), "参数Key为空");
        Assert.notNull(option.getOptionValue(), "参数Value为空");
        Optional<SystemOption> exists = soRepository.findById(option.getOptionKey());
        if (exists.isPresent()) {
            throw new SystemException("参数Key已存在");
        } else {
            soRepository.save(option);
            log.info("系统参数已添加到数据库");
            self.updateSystemOptionsCache();
            self.updateCertainSystemOptionCache(option.getOptionKey());
        }
    }

    @Override
    @ServiceLog("删除系统参项")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeSystemOption(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        Optional<SystemOption> option = soRepository.findById(optionKey);
        if (option.isPresent()) {
            if (option.get().getBuiltIn()) {
                throw new SystemException("内置参数无法删除");
            }
            if (!option.get().getEditable()) {
                throw new SystemException("该参数不允许修改，无法删除");
            }
            soRepository.deleteById(optionKey);
            log.info("系统参数已从数据库删除");
            self.updateSystemOptionsCache();
            self.removeCertainSystemOptionCache(optionKey);
        } else {
            throw new SystemException("参数不存在");
        }
    }

    @Override
    @ServiceLog("修改系统参数")
    public void updateSystemOption(SystemOption option) {
        Assert.hasText(option.getOptionKey(), "参数Key为空");
        Optional<SystemOption> oldOption = soRepository.findById(option.getOptionKey());
        if (oldOption.isPresent()) {
            if (!oldOption.get().getEditable()) {
                throw new SystemException("该参数不允许修改");
            }
            if (oldOption.get().getBuiltIn()) {
                log.info("内置参数仅允许修改optionValue、sortId，其他属性不允许修改");
                option.setOptionKey(null);
                option.setActive(null);
                option.setName(null);
                option.setDescription(null);
                option.setBuiltIn(null);
                option.setEditable(null);
                option.setTag(null);
            }

            SystemOption newOption = oldOption.get();
            EntityRenderUtil.renderNotNullFields(newOption, option);
            soRepository.save(newOption);
            log.info("系统参数已更新到数据库");
            self.updateSystemOptionsCache();
            self.updateCertainSystemOptionCache(newOption.getOptionKey());
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
            self.updateSystemOptionsCache();
            return (List<SystemOption>) redis.opsForValue().get("systemOptions");
        }
    }

    @Override
    @SimpleServiceLog("根据Key获取系统参数")
    public SystemOption getSystemOption(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        SystemOption cachedOption = (SystemOption) redis.opsForValue().get(optionKey);
        if (cachedOption != null) {
            return cachedOption;
        } else {
            self.updateCertainSystemOptionCache(optionKey);
            return (SystemOption) redis.opsForValue().get(optionKey);
        }
    }

    @Override
    @SimpleServiceLog("以String类型读取系统参数值")
    public Optional<String> readOptionValueForString(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        SystemOption option = self.getSystemOption(optionKey);
        return option != null && option.getActive() ? option.getOptionValue().describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Integer类型读取系统参数值")
    public Optional<Integer> readOptionValueForInteger(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        SystemOption option = self.getSystemOption(optionKey);
        return option != null && option.getActive() ? ((Integer) Integer.parseInt(option.getOptionValue())).describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Long类型读取系统参数值")
    public Optional<Long> readOptionValueForLong(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        SystemOption option = self.getSystemOption(optionKey);
        return option != null && option.getActive() ? ((Long) Long.parseLong(option.getOptionValue())).describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Boolean类型读取系统参数值")
    public Boolean readOptionValueForBoolean(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        SystemOption option = self.getSystemOption(optionKey);
        return option != null && option.getActive() ? Boolean.parseBoolean(option.getOptionValue()) : null;
    }
}
