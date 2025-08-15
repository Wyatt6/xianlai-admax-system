package fun.xianlai.admax.module.ledger.service;

import org.springframework.data.domain.Page;
import fun.xianlai.admax.module.ledger.model.entity.Journal;
import fun.xianlai.admax.module.ledger.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Wyatt
 * @date 2023/10/7
 */
public interface JournalService {
    /**
     * 创建新账务流水
     *
     * @param userId  用户ID
     * @param journal 新账务流水
     * @return 新账务流水对象
     */
    Journal createJournal(Long userId, Journal journal);

    /**
     * 删除账务流水
     *
     * @param userId    用户ID
     * @param journalId 要删除的账务流水ID
     */
    void deleteJournal(Long userId, Long journalId);

    /**
     * 更新账务流水
     *
     * @param userId  用户ID
     * @param journal 账务流水
     * @return 新的账务流水对象
     */
    Journal updateJournal(Long userId, Journal journal);

    /**
     * 条件查询记账流水分页数据
     *
     * @param pageNum     页码
     * @param pageSize    页尺寸
     * @param userId      用户ID
     * @param stDate      起始日期（含）
     * @param edDate      结束日期（含）
     * @param type        记账类型
     * @param categoryId  记账类别
     * @param channelId   动账渠道
     * @param description 说明
     * @return 记账流水分页
     */
    Page<Journal> getJournalsByPageConditionally(int pageNum, int pageSize, Long userId, Date stDate, Date edDate, AccountType type, Long categoryId, Long channelId, String description);

    /**
     * 条件查询总收入
     *
     * @param userId      用户ID
     * @param stDate      起始日期（含）
     * @param edDate      结束日期（含）
     * @param type        记账类型
     * @param categoryId  记账类别
     * @param channelId   动账渠道
     * @param description 说明
     * @return 满足条件的流水的总收入
     */
    BigDecimal getDebitSumConditionally(Long userId, Date stDate, Date edDate, AccountType type, Long categoryId, Long channelId, String description);

    /**
     * 条件查询总支出
     *
     * @param userId      用户ID
     * @param stDate      起始日期（含）
     * @param edDate      结束日期（含）
     * @param type        记账类型
     * @param categoryId  记账类别
     * @param channelId   动账渠道
     * @param description 说明
     * @return 满足条件的流水的总支出
     */
    BigDecimal getCreditSumConditionally(Long userId, Date stDate, Date edDate, AccountType type, Long categoryId, Long channelId, String description);
}
