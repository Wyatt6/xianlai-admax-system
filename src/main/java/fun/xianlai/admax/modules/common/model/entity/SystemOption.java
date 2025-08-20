package fun.xianlai.admax.modules.common.model.entity;

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
 * 系统参数项
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_system_option", indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "tag"),
        @Index(columnList = "sortId")
})
public class SystemOption {
    @Id
    private String optionKey;

    @Column(columnDefinition = "varchar(1024) not null")
    private String optionValue;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean builtIn;            // 内置参数（即Admax系统自动生成的满足运行所必须的参数，不含开发者二次开发时定义的系统参数）

    @Column(columnDefinition = "bit not null default 1")
    private Boolean editable;           // 是否允许修改

    @Column
    private String tag;                 // 配置项标签（用于打标分类）

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
