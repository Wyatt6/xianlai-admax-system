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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_user_option", indexes = {
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

    @Column(columnDefinition = "bigint not null")
    private Long userId;

    @Column(columnDefinition = "varchar(255) not null")
    private String optionKey;

    @Column(columnDefinition = "varchar(1024) not null")
    private String optionValue;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @Column(length = 1024)
    private String description;

    @Column
    private String tag;             // 配置项标签（用于打标分类）

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
