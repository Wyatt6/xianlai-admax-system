package fun.xianlai.admax.module.iam.model.entity;

import fun.xianlai.admax.util.PrimaryKeyGenerator;
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
 * RBAC-角色
 *
 * @author Wyatt6
 * @date 2025/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_role", indexes = {
        @Index(columnList = "identifier", unique = true),
        @Index(columnList = "name")
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;
    @Column(nullable = false)
    private String identifier;
    @Column
    private String name;
    @Column(nullable = false)
    private Boolean activated = false;
    @Column
    private String description;
    @Column(nullable = false)
    private Long sortId = 0L;
}
