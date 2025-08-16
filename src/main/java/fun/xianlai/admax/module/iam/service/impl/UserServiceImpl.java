package fun.xianlai.admax.module.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.content.service.ProfileService;
import fun.xianlai.admax.module.iam.model.constant.RoleInitConst;
import fun.xianlai.admax.module.iam.model.entity.User;
import fun.xianlai.admax.module.iam.model.entity.UserRole;
import fun.xianlai.admax.module.iam.repository.UserRepository;
import fun.xianlai.admax.module.iam.repository.UserRoleRepository;
import fun.xianlai.admax.module.iam.service.PermissionService;
import fun.xianlai.admax.module.iam.service.RoleService;
import fun.xianlai.admax.module.iam.service.UserService;
import fun.xianlai.admax.util.PasswordUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final String USERNAME_REGEXP = "^[a-zA-Z_0-9]{3,16}$";
    private static final String PASSWORD_REGEXP = "^[a-zA-Z_0-9.~!@#$%^&*?]{6,16}$";
    private static final String PHONE_REGEXP = "^[1-9][0-9]{10}$";
    private static final String EMAIL_REGEXP = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"; // ASP.NET 2.0

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ProfileService profileService;

    @Override
    public boolean checkUsernameFormat(String username) {
        return username.matches(USERNAME_REGEXP);
    }

    @Override
    public boolean checkPasswordFormat(String password) {
        return password.matches(PASSWORD_REGEXP);
    }

    @Override
    public boolean checkPhoneFormat(String phone) {
        return phone.matches(PHONE_REGEXP);
    }

    @Override
    public boolean checkEmailFormat(String email) {
        return email.matches(EMAIL_REGEXP);
    }

    @Override
    public User createUser(String username, String password, String phone, String email) {
        log.info("输入参数: username={} password=*, phone={}, email={}", username, phone, email);

        log.info("检查用户名、手机号码、电子邮箱是否已被注册");
        if (userRepository.findByUsername(username) != null) {
            throw new OnceException("用户已使用");
        }
        if (phone != null && userRepository.findByPhone(phone) != null) {
            throw new OnceException("手机号码已使用");
        }
        if (email != null && userRepository.findByEmail(email) != null) {
            throw new OnceException("电子邮箱已使用");
        }

        log.info("密码加密");
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encode(password, salt);

        User record = new User();
        record.setId(null);
        record.setUsername(username);
        record.setPassword(encryptedPassword);
        record.setSalt(salt);
        record.setPhone(phone);
        record.setEmail(email);
        record.setActivated(true);
        record.setCreateTime(new Date());
        User user = userRepository.save(record);
        log.info("新用户数据成功保存到数据库: id={}", user.getId());

        log.info("为新用户创建个人信息记录");
        profileService.createProfile(user.getId(), null, null, null);

        // 注意：JPA框架save持久化并不是即时写入数据库的，
        // 因此脱敏是不能在原对象上赋值为null，
        // 否则定义为非空的属性会报错。
        log.info("用户对象脱敏后返回");
        User result = new User();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setPhone(user.getPhone());
        result.setEmail(user.getEmail());
        result.setActivated(user.getActivated());
        result.setCreateTime(user.getCreateTime());
        return result;
    }

    @Override
    public User authentication(String username, String password) {
        log.info("输入参数: username={}, password=*", username);

        log.info("身份验证：用户名+密码");
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(PasswordUtil.encode(password, user.getSalt()))) {
            throw new OnceException("用户名或密码错误");
        }
        if (!user.getActivated()) {
            throw new OnceException("用户已冻结");
        }

        log.info("验证通过，用户对象脱敏后返回");
        user.setPassword(null);
        user.setSalt(null);
        return user;
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        log.info("输入参数: id={}", id);

        log.info("密码加密");
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encode(newPassword, salt);

        Optional<User> oldUser = userRepository.findById(id);
        if (oldUser.isPresent()) {
            User newUser = oldUser.get();
            newUser.setPassword(encryptedPassword);
            newUser.setSalt(salt);

            log.info("更新数据库");
            userRepository.save(newUser);
        } else {
            throw new OnceException("用户不存在");
        }
    }

    @Override
    public List<Long> bind(Long userId, List<Long> roleIds) {
        Assert.notNull(userId, "userId为空");
        Assert.notNull(roleIds, "roleIds为空");
        log.info("输入参数: userId={}, roleIds={}", userId, roleIds);

        List<Long> failList = new ArrayList<>();
        for (Long roleId : roleIds) {
            try {
                if (roleId.equals(RoleInitConst.SUPER_ADMIN_ROLE_ID)) {
                    throw new OnceException("权限不足，无法为用户绑定超级管理员角色");
                }
                if (roleId.equals(RoleInitConst.ADMIN_ROLE_ID) && !StpUtil.getRoleList().contains(RoleInitConst.SUPER_ADMIN_ROLE)) {
                    throw new OnceException("权限不足，无法为用户绑定管理员角色");
                }

                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleRepository.save(ur);
                log.info("绑定成功: (userId={}, roleId={})", userId, roleId);
            } catch (Exception e) {
                failList.add(roleId);
            }
        }

        if (failList.size() < roleIds.size()) {
            log.info("有绑定成功，要更新REFRESH_ROLE_AFTER和REFRESH_PERMISSION_AFTER时间戳，以动态更新用户权限缓存");
            roleService.updateRefreshRoleAfter(new Date());
            permissionService.updateRefreshPermissionAfter(new Date());
        }

        log.info("绑定失败的角色ID：{}", failList);
        return failList;
    }

    @Override
    public List<Long> cancelBind(Long userId, List<Long> roleIds) {
        Assert.notNull(userId, "userId为空");
        Assert.notNull(roleIds, "roleIds为空");
        log.info("输入参数: userId={}, roleIds={}", userId, roleIds);

        List<Long> failList = new ArrayList<>();
        for (Long roleId : roleIds) {
            try {
                if (roleId.equals(RoleInitConst.SUPER_ADMIN_ROLE_ID)) {
                    throw new OnceException("权限不足，无法为用户取消超级管理员角色");
                }
                if (roleId.equals(RoleInitConst.ADMIN_ROLE_ID) && !StpUtil.getRoleList().contains(RoleInitConst.SUPER_ADMIN_ROLE)) {
                    throw new OnceException("权限不足，无法为用户取消管理员角色");
                }

                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleRepository.delete(ur);
                log.info("解除绑定成功: (userId={}, roleId={})", userId, roleId);
            } catch (Exception e) {
                failList.add(roleId);
            }
        }

        if (failList.size() < roleIds.size()) {
            log.info("有解除绑定成功，要更新REFRESH_ROLE_AFTER和REFRESH_PERMISSION_AFTER时间戳，以动态更新用户权限缓存");
            roleService.updateRefreshRoleAfter(new Date());
            permissionService.updateRefreshPermissionAfter(new Date());
        }

        log.info("解除绑定失败的角色ID：{}", failList);
        return failList;
    }

    @Override
    public void setActivated(Long userId, Boolean activated) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(activated, "要设置的activated值为空");
        log.info("输入参数: userId={}, activated={}", userId, activated);

        log.info("查询是否存在该用户");
        Optional<User> oldUser = userRepository.findById(userId);
        if (oldUser.isPresent()) {
            User newUser = oldUser.get();
            newUser.setActivated(activated);

            log.info("更新数据库");
            userRepository.save(newUser);
        } else {
            throw new OnceException("用户不存在");
        }
    }

    @Override
    public Page<User> getUsersByPageDataMask(int pageNum, int pageSize) {
        log.info("输入参数: pageNum={}, pageSize={}", pageNum, pageSize);

        Sort sort = Sort.by(Sort.Order.asc("createTime"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<User> userPage = userRepository.findAll(pageable);
        log.info("完成查询，数据脱敏");
        for (int i = 0; i < userPage.getContent().size(); i++) {
            userPage.getContent().get(i).setPassword(null);
            userPage.getContent().get(i).setSalt(null);
        }
        log.info("查询结果: {}", userPage);

        return userPage;
    }

    @Override
    public Page<User> getUsersByPageDataMaskConditionally(
            int pageNum,
            int pageSize,
            String username,
            String phone,
            String email,
            Boolean activated,
            Date stTime,
            Date edTime,
            String role) {
        log.info("输入参数: pageNum={}, pageSize={}, username={}, phone={}, email={}, activated={}, stTime={}, edTime={}, role={}",
                pageNum, pageSize, username, phone, email, activated, stTime, edTime, role);

        Sort sort = Sort.by(Sort.Order.asc("createTime"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<User> userPage = userRepository.findConditionally(
                username,
                phone,
                email,
                activated == null ? null : (activated ? 1 : 0),
                stTime,
                edTime,
                role,
                pageable);
        log.info("完成查询，数据脱敏");
        for (int i = 0; i < userPage.getContent().size(); i++) {
            userPage.getContent().get(i).setPassword(null);
            userPage.getContent().get(i).setSalt(null);
        }
        log.info("查询结果: {}", userPage);

        return userPage;
    }
}
