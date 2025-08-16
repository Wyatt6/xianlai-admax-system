package fun.xianlai.admax.module.ledger.service;

/**
 * @author WyattLau
 * @date 2024/4/30
 */
public interface LedgerService {
    /**
     * 用户订阅记账本服务
     *
     * @param userId 用户ID
     */
    void subscribe(Long userId);

    /**
     * 用户退订记账本服务
     *
     * @param userId 用户ID
     */
    void unsubscribe(Long userId);
}
