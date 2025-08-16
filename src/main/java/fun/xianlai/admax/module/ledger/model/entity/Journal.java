package fun.xianlai.admax.module.ledger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import fun.xianlai.admax.module.ledger.model.enums.AccountType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 记账流水
 *
 * @author WyattLau
 * @date 2023/10/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_ledger_journal", indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "acctDate"),
        @Index(columnList = "categoryId"),
        @Index(columnList = "channelId"),
})
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PrimaryKeyGenerator")
    @GenericGenerator(name = "PrimaryKeyGenerator", strategy = "fun.xianlai.admax.util.PrimaryKeyGenerator")
    private Long id;            // 主键
    @Column(nullable = false)
    private Long userId;        // 用户ID
    @Column
    private Date acctDate;      // 记账日期
    @Column
    @Enumerated
    private AccountType type;   // 收支类型
    @Column
    private BigDecimal amount = BigDecimal.valueOf(0);  // 记账金额
    @Column(nullable = false)
    private Long categoryId;    // 记账类别ID
    @Column(nullable = false)
    private Long channelId;     // 动账渠道ID
    @Column
    private String description; // 记账说明
}
