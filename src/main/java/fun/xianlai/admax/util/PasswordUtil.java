package fun.xianlai.admax.util;

import java.util.UUID;

/**
 * @author Wyatt
 * @date 2024/2/4
 */
public class PasswordUtil {
    // 生成加密盐
    public static String generateSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(20);
    }

    // 明文加密
    public static String encode(String plaintext, String salt) {
        return new MD5Encoder().encode(plaintext, salt);
    }
}
