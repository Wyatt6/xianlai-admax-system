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
 * @author Wyatt
 * @date 2024/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_user", indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "phone", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createTime")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PrimaryKeyGenerator")
    @GenericGenerator(name = "PrimaryKeyGenerator", strategy = "fun.xianlai.admax.util.PrimaryKeyGenerator")
    private Long id;            // 主键
    @Column(nullable = false)
    private String username;    // 用户名
    @Column(nullable = false)
    private String password;    // 密码
    @Column(nullable = false)
    private String salt;        // 加密盐
    @Column
    private String phone;       // 手机号码
    @Column
    private String email;       // 电子邮箱
    @Column(nullable = false)
    private Boolean activated;  // 正常/冻结
    @Column
    private Date createTime;    // 创建时间
}
