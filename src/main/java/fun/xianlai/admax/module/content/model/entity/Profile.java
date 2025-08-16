package fun.xianlai.admax.module.content.model.entity;

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
 * @author WyattLau
 * @date 2024/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_content_profile", indexes = {
        @Index(columnList = "userId", unique = true)
})
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PrimaryKeyGenerator")
    @GenericGenerator(name = "PrimaryKeyGenerator", strategy = "fun.xianlai.admax.util.PrimaryKeyGenerator")
    private Long id;            // 主键
    @Column
    private Long userId;        // 所属用户
    @Column
    private String avatar;      // 头像URL
    @Column
    private String nickname;    // 昵称
    @Column
    private String motto;       // 座右铭
}
