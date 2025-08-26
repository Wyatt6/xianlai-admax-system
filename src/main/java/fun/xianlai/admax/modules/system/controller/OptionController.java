package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.OptionService;
import fun.xianlai.admax.supports.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/system/option")
public class OptionController {
    @Autowired
    private OptionService optionService;

    // TODO 添加访问权限
    @ControllerLog("更新允许前端加载的系统参数缓存")
    @GetMapping("/updateFrontLoadOptionsCache")
    public RetResult updateFrontLoadOptionsCache() {
        optionService.updateFrontLoadOptionsCache();
        return new RetResult().success();
    }

    // TODO 添加访问权限
    @ControllerLog("更新某个系统参数缓存")
    @GetMapping("/updateCertainOptionCache")
    public RetResult updateCertainOptionCache(@RequestParam("key") String optionKey) {
        optionService.updateCertainOptionCache(optionKey);
        return new RetResult().success();
    }

    // TODO 添加访问权限
    @ControllerLog("删除某个系统参数缓存")
    @GetMapping("/removeCertainOptionCache")
    public RetResult removeCertainOptionCache(@RequestParam("key") String optionKey) {
        optionService.removeCertainOptionCache(optionKey);
        return new RetResult().success();
    }

    @ControllerLog("获取允许前端访问的系统参数")
    @GetMapping("/getOptions")
    public RetResult getOptions() {
        Map<String, String> frontLoad = optionService.getFrontLoadOptions();
        // 不需要调用getFrontLoadOptionsChecksum()获取checksum
        // 在ControllerLog中已经对每个响应自动调用并封装到data.optionsChecksum
        // String frontLoadChecksum = optionService.getFrontLoadOptionsChecksum();
        return new RetResult().success()
                .addData("options", frontLoad);
                //.addData("optionsChecksum", frontLoadChecksum);
    }
}
