package fun.xianlai.admax.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("admax.datasource.mysql")
public class MysqlProperties {
    private String host;
    private String port = "3306";
    private String password;
    private String publicKey;
}
