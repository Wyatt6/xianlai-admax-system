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
 * 记账类别
 *
 * @author Wyatt
 * @date 2023/10/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_ledger_category", indexes = {
        @Index(columnList = "userId")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PrimaryKeyGenerator")
    @GenericGenerator(name = "PrimaryKeyGenerator", strategy = "fun.xianlai.admax.util.PrimaryKeyGenerator")
    private Long id;            // 主键
    @Column(nullable = false)
    private Long userId;        // 用户ID
    @Column
    private String name;        // 类别名称
    @Column
    private Long parentId;      // 父类别ID
    @Column(nullable = false)
    private Boolean activated = false;  // 激活标志
    @Column
    private Long sortId;        // 排序ID
}
