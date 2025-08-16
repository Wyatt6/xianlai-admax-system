package fun.xianlai.admax.module.iam.model.entity;

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
import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_role", indexes = {
        @Index(columnList = "identifier", unique = true)
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PrimaryKeyGenerator")
    @GenericGenerator(name = "PrimaryKeyGenerator", strategy = "fun.xianlai.admax.util.PrimaryKeyGenerator")
    private Long id;            // 主键
    @Column(nullable = false)
    private String identifier;  // 标识符
    @Column
    private String name;        // 名称
    @Column(nullable = false)
    private Boolean activated;  // 启用/禁用
    @Column
    private Long sortId;        // 排序号
    @Column
    private Date createTime;    // 创建时间
    @Column
    private String remark;      // 备注
}
