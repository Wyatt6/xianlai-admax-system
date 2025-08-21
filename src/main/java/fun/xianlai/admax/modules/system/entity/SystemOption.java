package fun.xianlai.admax.modules.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 系统参数
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_system_system_option", indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "tag"),
        @Index(columnList = "sortId")
})
public class SystemOption {
    @Id
    private String optionKey;           // 参数键

    @Column(columnDefinition = "varchar(14000) not null")
    private String optionValue;         // 参数值

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean builtIn;            // 内置参数（不允许修改参数Key、参数名、描述）

    @Column(columnDefinition = "bit not null default 1")
    private Boolean editable;           // 是否允许修改

    @Column
    private String tag;                 // 配置项标签（用于打标分类）

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
