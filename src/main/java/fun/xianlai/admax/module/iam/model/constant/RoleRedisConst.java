package fun.xianlai.admax.module.iam.model.constant;

/**
 * @author Wyatt
 * @date 2024/4/3
 */
public class RoleRedisConst {
    public static final String USER_ROLES_KEY = "roles";                    // 用户缓存角色数据的key名称
    public static final String REFRESH_ROLE_AFTER = "refresh_role_after";   // 此时间后需要刷新缓存的角色数据
    public static final String ROLE_REFRESHED = "role_refreshed";           // 本用户已刷新缓存的角色数据的时间
}
