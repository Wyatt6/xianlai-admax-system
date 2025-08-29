package fun.xianlai.admax.modules.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.admax.loggers.SimpleServiceLog;
import fun.xianlai.admax.modules.system.model.entity.Route;
import fun.xianlai.admax.modules.system.repository.RouteRepository;
import fun.xianlai.admax.modules.system.service.RouteService;
import fun.xianlai.admax.utils.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private RouteRepository routeRepository;
    @Lazy
    @Autowired
    private RouteService self;

    @Override
    @SimpleServiceLog("更新系统路由缓存")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateRoutesCache() {
        List<Route> routes = routeRepository.findByActive(true);
        List<Map<String, Object>> routeForest = getRouteForest(routes);
        redis.opsForValue().set("routesChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(routeForest)));
        redis.opsForValue().set("routes", routeForest);
    }

    @Override
    @SimpleServiceLog("获取系统路由")
    public List<Map<String, Object>> getRoutes() {
        List<Map<String, Object>> routes = (List<Map<String, Object>>) redis.opsForValue().get("routes");
        if (routes == null) {
            self.updateRoutesCache();
            routes = (List<Map<String, Object>>) redis.opsForValue().get("routes");
        }
        return routes;
    }

    @Override
    @SimpleServiceLog("获取系统路由的checksum")
    public String getRoutesChecksum() {
        return (String) redis.opsForValue().get("routesChecksum");
    }

    private List<Map<String, Object>> getRouteForest(List<Route> routes) {
        List<Map<String, Object>> forest = new ArrayList<>();

        Map<Long, Map<String, Object>> finder = new HashMap<>();
        for (Route route : routes) {
            Map<String, Object> mapRoute = new HashMap<>();
            mapRoute.put("parentId", route.getParentId());
            mapRoute.put("name", route.getName());
            mapRoute.put("path", route.getPath());
            mapRoute.put("redirect", route.getRedirect());
            mapRoute.put("component", route.getComponent());
            Map<String, Object> meta = new HashMap<>();
            meta.put("sortId", route.getSortId());
            meta.put("login", route.getLogin());
            meta.put("permission", route.getPermission());
            meta.put("menu", route.getMenu());
            meta.put("label", route.getLabel());
            meta.put("iconName", route.getIconName());
            mapRoute.put("meta", meta);
            finder.put(route.getId(), mapRoute);
        }

        for (Route route : routes) {
            Map<String, Object> mapRoute = finder.get(route.getId());
            if ((Long) mapRoute.get("parentId") == 0L) {
                forest.add(mapRoute);
            } else {
                Map<String, Object> parentRoute = finder.get((Long) mapRoute.get("parentId"));
                if (parentRoute.get("children") == null) {
                    parentRoute.put("children", new ArrayList<Map<String, Object>>());
                }
                List<Map<String, Object>> children = (List<Map<String, Object>>) parentRoute.get("children");
                children.add(mapRoute);
            }
            mapRoute.remove("parentId");
        }

        sortRoutesListInDfs(forest);

        return forest;
    }

    private void sortRoutesListInDfs(List<Map<String, Object>> list) {
        if (list == null) return;
        for (Map<String, Object> node : list) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
            if (children != null) {
                sortRoutesListInDfs(children);
            }
        }
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Long sortId1 = (Long) ((Map<String, Object>) o1.get("meta")).get("sortId");
                Long sortId2 = (Long) ((Map<String, Object>) o2.get("meta")).get("sortId");
                if (sortId1 < sortId2) {
                    return -1;
                } else if (sortId1 > sortId2) {
                    return 1;
                }
                return 0;
            }
        });
        for (Map<String, Object> node : list) {
            ((Map<String, Object>) node.get("meta")).remove("sortId");
        }
    }
}
