package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.OptionService;
import fun.xianlai.admax.supports.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/system/init")
public class InitializationController {
    @Autowired
    private OptionService optionService;

    @ControllerLog("获取初始化数据")
    @GetMapping("/getInitData")
    public RetResult getInitData() {
        Map<String, Map<String, String>> options = optionService.getFrontLoadOptions();
        return new RetResult().success().addData("options", options);
    }
}
