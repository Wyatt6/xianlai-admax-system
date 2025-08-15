package fun.xianlai.admax.module.iam.model.constant;

/**
 * @author Wyatt
 * @date 2024/4/4
 */
public class PermissionInitConst {
    // ----- 身份认证管理菜单 -----
    public static final Long MENU_IAM_ID = 1L;
    public static final Long MENU_USER_MANAGE_ID = 2L;
    public static final Long MENU_ROLE_MANAGE_ID = 3L;
    public static final Long MENU_PERMISSION_MANAGE_ID = 4L;
    public static final String MENU_IAM = "menu_iam";
    public static final String MENU_USER_MANAGE = "menu_user_manage";
    public static final String MENU_ROLE_MANAGE = "menu_role_manage";
    public static final String MENU_PERMISSION_MANAGE = "menu_permission_manage";
    public static final String MENU_IAM_NAME = "身份认证管理菜单";
    public static final String MENU_USER_MANAGE_NAME = "用户管理菜单";
    public static final String MENU_ROLE_MANAGE_NAME = "角色管理菜单";
    public static final String MENU_PERMISSION_MANAGE_NAME = "权限管理菜单";

    // ----- 权限相关操作 -----
    public static final Long PERMISSION_ADD_ID = 5L;
    public static final Long PERMISSION_DELETE_ID = 6L;
    public static final Long PERMISSION_EDIT_ID = 7L;
    public static final Long PERMISSION_QUERY_ID = 8L;
    public static final String PERMISSION_ADD = "iam:permission:add";
    public static final String PERMISSION_DELETE = "iam:permission:delete";
    public static final String PERMISSION_EDIT = "iam:permission:edit";
    public static final String PERMISSION_QUERY = "iam:permission:query";
    public static final String PERMISSION_ADD_NAME = "添加权限对象";
    public static final String PERMISSION_DELETE_NAME = "删除权限对象";
    public static final String PERMISSION_EDIT_NAME = "修改权限对象";
    public static final String PERMISSION_QUERY_NAME = "查询权限对象";

    // ----- 角色相关操作 -----
    public static final Long ROLE_ADD_ID = 9L;
    public static final Long ROLE_DELETE_ID = 10L;
    public static final Long ROLE_EDIT_ID = 11L;
    public static final Long ROLE_QUERY_ID = 12L;
    public static final String ROLE_ADD = "iam:role:add";
    public static final String ROLE_DELETE = "iam:role:delete";
    public static final String ROLE_EDIT = "iam:role:edit";
    public static final String ROLE_QUERY = "iam:role:query";
    public static final String ROLE_ADD_NAME = "添加角色";
    public static final String ROLE_DELETE_NAME = "删除角色";
    public static final String ROLE_EDIT_NAME = "修改角色";
    public static final String ROLE_QUERY_NAME = "查询角色";

    // ----- 用户相关操作 -----
    public static final Long USER_ADD_ID = 13L;
    public static final Long USER_DELETE_ID = 14L;
    public static final Long USER_EDIT_ID = 15L;
    public static final Long USER_QUERY_ID = 16L;
    public static final String USER_ADD = "iam:user:add";
    public static final String USER_DELETE = "iam:user:delete";
    public static final String USER_EDIT = "iam:user:edit";
    public static final String USER_QUERY = "iam:user:query";
    public static final String USER_ADD_NAME = "添加用户";
    public static final String USER_DELETE_NAME = "删除用户";
    public static final String USER_EDIT_NAME = "修改用户";
    public static final String USER_QUERY_NAME = "查询用户";

    // ----- API权限 -----
    public static final Long UPDATE_GRANTS_ID = 17L;
    public static final Long UPDATE_BINDS_ID = 18L;
    public static final String UPDATE_GRANTS = "updateGrants";
    public static final String UPDATE_BINDS = "updateBinds";
    public static final String UPDATE_GRANTS_NAME = "变更角色授权";
    public static final String UPDATE_BINDS_NAME = "变更用户绑定的角色";
}
