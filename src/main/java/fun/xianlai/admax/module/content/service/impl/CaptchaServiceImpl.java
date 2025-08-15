package fun.xianlai.admax.module.content.service.impl;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.definition.response.Map;
import fun.xianlai.admax.module.content.model.constant.CaptchaRedisConst;
import fun.xianlai.admax.module.content.service.CaptchaService;
import fun.xianlai.admax.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * @author Wyatt
 * @date 2024/2/2
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private static final String CAPTCHA_REGEXP = "^[A-Za-z0-9]{5}$";

    @Autowired
    private Producer producer;
    @Autowired
    private RedisTemplate<String, Object> redis;

    @Override
    public Map generateCaptcha() {
        try {
            // 生成验证码KEY、验证码文本，并缓存到Redis
            String captchaKey = UUID.randomUUID().toString().replaceAll("-", "");
            String captchaText = producer.createText();
            redis.opsForValue().set(CaptchaRedisConst.KEY_PREFIX + captchaKey, captchaText, Duration.ofSeconds(CaptchaRedisConst.EXP_60_SECS));
            // 根据文本生成图片并转成Base64格式
            BufferedImage captchaImage = producer.createImage(captchaText);
            String captchaImageBase64 = null;
            captchaImageBase64 = ImageUtil.bufferedImageToBase64(captchaImage, "jpeg");

            log.info("验证码KEY: {}", captchaKey);
            log.info("验证码: {}", captchaText);

            Map data = new Map();
            data.put("captchaKey", captchaKey);
            data.put("captchaImage", captchaImageBase64);
            return data;
        } catch (IOException e) {
            throw new OnceException("验证码生成失败");
        }
    }

    @Override
    public void verifyCaptcha(String captchaKey, String captcha) {
        log.info("输入参数: captchaKey={}, captcha={}", captchaKey, captcha);

        if (!captcha.matches(CAPTCHA_REGEXP)) {
            throw new OnceException("验证码格式错误");
        }

        Object captchaText = redis.opsForValue().get(CaptchaRedisConst.KEY_PREFIX + captchaKey);
        if (captchaText == null) {
            throw new OnceException("验证码已失效");
        } else {
            log.info("缓存的验证码: captchaText={}", captchaText);
            if (captcha.equalsIgnoreCase(String.valueOf(captchaText))) {
                log.info("通过验证，删除缓存的验证码");
                redis.delete(CaptchaRedisConst.KEY_PREFIX + captchaKey);
            } else {
                throw new OnceException("验证码错误");
            }
        }
    }
}
