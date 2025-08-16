package fun.xianlai.admax.module.ledger.model.form;

import lombok.Data;
import fun.xianlai.admax.module.ledger.model.enums.AccountType;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/4/23
 */
@Data
public class JournalConditionForm {
    private int pageNum;
    private int pageSize;
    private Date stDate;            // 开始日期
    private Date edDate;            // 结束日期
    private AccountType type;       // 收支类型
    private Long categoryId;        // 记账类别
    private Long channelId;         // 动账渠道
    private String description;     // 说明
}
