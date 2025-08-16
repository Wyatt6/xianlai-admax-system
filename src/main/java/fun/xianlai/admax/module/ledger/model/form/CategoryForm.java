package fun.xianlai.admax.module.ledger.model.form;

import lombok.Data;
import fun.xianlai.admax.module.ledger.model.entity.Category;

/**
 * @author WyattLau
 * @date 2023/10/9
 */
@Data
public class CategoryForm {
    private Long id;            // 主键
    private Long userId;        // 用户ID
    private String name;        // 类别名称
    private Long parentId;      // 父类别ID
    private Boolean activated;  // 激活标志
    private Long sortId;        // 排序ID

    public Category convert() {
        Category category = new Category();

        category.setId(id);
        category.setUserId(userId);
        category.setName(name != null ? name.trim() : null);
        category.setParentId(parentId);
        category.setActivated(activated);
        category.setSortId(sortId);

        return category;
    }
}
