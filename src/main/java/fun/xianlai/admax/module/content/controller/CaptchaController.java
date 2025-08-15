package fun.xianlai.admax.module.content.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.content.service.CaptchaService;

/**
 * @author Wyatt
 * @date 2024/2/2
 */
@Slf4j
@RestController
@RequestMapping("/api/content/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取验证码
     *
     * @return {captchaKey 验证码KEY, captchaImage 验证码Base64图像}
     */
    @GetMapping("/getCaptcha")
    public Res getCaptcha() {
        return new Res().success().setData(captchaService.generateCaptcha());
    }
}
