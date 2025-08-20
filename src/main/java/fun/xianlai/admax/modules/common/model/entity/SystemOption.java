package fun.xianlai.admax.modules.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统参数项
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_common_system_option", indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "tag"),
        @Index(columnList = "sortId")
})
public class SystemOption {
    @Id
    private String optionKey;
    @Column(nullable = false, length = 1024)
    private String optionValue;
    @Column(nullable = false)
    private Boolean active = false;
    @Column(nullable = false)
    private String name;
    @Column(length = 1024)
    private String description;
    @Column(nullable = false)
    private Boolean builtIn = false;    // 内置参数（即Admax系统自动生成的满足运行所必须的参数，不含开发者二次开发时定义的系统参数）
    @Column(nullable = false)
    private Boolean editable = true;    // 是否允许修改
    @Column
    private String tag;                 // 配置项标签（用于打标分类）
    @Column(nullable = false)
    private Long sortId = 0L;
}
