package fun.xianlai.admax.module.iam.model.constant;

/**
 * @author WyattLau
 * @date 2024/4/3
 */
public class PermissionRedisConst {
    public static final String USER_PERMISSIONS_KEY = "permissions";                    // 用户缓存权限数据的key名称
    public static final String REFRESH_PERMISSION_AFTER = "refresh_permission_after";   // 此时间后需要刷新缓存的权限数据
    public static final String PERMISSION_REFRESHED = "permission_refreshed";           // 本用户已刷新缓存的角色权限的时间
}
