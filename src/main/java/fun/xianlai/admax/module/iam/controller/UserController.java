package fun.xianlai.admax.module.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.definition.response.Map;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.content.service.CaptchaService;
import fun.xianlai.admax.module.iam.model.constant.AuthorityConst;
import fun.xianlai.admax.module.iam.model.constant.PermissionRedisConst;
import fun.xianlai.admax.module.iam.model.constant.RoleRedisConst;
import fun.xianlai.admax.module.iam.model.constant.UserInitConst;
import fun.xianlai.admax.module.iam.model.entity.User;
import fun.xianlai.admax.module.iam.model.form.BindForm;
import fun.xianlai.admax.module.iam.model.form.ChangePasswordForm;
import fun.xianlai.admax.module.iam.model.form.UserConditionForm;
import fun.xianlai.admax.module.iam.model.form.UserForm;
import fun.xianlai.admax.module.iam.service.PermissionService;
import fun.xianlai.admax.module.iam.service.RoleService;
import fun.xianlai.admax.module.iam.service.UserService;

import java.util.List;

/**
 * @author WyattLau
 * @date 2024/4/2
 */
@Slf4j
@RestController
@RequestMapping("/api/iam/user")
public class UserController {
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 注册新用户
     *
     * @param userForm 注册信息
     * @return {user 用户对象, token 令牌, tokenExpiredTime 令牌过期时间}
     */
    @PostMapping("/register")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res register(@RequestBody UserForm userForm) {
        Assert.notNull(userForm, "注册信息为空");

        Assert.hasText(userForm.getCaptchaKey(), "验证码KEY为空");
        Assert.hasText(userForm.getCaptcha(), "验证码为空");
        String captchaKey = userForm.getCaptchaKey();
        String captcha = userForm.getCaptcha();
        log.info("请求参数: captchaKey={}, captcha={}", captchaKey, captcha);
        log.info("校验验证码");
        captchaService.verifyCaptcha(captchaKey, captcha);

        Assert.hasText(userForm.getUsername(), "用户名为空");
        Assert.hasText(userForm.getPassword(), "密码为空");
        String username = userForm.getUsername();
        String password = userForm.getPassword();
        String phone = userForm.getPhone();
        String email = userForm.getEmail();
        // 手机号码和邮箱地址空串情况下设为null
        if (phone != null && phone.isEmpty()) phone = null;
        if (email != null && email.isEmpty()) email = null;
        log.info("请求参数: username={}, password=*, phone={}, email={}", username, phone, email);
        log.info("检查用户名和密码格式");
        if (!userService.checkUsernameFormat(username)) {
            throw new OnceException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new OnceException("密码格式错误");
        }
        log.info("检查手机号码、电子邮箱格式（如有）");
        if (phone != null && !userService.checkPhoneFormat(phone)) {
            throw new OnceException("手机号码格式错误");
        }
        if (email != null && !userService.checkEmailFormat(email)) {
            throw new OnceException("电子邮箱格式错误");
        }

        log.info("调用创建用户服务");
        User user = userService.createUser(username, password, phone, email);

        log.info("登录：Sa-Token框架自动生成token，并缓存到Redis");
        StpUtil.login(user.getId());
        log.info("token={}", StpUtil.getTokenInfo().getTokenValue());
        log.info("sessionId={}", StpUtil.getSession().getId());

        return new Res().success()
                .addData("user", user)
                .addData("token", StpUtil.getTokenInfo().getTokenValue())
                .addData("tokenExpiredTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000);
    }

    /**
     * 用户登录（用户名+密码）
     *
     * @param userForm 登录信息
     * @return {user 用户对象, token 令牌, tokenExpiredTime 令牌过期时间}
     */
    @PostMapping("/login")
    public Res login(@RequestBody UserForm userForm) {
        Assert.notNull(userForm, "登录信息为空");

        Assert.hasText(userForm.getCaptchaKey(), "验证码KEY为空");
        Assert.hasText(userForm.getCaptcha(), "验证码为空");
        String captchaKey = userForm.getCaptchaKey();
        String captcha = userForm.getCaptcha();
        log.info("请求参数: captchaKey={}, captcha={}", captchaKey, captcha);
        log.info("校验验证码");
        captchaService.verifyCaptcha(captchaKey, captcha);

        Assert.hasText(userForm.getUsername(), "用户名为空");
        Assert.hasText(userForm.getPassword(), "密码为空");
        String username = userForm.getUsername();
        String password = userForm.getPassword();
        log.info("请求参数: username={}, password=*", username);

        log.info("检查用户名和密码格式");
        if (!userService.checkUsernameFormat(username)) {
            throw new OnceException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new OnceException("密码格式错误");
        }

        log.info("调用身份验证服务");
        User user = userService.authentication(username, password);

        log.info("登录：Sa-Token框架自动生成token，并缓存到Redis");
        StpUtil.login(user.getId());
        log.info("token={}", StpUtil.getTokenInfo().getTokenValue());
        log.info("sessionId={}", StpUtil.getSession().getId());

        return new Res().success()
                .addData("user", user)
                .addData("token", StpUtil.getTokenInfo().getTokenValue())
                .addData("tokenExpiredTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000);
    }

    // TODO loginByEmail
    // TODO loginBySms

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public Res logout() {
        log.info("token={}", StpUtil.getTokenValue());
        log.info("loginId={}", StpUtil.getLoginIdAsLong());
        StpUtil.logout();
        return new Res().success();
    }

    /**
     * 修改密码
     *
     * @param input 新、旧密码
     */
    @SaCheckLogin
    @PostMapping("/changePassword")
    public Res changePassword(@RequestBody ChangePasswordForm input) {
        Assert.notNull(input, "输入数据为空");
        Assert.hasText(input.getUsername(), "用户名为空");
        Assert.hasText(input.getOldPassword(), "旧密码为空");
        Assert.hasText(input.getNewPassword(), "新密码为空");
        log.info("请求参数：username={}", input.getUsername());

        log.info("检查用户名和密码格式");
        if (!userService.checkUsernameFormat(input.getUsername())) {
            throw new OnceException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(input.getOldPassword())) {
            throw new OnceException("旧密码格式错误");
        }
        if (!userService.checkPasswordFormat(input.getNewPassword())) {
            throw new OnceException("新密码格式错误");
        }

        log.info("调用身份验证服务");
        User user = userService.authentication(input.getUsername(), input.getOldPassword());
        if (user != null) {
            log.info("身份验证通过，修改密码");
            userService.changePassword(user.getId(), input.getNewPassword());
        }

        return new Res().success();
    }

    /**
     * 获取用户的授权数据（角色+权限）
     *
     * @return authorizations 授权数据
     */
    @SaCheckLogin
    @GetMapping("/getAuthorizations")
    public Res getAuthorizations() {
        log.info("获取用户的授权数据（角色+权限）");

        Long userId = StpUtil.getLoginIdAsLong();
        List<String> roles = roleService.getActivatedRoleIdentifiers(userId);
        List<String> permissions = permissionService.getActivatedPermissionIdentifiers(userId);
        Map authorizations = new Map();
        authorizations.put("roles", roles);
        authorizations.put("permissions", permissions);

        return new Res().success().addData("authorizations", authorizations);
    }

    /**
     * 刷新用户的授权数据（角色+权限）
     *
     * @return authorizations 授权数据
     */
    @SaCheckLogin
    @GetMapping("/updateAuthorizations")
    public Res updateAuthorizations() {
        log.info("刷新用户的授权数据（角色+权限）");

        Long userId = StpUtil.getLoginIdAsLong();
        log.info("userId={}", userId);

        log.info("删除该用户缓存的授权数据");
        try {
            StpUtil.getSessionByLoginId(userId).delete(RoleRedisConst.USER_ROLES_KEY);
            StpUtil.getSessionByLoginId(userId).delete(PermissionRedisConst.USER_PERMISSIONS_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("重新查询该用户缓存的授权数据");
        List<String> roles = roleService.getActivatedRoleIdentifiers(userId);
        List<String> permissions = permissionService.getActivatedPermissionIdentifiers(userId);
        Map authorizations = new Map();
        authorizations.put("roles", roles);
        authorizations.put("permissions", permissions);

        return new Res().success().addData("authorizations", authorizations);
    }

    /**
     * 更新用户绑定的角色
     *
     * @param input { userId 用户ID, bind 绑定ID列表, cancel 取消绑定ID列表 }
     * @return { failBind 绑定失败ID列表, failCancel 取消绑定失败ID列表 }
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.UPDATE_BINDS)
    @PostMapping("/updateBinds")
    public Res
    updateBinds(@RequestBody BindForm input) {
        Assert.notNull(input, "绑定/取消绑定表单为空");
        log.info("请求参数: {}", input);

        List<Long> failBind = null;
        List<Long> failCancel = null;
        try {
            log.info("绑定");
            failBind = userService.bind(input.getUserId(), input.getBind());
        } catch (IllegalArgumentException e) {
            log.info("无须绑定");
        }
        try {
            log.info("解除绑定");
            failCancel = userService.cancelBind(input.getUserId(), input.getCancel());
        } catch (IllegalArgumentException e) {
            log.info("无须解除绑定");
        }

        return new Res().success()
                .addData("failBind", failBind)
                .addData("failCancel", failCancel);
    }

    /**
     * 冻结用户
     *
     * @param userId 要冻结的用户ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.USER_EDIT)
    @GetMapping("/freeze")
    public Res freeze(@RequestParam("userId") Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("请求参数：userId={}", userId);

        if (userId.equals(UserInitConst.SUPER_ADMIN_USER_ID)) {
            throw new OnceException("无法冻结超级管理员用户");
        }

        userService.setActivated(userId, false);
        return new Res().success();
    }

    /**
     * 解冻用户
     *
     * @param userId 要解冻的用户ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.USER_EDIT)
    @GetMapping("/unfreeze")
    public Res unfreeze(@RequestParam("userId") Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("请求参数：userId={}", userId);
        userService.setActivated(userId, true);
        return new Res().success();
    }

    /**
     * 查询用户分页
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, users 用户分页}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.USER_QUERY)
    @GetMapping("/getUsersByPage")
    public Res getUsersByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        log.info("请求参数：pageNum={}, pageSize={}", pageNum, pageSize);

        log.info("调用查询用户分页服务");
        Page<User> users = userService.getUsersByPageDataMask(pageNum, pageSize);

        return new Res().success()
                .addData("pageNum", users.getPageable().getPageNumber())
                .addData("pageSize", users.getPageable().getPageSize())
                .addData("totalPages", users.getTotalPages())
                .addData("totalElements", users.getTotalElements())
                .addData("users", users.getContent());
    }

    /**
     * 条件查询用户分页
     *
     * @param form 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, users 用户分页}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.USER_QUERY)
    @PostMapping("/getUsersByPageConditionally")
    public Res getUsersByPageConditionally(@RequestBody UserConditionForm form) {
        Assert.notNull(form, "查询条件为空");
        log.info("请求参数：{}", form);

        log.info("调用条件查询用户分页服务");
        Page<User> users = userService.getUsersByPageDataMaskConditionally(
                form.getPageNum(),
                form.getPageSize(),
                form.getUsername(),
                form.getPhone(),
                form.getEmail(),
                form.getActivated(),
                form.getStTime(),
                form.getEdTime(),
                form.getRole()
        );

        return new Res().success()
                .addData("pageNum", users.getPageable().getPageNumber())
                .addData("pageSize", users.getPageable().getPageSize())
                .addData("totalPages", users.getTotalPages())
                .addData("totalElements", users.getTotalElements())
                .addData("users", users.getContent());
    }
}
