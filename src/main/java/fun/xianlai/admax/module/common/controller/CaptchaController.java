package fun.xianlai.admax.module.common.controller;

import fun.xianlai.admax.logger.ControllerLog;
import fun.xianlai.admax.module.common.service.CaptchaService;
import fun.xianlai.admax.result.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wyatt6
 * @date 2025/8/14
 */
@Slf4j
@RestController
@RequestMapping("/api/admax/common/captcha")
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
