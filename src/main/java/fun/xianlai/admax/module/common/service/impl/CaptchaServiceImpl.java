package fun.xianlai.admax.module.common.service.impl;

import com.google.code.kaptcha.Producer;
import fun.xianlai.admax.exception.SystemException;
import fun.xianlai.admax.logger.ServiceLog;
import fun.xianlai.admax.logger.SimpleServiceLog;
import fun.xianlai.admax.module.common.service.CaptchaService;
import fun.xianlai.admax.result.DataMap;
import fun.xianlai.admax.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * @author Wyatt6
 * @date 2025/5/15
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private static final String CAPTCHA_REGEXP = "^[A-Za-z0-9]{5}$";
    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private static final int CAPTCHA_EXP_SECS = 60;

    @Autowired
    private Producer producer;
    @Autowired
    private RedisTemplate<String, Object> redis;

    @Override
    @ServiceLog("生成验证码")
    public DataMap generateCaptcha() {
        try {
            // 生成验证码KEY、验证码文本，并缓存到Redis
            String captchaKey = UUID.randomUUID().toString().replaceAll("-", "");
            String captchaText = producer.createText();
            redis.opsForValue().set(CAPTCHA_KEY_PREFIX + captchaKey, captchaText, Duration.ofSeconds(CAPTCHA_EXP_SECS));
            // 根据文本生成图片并转成Base64格式
            BufferedImage captchaImage = producer.createImage(captchaText);
            String captchaImageBase64 = null;
            captchaImageBase64 = ImageUtil.bufferedImageToBase64(captchaImage, "jpeg");

            log.info("验证码KEY: {}", captchaKey);
            log.info("验证码: {}", captchaText);
            log.info("过期时长: {}s", CAPTCHA_EXP_SECS);

            DataMap data = new DataMap();
            data.put("captchaKey", captchaKey);
            data.put("captchaImage", captchaImageBase64);
            return data;
        } catch (IOException e) {
            throw new SystemException("验证码生成失败");
        }
    }

    @Override
    @SimpleServiceLog("检查验证码格式")
    public boolean checkFormat(String captcha) {
        return captcha.matches(CAPTCHA_REGEXP);
    }

    @Override
    @ServiceLog("校验验证码")
    public void verifyCaptcha(String captchaKey, String captcha) {
        log.info("输入参数: captchaKey=[{}], captcha=[{}]", captchaKey, captcha);

        if (!checkFormat(captcha)) {
            throw new SystemException("验证码格式错误");
        }

        Object captchaText = redis.opsForValue().get(CAPTCHA_KEY_PREFIX + captchaKey);
        if (captchaText == null) {
            throw new SystemException("验证码已失效");
        } else {
            log.info("缓存的验证码: captchaText=[{}]", captchaText);
            if (captcha.equalsIgnoreCase(String.valueOf(captchaText))) {
                log.info("通过验证，删除缓存的验证码");
                redis.delete(CAPTCHA_KEY_PREFIX + captchaKey);
            } else {
                throw new SystemException("验证码错误");
            }
        }
    }
}
