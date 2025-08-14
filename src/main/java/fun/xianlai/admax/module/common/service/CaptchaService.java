package fun.xianlai.admax.module.common.service;


import fun.xianlai.admax.result.DataMap;

/**
 * @author Wyatt6
 * @date 2025/8/14
 */
public interface CaptchaService {
    /**
     * 生成验证码
     *
     * @return {captchaKey 验证码KEY, captchaImage 验证码Base64图像}
     */
    DataMap generateCaptcha();

    /**
     * 检查验证码输入格式
     *
     * @param captcha 输入的验证码
     * @return true-格式正确 / false-格式错误
     */
    boolean checkFormat(String captcha);

    /**
     * 校验验证码
     * 若校验错误则抛出异常
     *
     * @param captchaKey 验证码KEY
     * @param captcha    输入的验证码
     */
    void verifyCaptcha(String captchaKey, String captcha);
}
