package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.RouteService;
import fun.xianlai.admax.supports.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @ControllerLog("获取系统路由")
    @GetMapping("/getRoutes")
    public RetResult getRoutes() {
        List<Map<String, Object>> routes = routeService.getRoutes();
        // 不需要调用getRoutesChecksum()获取checksum，在ControllerLog中已经对每个响应自动调用并封装到data.routesChecksum
        return new RetResult().success().addData("routes", routes);
    }
}
