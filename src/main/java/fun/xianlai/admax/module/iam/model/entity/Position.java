package fun.xianlai.admax.module.iam.model.entity;

import fun.xianlai.admax.util.MapAndJsonConverter;
import fun.xianlai.admax.util.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

import java.util.Map;

/**
 * 职位/岗位
 *
 * @author Wyatt6
 * @date 2025/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_position", indexes = {
        @Index(columnList = "organizationId"),
        @Index(columnList = "name"),
        @Index(columnList = "organizationId,name", unique = true)
})
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;
    @Column(nullable = false)
    private Long organizationId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Boolean active = false;
    @Column
    private String description;
    @Column(nullable = false)
    private Long sortId = 0L;
    @Convert(converter = MapAndJsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, String> more;
}
