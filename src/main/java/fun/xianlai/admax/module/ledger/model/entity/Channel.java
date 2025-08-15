package fun.xianlai.admax.module.ledger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 动账渠道
 *
 * @author Wyatt
 * @date 2023/10/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_ledger_channel", indexes = {
        @Index(columnList = "userId")
})
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PrimaryKeyGenerator")
    @GenericGenerator(name = "PrimaryKeyGenerator", strategy = "fun.xianlai.admax.util.PrimaryKeyGenerator")
    private Long id;            // 主键
    @Column(nullable = false)
    private Long userId;        // 用户ID
    @Column
    private String name;        // 渠道名称
    @Column(nullable = false)
    private Boolean activated;  // 激活标志
    @Column
    private Long sortId;        // 排序ID
}
