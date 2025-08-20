package fun.xianlai.admax.modules.common.model.entity;

import fun.xianlai.admax.supports.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户参数项（用于用户自定义的设置）
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_setting_user_option", indexes = {
        @Index(columnList = "userId,optionKey", unique = true),
        @Index(columnList = "userId,name"),
        @Index(columnList = "userId,tag"),
        @Index(columnList = "userId,sortId")
})
public class UserOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String optionKey;
    @Column(nullable = false, length = 1024)
    private String optionValue;
    @Column(nullable = false)
    private Boolean active = false;
    @Column(nullable = false)
    private String name;
    @Column(length = 1024)
    private String description;
    @Column
    private String tag;             // 配置项标签（用于打标分类）
    @Column(nullable = false)
    private Long sortId = 0L;
}
