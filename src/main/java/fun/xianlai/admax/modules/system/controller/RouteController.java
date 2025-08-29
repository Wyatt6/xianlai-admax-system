package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.RouteService;
import fun.xianlai.admax.supports.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/api/admax/system/route")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @ControllerLog("更新系统路由缓存")
    @GetMapping("/updateRoutesCache")
    public RetResult updateRoutesCache() {
        routeService.updateRoutesCache();
        return new RetResult().success();
    }
}
