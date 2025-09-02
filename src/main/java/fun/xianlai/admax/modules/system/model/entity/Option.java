package fun.xianlai.admax.modules.system.model.entity;

import fun.xianlai.admax.modules.system.model.enums.JsType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 系统参数
 * <p>
 * 指用于控制Admax系统或基于Admax二次开发系统运行的参数
 * 按系统参数定义者可以分成两类：
 *      - 内置参数：已在Admax中定义，参数数据由程序SQL自动创建，除了参数值和顺序号，其他属性都不允许修改
 *      - 非内置参数：由开发者在二次开发中定义，参数数据由具有权限的用户通过管理台新增和管理
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_system_option", indexes = {
        @Index(columnList = "sortId")
})
public class Option {
    @Id
    private String optionKey;           // 参数键

    @Column(columnDefinition = "varchar(14000) not null")
    private String optionValue;         // 参数值

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null default 'String'")
    private JsType jsType;              // 参数值的js数据类型

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean builtIn;            // 内置参数（仅允许修改optionValue和sortId）

    @Column(columnDefinition = "bit not null default 1")
    private Boolean editable;           // 是否允许修改（即使是内置参数也要editable才允许修改optionValue和sortId）

    @Column(columnDefinition = "bit not null default 0")
    private Boolean frontLoad;          // 是否允许前端加载

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
