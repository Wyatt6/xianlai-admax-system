package fun.xianlai.admax.modules.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.loggers.ServiceLog;
import fun.xianlai.admax.loggers.SimpleServiceLog;
import fun.xianlai.admax.modules.system.entity.SystemOption;
import fun.xianlai.admax.modules.system.repository.SystemOptionRepository;
import fun.xianlai.admax.modules.system.service.SystemOptionService;
import fun.xianlai.admax.utils.ChecksumUtil;
import fun.xianlai.admax.utils.EntityRenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @SimpleServiceLog("更新允许前端加载的系统参数缓存")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateFrontLoadSystemOptionsCache() {
        List<SystemOption> options = soRepository.findByActiveAndFrontLoad(true, true);
        Map<String, String> mapOptions = new HashMap<>();
        if (options != null) {
            for (SystemOption option : options) {
                mapOptions.put(option.getOptionKey(), option.getOptionValue());
            }
        }
        redis.opsForValue().set("systemOptionsChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(mapOptions)));
        redis.opsForValue().set("systemOptions", mapOptions);
    }

    @Override
    @SimpleServiceLog("更新某个系统参数缓存")
    public void updateCertainSystemOptionCache(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        Optional<SystemOption> option = soRepository.findByOptionKeyAndActive(optionKey, true);
        redis.delete(optionKey);
        if (option.isPresent()) {
            redis.opsForValue().set(optionKey, option.get().getOptionValue());
        }
    }

    @Override
    @SimpleServiceLog("删除某个系统参数缓存")
    public void removeCertainSystemOptionCache(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
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
            self.updateCertainSystemOptionCache(option.getOptionKey());
            if (option.getFrontLoad()) {
                self.updateFrontLoadSystemOptionsCache();
            }
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
            self.removeCertainSystemOptionCache(optionKey);
            if (option.get().getFrontLoad()) {
                self.updateFrontLoadSystemOptionsCache();
            }
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
                option.setFrontLoad(null);
            }
            SystemOption newOption = oldOption.get();
            EntityRenderUtil.renderNotNullFields(newOption, option);
            soRepository.save(newOption);
            log.info("系统参数已更新到数据库");
            self.updateCertainSystemOptionCache(newOption.getOptionKey());
            if (newOption.getFrontLoad()) {
                self.updateFrontLoadSystemOptionsCache();
            }
        } else {
            throw new SystemException("参数不存在");
        }
    }

    @Override
    @SimpleServiceLog("根据Key获取系统参数值")
    public String getActiveOptionValue(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        String value = (String) redis.opsForValue().get(optionKey);
        if (value != null) {
            return value;
        } else {
            self.updateCertainSystemOptionCache(optionKey);
            return (String) redis.opsForValue().get(optionKey);
        }
    }

    @Override
    @SimpleServiceLog("以String类型读取系统参数值")
    public Optional<String> readOptionValueForString(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        String value = self.getActiveOptionValue(optionKey);
        return value != null ? value.describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Integer类型读取系统参数值")
    public Optional<Integer> readOptionValueForInteger(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        String value = self.getActiveOptionValue(optionKey);
        return value != null ? ((Integer) Integer.parseInt(value)).describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Long类型读取系统参数值")
    public Optional<Long> readOptionValueForLong(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        String value = self.getActiveOptionValue(optionKey);
        return value != null ? ((Long) Long.parseLong(value)).describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Boolean类型读取系统参数值")
    public Boolean readOptionValueForBoolean(String optionKey) {
        Assert.hasText(optionKey, "参数Key为空");
        String value = self.getActiveOptionValue(optionKey);
        return value != null ? Boolean.parseBoolean(value) : null;
    }
}
