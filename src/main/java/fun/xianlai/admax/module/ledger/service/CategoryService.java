package fun.xianlai.admax.module.ledger.service;

import fun.xianlai.admax.module.ledger.model.definition.TreeNode;
import fun.xianlai.admax.module.ledger.model.entity.Category;

import java.util.List;

/**
 * @author WyattLau
 * @date 2023/10/8
 */
public interface CategoryService {
    /**
     * 创建新交易类别
     *
     * @param userId   用户ID
     * @param category 新交易类别
     * @return 新交易类别对象
     */
    Category createCategory(Long userId, Category category);

    /**
     * 删除记账类别树
     *
     * @param userId 用户ID
     * @param rootId 要删除的记账类别树的根ID
     */
    void deleteCategoryTree(Long userId, Long rootId);

    /**
     * 更新记账类别
     *
     * @param userId   用户ID
     * @param category 记账类别
     * @return 新的记账类别对象
     */
    Category updateCategory(Long userId, Category category);

    /**
     * 变更子树的activated值
     *
     * @param userId       用户ID
     * @param rootId       子树根节点ID
     * @param newActivated 新activated值
     */
    void changeActivated(Long userId, Long rootId, Boolean newActivated);

    /**
     * 上下移动一行
     *
     * @param userId 用户ID
     * @param id     要移动的记账类别ID
     * @param mode   移动模式：0上移，1下移
     */
    void moveOneRow(Long userId, Long id, int mode);

    /**
     * 获取记账类别树（森林）
     *
     * @param userId 用户ID
     * @return 记账类别树
     */
    List<TreeNode<Long, Category>> getCategoryTree(Long userId);

    /**
     * 获取所有记账类别列表
     *
     * @param userId 用户ID
     * @return 记账类别列表
     */
    List<Category> getCategories(Long userId);
}
