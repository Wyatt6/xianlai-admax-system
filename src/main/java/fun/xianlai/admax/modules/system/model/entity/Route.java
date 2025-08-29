package fun.xianlai.admax.modules.system.model.entity;

import fun.xianlai.admax.supports.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 系统路由
 *  - 内置路由：已在Admax中定义，路由数据由程序SQL自动创建，不允许修改
 *  - 非内置路由：由开发者在二次开发中定义，路由数据由具有权限的用户通过管理台新增和管理
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_system_route", indexes = {
})
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "varchar(1024) not null")
    private String path;

    @Column(columnDefinition = "varchar(1024) not null")
    private String redirect;

    @Column(columnDefinition = "varchar(1024) not null")
    private String component;

    @Column
    private Long firstChild;

    @Column
    private Long nextBrother;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;            // 路由顺序

    @Column(columnDefinition = "bit not null default 1")
    private Boolean login;          // 是否需要登录才能访问该路由

    @Column
    private String permission;      // 需要的访问权限标识符，null或空串表示不需要访问权限

    @Column(columnDefinition = "bit not null default 0")
    private Boolean menu;           // 是否在菜单栏显示

    @Column
    private String label;           // 在菜单和标签栏显示的标题

    @Column
    private String iconName;        // 图标名称

    @Column(columnDefinition = "bit not null default 0")
    private Boolean builtIn;        // 内置路由

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;
}
