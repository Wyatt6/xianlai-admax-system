package fun.xianlai.admax.modules.system.model.entity;

import fun.xianlai.admax.modules.system.model.enums.RequestMethod;
import fun.xianlai.admax.supports.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * 系统接口
 *  - 内置接口：已在Admax中定义，接口数据由程序SQL自动创建，不允许修改
 *  - 非内置接口：由开发者在二次开发中定义，接口数据由具有权限的用户通过管理台新增和管理
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_system_api", indexes = {
        @Index(columnList = "callPath", unique = true),
        @Index(columnList = "url", unique = true),
        @Index(columnList = "sortId")
})
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(1024) not null")
    private String callPath;            // 调用路径，如callPath="father.son"在前端可通过Apis.father.son调用接口url访问后端

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) not null default 'get'")
    private RequestMethod requestMethod;

    @Column(columnDefinition = "varchar(1024) not null")
    private String url;

    @Column(length = 1024)
    private String description;

    @Column(length = 4096)
    private String requestSpec;         // 请求规范

    @Column(length = 4096)
    private String responseSpec;        // 响应规范

    @Column(columnDefinition = "bit not null default 0")
    private Boolean builtIn;            // 内置参数

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
