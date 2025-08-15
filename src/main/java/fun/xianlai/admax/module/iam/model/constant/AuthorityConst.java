package fun.xianlai.admax.module.iam.model.constant;

/**
 * 权限常量
 * 用于SaCheckRole或SaCheckPermission注解
 *
 * @author Wyatt
 * @date 2024/4/4
 */
public class AuthorityConst {
    public static final String PERMISSION_ADD = PermissionInitConst.PERMISSION_ADD;
    public static final String PERMISSION_DELETE = PermissionInitConst.PERMISSION_DELETE;
    public static final String PERMISSION_EDIT = PermissionInitConst.PERMISSION_EDIT;
    public static final String PERMISSION_QUERY = PermissionInitConst.PERMISSION_QUERY;

    public static final String ROLE_ADD = PermissionInitConst.ROLE_ADD;
    public static final String ROLE_DELETE = PermissionInitConst.ROLE_DELETE;
    public static final String ROLE_EDIT = PermissionInitConst.ROLE_EDIT;
    public static final String ROLE_QUERY = PermissionInitConst.ROLE_QUERY;

    public static final String USER_ADD = PermissionInitConst.USER_ADD;
    public static final String USER_DELETE = PermissionInitConst.USER_DELETE;
    public static final String USER_EDIT = PermissionInitConst.USER_EDIT;
    public static final String USER_QUERY = PermissionInitConst.USER_QUERY;

    public static final String UPDATE_GRANTS = PermissionInitConst.UPDATE_GRANTS;
    public static final String UPDATE_BINDS = PermissionInitConst.UPDATE_BINDS;
}
