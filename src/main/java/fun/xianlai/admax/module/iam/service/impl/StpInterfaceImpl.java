package fun.xianlai.admax.module.iam.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.service.PermissionService;
import fun.xianlai.admax.module.iam.service.RoleService;

import java.util.List;

/**
 * @author Wyatt
 * @date 2024/2/4
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return roleService.getActivatedRoleIdentifiers(Long.valueOf(String.valueOf(loginId)));
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return permissionService.getActivatedPermissionIdentifiers(Long.valueOf(String.valueOf(loginId)));
    }
}
