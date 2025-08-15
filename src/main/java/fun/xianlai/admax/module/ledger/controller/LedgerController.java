package fun.xianlai.admax.module.ledger.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.ledger.service.LedgerService;

/**
 * @author Wyatt
 * @date 2024/4/28
 */
@Slf4j
@RestController
@RequestMapping("/api/ledger/basic")
public class LedgerController {
    @Autowired
    private LedgerService ledgerService;

    /**
     * 订阅记账本服务
     */
    @SaCheckLogin
    @GetMapping("/subscribe")
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Res subscribe() {
        Long userId = StpUtil.getLoginIdAsLong();
        ledgerService.subscribe(userId);
        return new Res().success();
    }

    /**
     * 取消订阅记账本服务
     */
    @SaCheckLogin
    @GetMapping("/unsubscribe")
    public Res unsubscribe() {
        Long userId = StpUtil.getLoginIdAsLong();
        ledgerService.unsubscribe(userId);
        return new Res().success();
    }
}
