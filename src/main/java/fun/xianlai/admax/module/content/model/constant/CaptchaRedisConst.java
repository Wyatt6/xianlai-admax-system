package fun.xianlai.admax.module.content.model.constant;

/**
 * @author Wyatt
 * @date 2024/4/2
 */
public class CaptchaRedisConst {
    public static final String KEY_PREFIX = "captcha:"; // 验证码缓存KEY前缀
    public static final int EXP_60_SECS = 60;           // 验证码缓存过期时长：60秒
}
