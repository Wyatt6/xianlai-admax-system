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

import java.util.Date;

/**
 * 登录日志
 *
 * @author Wyatt6
 * @date 2025/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_login_log", indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "loginTime"),
        @Index(columnList = "userId,loginTime")
})
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Date loginTime;
    @Column
    private String ip;
    @Column
    private String address;
    @Column
    private String os;
    @Column
    private String browser;
    @Column(nullable = false)
    private Boolean loginSuccess;
    @Column
    private String message;
}
