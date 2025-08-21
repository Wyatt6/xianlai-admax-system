package fun.xianlai.admax.modules.system.controller;

import fun.xianlai.admax.loggers.ControllerLog;
import fun.xianlai.admax.modules.system.service.CaptchaService;
import fun.xianlai.admax.supports.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/system/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @ControllerLog("获取验证码")
    @GetMapping("/getCaptcha")
    public RetResult getCaptcha() {
        // {captchaKey 验证码KEY, captchaImage 验证码Base64图像}
        return new RetResult().success().setData(captchaService.generateCaptcha());
    }
}
