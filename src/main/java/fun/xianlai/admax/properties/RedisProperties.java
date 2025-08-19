package fun.xianlai.admax.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("admax.datasource.redis")
public class RedisProperties {
    private String host;
    private int port;
    private String password;
    private int database = 0;
}
