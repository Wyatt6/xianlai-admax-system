package fun.xianlai.admax.module.content.service;


import fun.xianlai.admax.definition.response.Map;

/**
 * @author WyattLau
 * @date 2024/2/2
 */
public interface CaptchaService {
    /**
     * 生成验证码
     *
     * @return {captchaKey 验证码KEY, captchaImage 验证码Base64图像}
     */
    Map generateCaptcha();

    /**
     * 校验验证码
     * 若校验错误则抛出异常
     *
     * @param captchaKey 验证码KEY
     * @param captcha    输入的验证码
     */
    void verifyCaptcha(String captchaKey, String captcha);
}
