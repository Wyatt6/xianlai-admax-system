package fun.xianlai.admax.module.ledger.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.ledger.model.constant.CategoryInitConst;
import fun.xianlai.admax.module.ledger.model.definition.TreeNode;
import fun.xianlai.admax.module.ledger.model.entity.Category;
import fun.xianlai.admax.module.ledger.repository.CategoryRepository;
import fun.xianlai.admax.module.ledger.repository.JournalRepository;
import fun.xianlai.admax.module.ledger.service.CategoryService;
import fun.xianlai.admax.util.FakeSnowflakeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

/**
 * @author Wyatt
 * @date 2024/4/18
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JournalRepository journalRepository;

    @Override
    public Category createCategory(Long userId, Category category) {
        Assert.notNull(userId, "用户ID为空");


        log.info("插入记录");
        category.setId(null);
        Assert.notNull(category, "记账类别数据为空");
        log.info("输入参数: userId={}, category={}", userId, category);

        if (category.getName() != null && CategoryInitConst.AUTO_GEN_NAME.equals(category.getName().trim())) {
            log.info("不能与自动生成的{}类别重复", CategoryInitConst.AUTO_GEN_NAME);
            Optional<Category> c = categoryRepository.findByUserIdAndName(userId, category.getName().trim());
            if (c.isPresent()) {
                throw new OnceException("无法新增名为“" + category.getName().trim() + "”的记账类别");
            }
        }
        if (category.getParentId() != null) {
            log.info("不能在{}下生成子类", CategoryInitConst.AUTO_GEN_NAME);
            Optional<Category> c = categoryRepository.findByIdAndUserId(category.getParentId(), userId);
            if (c.isPresent() && c.get().getName().equals(CategoryInitConst.AUTO_GEN_NAME)) {
                throw new OnceException("无法为“" + c.get().getName().trim() + "”记账类别新增子类");
            }
        }
        category.setUserId(userId);
        if (category.getParentId() == null) category.setParentId(-1L);
        if (category.getActivated() == null) category.setActivated(false);
        category.setSortId(new FakeSnowflakeUtil().nextId());
        category = categoryRepository.save(category);
        log.info("成功插入记录: id={}", category.getId());

        return category;
    }

    @Override
    public void deleteCategoryTree(Long userId, Long rootId) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(rootId, "记账类别根ID为空");
        log.info("输入参数: userId={}, rootId={}", userId, rootId);

        // 自动生成的<未分类>类别不支持
        Optional<Category> cat = categoryRepository.findByUserIdAndName(userId, CategoryInitConst.AUTO_GEN_NAME);
        if (cat.isPresent() && cat.get().getId().equals(rootId)) {
            throw new OnceException("此记账类别无法删除");
        }

        Stack<Long> idsToDelete = new Stack<>();
        idsToDelete.push(rootId);
        while (!idsToDelete.empty()) {
            Long id = idsToDelete.pop();
            List<Category> categories = categoryRepository.findByUserIdAndParentId(userId, id);
            if (categories != null && !categories.isEmpty()) {
                for (Category category : categories) {
                    idsToDelete.push(category.getId());
                }
            }

            log.info("将相关联的记账明细的记账类别字段设为{}或null", CategoryInitConst.AUTO_GEN_NAME);
            journalRepository.setCategoryIdByUserIdAndCategoryId(userId, id, cat.isPresent() ? cat.get().getId() : null);

            log.info("删除节点: id={}", id);
            categoryRepository.deleteByIdAndUserId(id, userId);
        }
    }

    @Override
    public void changeActivated(Long userId, Long rootId, Boolean newActivated) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(rootId, "记账类别根ID为空");
        Assert.notNull(newActivated, "新activated值为空");
        log.info("输入参数: userId={}, rootId={}, newActivated={}", userId, rootId, newActivated);

        Optional<Category> root = categoryRepository.findByIdAndUserId(rootId, userId);
        // 自动生成的<未分类>类别不支持
        if (root.isPresent() && CategoryInitConst.AUTO_GEN_NAME.equals(root.get().getName().trim())) {
            throw new OnceException("此记账类别无法变更");
        }

        Stack<Long> idsToChange = new Stack<>();
        idsToChange.push(rootId);
        while (!idsToChange.empty()) {
            Long id = idsToChange.pop();
            List<Category> categories = categoryRepository.findByUserIdAndParentId(userId, id);
            if (categories != null && !categories.isEmpty()) {
                for (Category category : categories) {
                    idsToChange.push(category.getId());
                }
            }

            Optional<Category> old = categoryRepository.findByIdAndUserId(id, userId);
            if (old.isPresent()) {
                Category c = old.get();
                c.setActivated(newActivated);
                categoryRepository.save(c);
                log.info("变更节点: id={}", id);
            }
        }
    }

    @Override
    public Category updateCategory(Long userId, Category category) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(category, "记账类别为空");
        Assert.notNull(category.getId(), "记账类别ID为空");
        log.info("输入参数: userId={}, category={}", userId, category);

        log.info("查询是否存在该类别");
        Optional<Category> oldCategory = categoryRepository.findByIdAndUserId(category.getId(), userId);
        if (oldCategory.isPresent()) {
            // 自动生成的<未分类>类别不支持
            if (CategoryInitConst.AUTO_GEN_NAME.equals(oldCategory.get().getName().trim())) {
                throw new OnceException("此记账类别无法更新");
            }

            String name = category.getName();
            Long parentId = category.getParentId();
            Boolean activated = category.getActivated();
            Long sortId = category.getSortId();

            log.info("类别存在，组装用来更新的对象");
            Category newCategory = oldCategory.get();
            if (name != null) newCategory.setName(name);
            if (parentId != null) newCategory.setParentId(parentId);
            if (activated != null) newCategory.setActivated(activated);
            if (sortId != null) newCategory.setSortId(sortId);

            log.info("更新数据库记录");
            newCategory = categoryRepository.save(newCategory);

            return newCategory;
        } else {
            throw new OnceException("记账类别不存在");
        }
    }

    @Override
    public void moveOneRow(Long userId, Long id, int mode) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(id, "记账类别ID为空");
        log.info("输入参数：userId={}, id={}, mode={}", userId, id, mode);
        if (mode != 0 && mode != 1) {
            throw new OnceException("移动模式参数输入错误");
        }

        Optional<Category> targetCategory = categoryRepository.findByIdAndUserId(id, userId);
        if (targetCategory.isPresent()) {
            // 自动生成的<未分类>类别不支持
            if (CategoryInitConst.AUTO_GEN_NAME.equals(targetCategory.get().getName().trim())) {
                throw new OnceException("无法改变" + CategoryInitConst.AUTO_GEN_NAME + "记账类别位置");
            }

            log.info("按顺序查找同一父节点以下的子节点");
            Sort sort = Sort.by("sortId");
            List<Category> categories = categoryRepository.findByUserIdAndParentId(userId, targetCategory.get().getParentId(), sort);

            log.info("确定目标类别及其前驱后继");
            Category pre = null;
            Category target = null;
            Category next = null;
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId().equals(targetCategory.get().getId())) {
                    target = categories.get(i);
                    if (i + 1 < categories.size()) next = categories.get(i + 1);
                    break;
                }
                pre = categories.get(i);
            }
            log.info("pre={}", pre);
            log.info("target={}", target);
            log.info("next={}", next);

            if (mode == 0) {
                log.info("上移");
                if (pre == null) {
                    throw new OnceException("无法继续上移");
                }
                // 自动生成的<未分类>类别不支持
                if (CategoryInitConst.AUTO_GEN_NAME.equals(pre.getName().trim())) {
                    throw new OnceException("无法改变" + CategoryInitConst.AUTO_GEN_NAME + "记账类别位置");
                }

                log.info("交换sortId");
                Long tempSortId = target.getSortId();
                target.setSortId(pre.getSortId());
                pre.setSortId(tempSortId);

                updateCategory(userId, target);
                updateCategory(userId, pre);
                log.info("交换sortId完成");
            } else {
                log.info("下移");
                if (next == null) {
                    throw new OnceException("无法继续下移");
                }
                // 自动生成的<未分类>类别不支持
                if (CategoryInitConst.AUTO_GEN_NAME.equals(next.getName().trim())) {
                    throw new OnceException("无法改变" + CategoryInitConst.AUTO_GEN_NAME + "记账类别位置");
                }

                log.info("交换sortId");
                Long tempSortId = target.getSortId();
                target.setSortId(next.getSortId());
                next.setSortId(tempSortId);

                updateCategory(userId, target);
                updateCategory(userId, next);
                log.info("交换sortId完成");
            }
        } else {
            throw new OnceException("找不到要移动的记账类别");
        }
    }

    @Override
    public List<TreeNode<Long, Category>> getCategoryTree(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数：userId={}", userId);

        log.info("查询分类列表");
        Sort sort = Sort.by("sortId");
        List<Category> categoryList = categoryRepository.findByUserId(userId, sort);

        log.info("先把每条记录封装成节点，放在容器中等待组装成树");
        Map<Long, TreeNode<Long, Category>> mapping = new HashMap<>();
        for (Category item : categoryList) {
            TreeNode<Long, Category> node = new TreeNode<>(item.getId(), item);
            mapping.put(item.getId(), node);
        }

        log.info("将容器中待组装的节点，根据父子关系进行组装");
        List<TreeNode<Long, Category>> categoryTree = new ArrayList<>();
        for (Category item : categoryList) {
            if (item.getParentId() == null || item.getParentId() == -1L || mapping.get(item.getParentId()) == null) {
                // 此节点没有父节点：表示是父节点，直接加入到森林中
                categoryTree.add(mapping.get(item.getId()));
            } else {
                // 此节点有父节点：将此节点挂载到父节点的children列表下
                TreeNode<Long, Category> parent = mapping.get(item.getParentId());
                parent.accessChildrenInstance().add(mapping.get(item.getId()));
            }
        }

        log.info("计算树大小");
        for (TreeNode<Long, Category> root : categoryTree) {
            root.countNodes();
        }

        return categoryTree;
    }

    @Override
    public List<Category> getCategories(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数：userId={}", userId);

        Sort sort = Sort.by("sortId");
        List<Category> categories = categoryRepository.findByUserId(userId, sort);
        log.info("查询结果: {}", categories);

        return categories;
    }
}
