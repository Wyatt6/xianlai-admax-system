package fun.xianlai.admax.module.iam.model.enums;

/**
 * @author WyattLau
 * @date 2024/1/30
 */
public enum PermissionType {
    view,       // 前端元素，如页面、菜单、按钮等
    resource,   // 后端资源的操作，如权限对象(iam:permission:add、iam:permission:delete等)等
    api         // 后端API的访问，如getPermissionsByPage等
}
