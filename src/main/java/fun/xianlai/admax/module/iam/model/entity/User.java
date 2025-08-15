package fun.xianlai.admax.module.iam.model.entity;

import fun.xianlai.admax.module.iam.model.enums.Gender;
import fun.xianlai.admax.util.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * RBAC-用户
 *
 * @author Wyatt6
 * @date 2025/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_user", indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "nickname", unique = true),
        @Index(columnList = "name"),
        @Index(columnList = "employeeNo", unique = true),
        @Index(columnList = "phone", unique = true),
        @Index(columnList = "email", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String salt;        // 加密盐
    @Column
    private String nickname;
    @Column
    private String avatar;      // 头像（文件名）
    @Column
    private String name;        // 真名
    @Column
    private Gender gender;      // 性别
    @Column
    private String employeeNo;  // 工号
    @Column
    private String photo;       // 照片（文件名）
    @Column
    private String phone;
    @Column
    private String email;
    @Column(nullable = false)
    private Boolean active = false;
    @Column
    private Date registerTime;  // 注册时间
    @Column
    private Long mainDepartmentId;      // 主部门
    @Column
    private Long mainPositionId;        // 主职务/岗位
    // ----- 非持久化属性 -----
    @Transient
    private String captchaKey;  // 验证码KEY
    @Transient
    private String captcha;     // 验证码
    @Transient
    private String certificate; // 凭证
}
