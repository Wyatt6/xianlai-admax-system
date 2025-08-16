package fun.xianlai.admax.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;

/**
 * @author WyattLau
 * @date 2024/4/1
 */
@Configuration
public class DruidStateMonitorConfig {
    @Value("${admax.datasource.druid.monitor.username}")
    private String username;   // druid监控登录用户
    @Value("${admax.datasource.druid.monitor.password}")
    private String password;   // druid监控登录密码

    @Bean
    public ServletRegistrationBean<Servlet> druidStateViewServlet() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<Servlet>(new StatViewServlet(), "/druid/*");
        // 登录Druid监控后台的账号密码
        bean.addInitParameter("loginUsername", username);
        bean.addInitParameter("loginPassword", password);
        // 是否能够重置数据
        bean.addInitParameter("resetEnable", "true");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> webStatFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<Filter>();
        bean.setFilter(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }
}
