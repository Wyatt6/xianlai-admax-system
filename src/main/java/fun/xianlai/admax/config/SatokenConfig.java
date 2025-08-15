package fun.xianlai.admax.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Wyatt
 * @date 2024/2/4
 */
@Configuration
public class SatokenConfig implements WebMvcConfigurer {
    // Sa-Token 参数配置，参考文档：https://sa-token.cc
    // 此配置会覆盖 application.yml 中的配置
    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        config.setTokenName("token");               // token名称 (同时也是cookie名称)
        config.setTimeout(24 * 60 * 60);            // token有效期，单位s：24小时
        config.setActivityTimeout(3 * 60 * 60);     // token临时有效期 (指定时间内无操作就视为token过期) 单位s：3小时
        config.setIsConcurrent(true);               // 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
        config.setIsShare(true);                    // 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
        config.setTokenStyle("simple-uuid");        // token风格
        config.setIsLog(true);                      // 是否输出操作日志
        config.setIsPrint(false);                   // 是否在初始化配置时打印版本字符画
        config.setIsReadCookie(false);              // 是否尝试从Cookie里读取token，此值为false后，StpUtil.login(id)登录时也不会再往前端注入Cookie
        return config;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }
}
