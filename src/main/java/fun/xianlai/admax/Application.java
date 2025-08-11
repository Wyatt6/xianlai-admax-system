package fun.xianlai.admax;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author  Wyatt6
 * @date    2025/8/11
 */
@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("-------------------- XianLai - Admax 系统已成功启动 --------------------");
    }
}
