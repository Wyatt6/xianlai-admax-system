package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.SystemOptionService;
import fun.xianlai.admax.supports.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/system/system-option")
public class SystemOptionController {
    @Autowired
    private SystemOptionService soService;

    // TODO 添加访问权限
    @ControllerLog("更新全量系统参数缓存")
    @GetMapping("/updateSystemOptionsCache")
    public RetResult updateSystemOptionsCache() {
        soService.updateSystemOptionsCache();
        return new RetResult().success();
    }

    // TODO 添加访问权限
    @ControllerLog("更新某个系统参数缓存")
    @GetMapping("/updateCertainSystemOptionCache")
    public RetResult updateCertainSystemOptionCache(@RequestParam("key") String optionKey) {
        soService.updateCertainSystemOptionCache(optionKey);
        return new RetResult().success();
    }
}
