package fun.xianlai.admax.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author WyattLau
 * @date 2024/4/1
 */
@Slf4j
@Configuration
public class DruidDataSourceConfig {
    // 数据库名db_once
    // 使用Unicode字符集
    // 字符编码utf8
    // 不使用SSL连接
    // 允许返回数据库生成的主键
    // 时区东八区
    private static final String url = "jdbc:mysql://{0}:{1}/admax?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";

    @Value("${admax.datasource.mysql.ip}")
    private String ip;
    @Value("${admax.datasource.mysql.port}")
    private String port;
    @Value("${admax.datasource.mysql.username}")
    private String username;
    @Value("${admax.datasource.mysql.password}")
    private String password;
    @Value("${admax.datasource.mysql.public-key}")
    private String publicKey;

    @Bean
    public DataSource druidDateSource() {
        DruidDataSource ds = new DruidDataSource();
        // 基础配置
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(MessageFormat.format(url, ip, port));
        ds.setUsername(username);
        ds.setPassword(password);
        // Druid配置
        ds.setInitialSize(1);   // 初始连接数
        ds.setMaxActive(100);   // 最大活跃连接数
        ds.setMinIdle(1);       // 最小空闲连接数
        ds.setMaxWait(60000);   // 最大等待时间（ms）
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setMinEvictableIdleTimeMillis(300000);
        ds.setValidationQuery("select 'x'");
        ds.setTestWhileIdle(true);
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);
        ds.setPoolPreparedStatements(true);
        ds.setMaxOpenPreparedStatements(50);
        ds.setMaxPoolPreparedStatementPerConnectionSize(20);
        try {
            ds.setFilters("config,stat,wall,log4j");
            Properties connectProperties = new Properties();
            connectProperties.put("config.decrypt", "true");    // 这里必须使用字符串
            connectProperties.put("config.decrypt.key", publicKey);
            ds.setConnectProperties(connectProperties);
            ds.init();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return ds;
    }
}
