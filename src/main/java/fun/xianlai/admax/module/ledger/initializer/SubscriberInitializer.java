package fun.xianlai.admax.module.ledger.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.iam.repository.RoleRepository;
import fun.xianlai.admax.module.ledger.model.constant.SubscriberInitConst;

import java.util.Date;

/**
 * 记账本模块订阅者初始化类
 *
 * @author Wyatt
 * @date 2024/4/17
 */
@Slf4j
@Component
@Order(1000)
public class SubscriberInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>> 记账本模块：注册记账本模块订阅者角色 >>>>>");
        try {
            roleRepository.insert(
                    SubscriberInitConst.ID,
                    SubscriberInitConst.IDENTIFIER,
                    SubscriberInitConst.NAME,
                    1,
                    SubscriberInitConst.ID,
                    new Date(),
                    null
            );
            log.info("已注册: 记账本模块订阅者角色 {}", SubscriberInitConst.IDENTIFIER);
        } catch (DataIntegrityViolationException e) {
            log.info("已存在: 记账本模块订阅者角色 {}", SubscriberInitConst.IDENTIFIER);
        }
        log.info(">>>>> FINISHED: 记账本模块：注册记账本模块订阅者角色 >>>>>");
    }
}
