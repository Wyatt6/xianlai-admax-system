package fun.xianlai.admax.modules.iam.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.iam.model.entity.User;
import fun.xianlai.admax.modules.iam.service.UserService;
import fun.xianlai.admax.modules.system.service.CaptchaService;
import fun.xianlai.admax.modules.system.service.OptionService;
import fun.xianlai.admax.supports.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/iam/user")
public class UserController {
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private UserService userService;
    @Autowired
    private OptionService optionService;

    @ControllerLog("注册新用户API")
    @PostMapping("/register")
    public RetResult register(@RequestBody User input) {
        Assert.notNull(input, "输入数据为空");
        Assert.hasText(input.getCaptchaKey(), "验证码KEY为空");
        Assert.hasText(input.getCaptcha(), "验证码为空");
        Assert.hasText(input.getUsername(), "用户名为空");
        Assert.hasText(input.getPassword(), "密码为空");

        String captchaKey = input.getCaptchaKey().trim();
        String captcha = input.getCaptcha().trim();
        String username = input.getUsername().trim();
        String password = input.getPassword().trim();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);

        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.checkUsernameFormat(username)) {
            throw new SystemException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new SystemException("密码格式错误");
        }
        User user = userService.createUser(username, password);

        return new RetResult().success();
    }

    @ControllerLog("用户登录API")
    @PostMapping("/login")
    public RetResult login(@RequestBody User input) {
        Assert.notNull(input, "输入数据为空");
        Assert.hasText(input.getCaptchaKey(), "验证码KEY为空");
        Assert.hasText(input.getCaptcha(), "验证码为空");
        Assert.hasText(input.getUsername(), "用户名为空");
        Assert.hasText(input.getPassword(), "密码为空");

        String captchaKey = input.getCaptchaKey().trim();
        String captcha = input.getCaptcha().trim();
        String username = input.getUsername();
        String password = input.getPassword();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);

        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.checkUsernameFormat(username)) {
            throw new SystemException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new SystemException("密码格式错误");
        }
        User user = userService.authentication(username, password);
        log.info("登录：Sa-Token框架自动生成token并缓存");
        StpUtil.login(user.getId(), new SaLoginParameter()
                .setTimeout(optionService.readOptionValueForLong("token.timeout").orElse(43200L))
                .setActiveTimeout(optionService.readOptionValueForLong("token.activeTimeout").orElse(3600L)));
        log.info("loginId=[{}]", StpUtil.getLoginId());
        log.info("token=[{}]", StpUtil.getTokenValue());
        log.info("sessionId=[{}]", StpUtil.getSession().getId());

        log.info("用户数据脱敏");
        user.setPassword(null);
        user.setSalt(null);

        return new RetResult().success()
                .addData("user", user)
                .addData("token", StpUtil.getTokenValue())
                .addData("tokenExpireTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000);
    }
}
