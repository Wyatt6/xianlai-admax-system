package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.ApiService;
import fun.xianlai.admax.supports.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/system/api")
public class ApiController {
    @Autowired
    private ApiService apiService;

    // TODO 添加访问权限
    @ControllerLog("更新接口缓存")
    @GetMapping("/updateApisCache")
    public RetResult updateApisCache() {
        apiService.updateApisCache();
        return new RetResult().success();
    }

    @ControllerLog("获取接口")
    @GetMapping("/getApis")
    public RetResult getApis() {
        List<Map<String, Object>> apis = apiService.getApis();
        // 不需要调用getApisChecksum()获取checksum，在ControllerLog中已经对每个响应自动调用并封装到data.apisChecksum
        return new RetResult().success().addData("apis", apis);
    }
}
