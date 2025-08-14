package fun.xianlai.admax.module.iam.controller;

import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.logger.ControllerLog;
import fun.xianlai.admax.module.common.service.CaptchaService;
import fun.xianlai.admax.module.iam.model.entity.User;
import fun.xianlai.admax.module.iam.service.UserService;
import fun.xianlai.admax.result.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/iam/user")
public class UserController {
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private UserService userService;

    @ControllerLog("注册API")
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
        userService.createUser(username, password);

        return new RetResult().success();
    }

    @ControllerLog("登录API")
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
            StpUtil.login(user.getId());
            log.info("loginId=[{}]", StpUtil.getLoginId());
            log.info("token=[{}]", StpUtil.getTokenValue());
            log.info("sessionId=[{}]", StpUtil.getSession().getId());

            // TODO 登录日志

            return new RetResult().success()
                    .addData("token", StpUtil.getTokenValue())
                    .addData("tokenExpiredTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000);
    }
}
