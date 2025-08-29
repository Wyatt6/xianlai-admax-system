package fun.xianlai.admax.modules.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.admax.loggers.SimpleServiceLog;
import fun.xianlai.admax.modules.system.model.entity.Api;
import fun.xianlai.admax.modules.system.repository.ApiRepository;
import fun.xianlai.admax.modules.system.service.ApiService;
import fun.xianlai.admax.utils.ChecksumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private ApiService self;
    @Autowired
    private ApiRepository apiRepository;

    @Override
    @SimpleServiceLog("更新系统接口缓存")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateApisCache() {
        List<Api> apis = apiRepository.findByActive(true);
        List<Map<String, Object>> listApis = new ArrayList<>();
        if (apis != null) {
            for (Api api : apis) {
                Map<String, Object> mapApi = new HashMap<>();
                mapApi.put("callPath", api.getCallPath());
                mapApi.put("requestMethod", api.getRequestMethod());
                mapApi.put("url", api.getUrl());
                mapApi.put("description", api.getDescription());
                listApis.add(mapApi);
            }
        }
        redis.opsForValue().set("apisChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(listApis)));
        redis.opsForValue().set("apis", listApis);
    }

    @Override
    @SimpleServiceLog("获取系统接口")
    public List<Map<String, Object>> getApis() {
        List<Map<String, Object>> apis = (List<Map<String, Object>>) redis.opsForValue().get("apis");
        if (apis == null) {
            self.updateApisCache();
            apis = (List<Map<String, Object>>) redis.opsForValue().get("apis");
        }
        return apis;
    }

    @Override
    @SimpleServiceLog("获取系统接口的checksum")
    public String getApisChecksum() {
        return (String) redis.opsForValue().get("apisChecksum");
    }
}
