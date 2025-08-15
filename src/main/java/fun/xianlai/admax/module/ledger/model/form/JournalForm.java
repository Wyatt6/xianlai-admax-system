package fun.xianlai.admax.module.ledger.model.form;

import lombok.Data;
import fun.xianlai.admax.module.ledger.model.entity.Journal;
import fun.xianlai.admax.module.ledger.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Wyatt
 * @date 2023/10/10
 */
@Data
public class JournalForm {
    private Long id;            // 主键
    private Long userId;        // 用户ID
    private Date acctDate;      // 记账日期
    private AccountType type;   // 收支类型
    private BigDecimal amount;  // 记账金额
    private Long categoryId;    // 记账类别
    private Long channelId;     // 动账渠道
    private String description; // 记账说明

    public Journal convert() {
        Journal journal = new Journal();

        journal.setId(id);
        journal.setUserId(userId);
        journal.setAcctDate(acctDate);
        journal.setType(type);
        journal.setAmount(amount);
        journal.setCategoryId(categoryId);
        journal.setChannelId(channelId);
        journal.setDescription(description != null ? description.trim() : null);

        return journal;
    }
}
