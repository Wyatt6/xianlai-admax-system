package fun.xianlai.admax.modules.iam.model.entity;

import fun.xianlai.admax.supports.MapAndJsonConverter;
import fun.xianlai.admax.supports.PrimaryKeyGenerator;
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

import java.util.Date;
import java.util.Map;

/**
 * 职务/岗位
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_position", indexes = {
        @Index(columnList = "departmentId"),
        @Index(columnList = "name"),
        @Index(columnList = "departmentId,name", unique = true),
        @Index(columnList = "createTime"),
        @Index(columnList = "sortId")
})
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;
    @Column(nullable = false)
    private Long departmentId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Boolean active = false;
    @Column
    private String description;
    @Column
    private Date createTime;    // 职位/岗位设立时间（不是数据记录生成时间）
    @Column(nullable = false)
    private Long sortId = 0L;
    @Convert(converter = MapAndJsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, String> more;
}
