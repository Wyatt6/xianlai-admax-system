package fun.xianlai.admax.module.ledger.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.ledger.model.constant.AuthorityConst;
import fun.xianlai.admax.module.ledger.model.entity.Category;
import fun.xianlai.admax.module.ledger.model.form.CategoryForm;
import fun.xianlai.admax.module.ledger.service.CategoryService;

/**
 * @author Wyatt
 * @date 2024/4/18
 */
@Slf4j
@RestController
@RequestMapping("/api/ledger/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增记账类别
     *
     * @param input 新的记账类别数据
     * @return {category 新的记账类别对象}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_ADD)
    @PostMapping("/addCategory")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res addCategory(@RequestBody CategoryForm input) {
        Assert.notNull(input, "记账类别数据为空");
        Assert.hasText(input.getName(), "记账类别名称为空");
        log.info("请求参数: {}", input);

        log.info("CategoryForm转换为Category");
        Category categoryInfo = input.convert();

        log.info("创建交易类别");
        Long userId = StpUtil.getLoginIdAsLong();
        Category category = categoryService.createCategory(userId, categoryInfo);

        return new Res().success().addData("category", category);
    }

    /**
     * 删除记账类别及其子类别
     *
     * @param id 要删除的记账类别ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_DELETE)
    @GetMapping("/deleteCategoryTree")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res deleteCategoryTree(@RequestParam("id") Long id) {
        Assert.notNull(id, "记账类别ID为空");
        log.info("请求参数: id={}", id);

        log.info("删除记账类别及其下的子类别");
        Long userId = StpUtil.getLoginIdAsLong();
        categoryService.deleteCategoryTree(userId, id);

        return new Res().success();
    }

    /**
     * 编辑记账类别
     *
     * @param input 新的记账类别数据
     * @return {category 新的记账类别对象}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_EDIT)
    @PostMapping("/editCategory")
    public Res editCategory(@RequestBody CategoryForm input) {
        Assert.notNull(input, "记账类别数据为空");
        Assert.notNull(input.getId(), "记账类别ID为空");
        log.info("请求参数：{}", input);

        log.info("CategoryForm转换为Category");
        Category categoryInfo = input.convert();

        log.info("更新类别");
        Long userId = StpUtil.getLoginIdAsLong();
        Category category = categoryService.updateCategory(userId, categoryInfo);

        return new Res().success().addData("category", category);
    }

    /**
     * 启用/禁用记账类别子树
     *
     * @param rootId       子树根节点ID
     * @param newActivated 新activated值
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_EDIT)
    @GetMapping("/changeActivated")
    public Res changeActivated(@RequestParam("rootId") Long rootId, @RequestParam("newActivated") Boolean newActivated) {
        Assert.notNull(rootId, "子树根节点ID为空");
        Assert.notNull(newActivated, "新状态为空");
        log.info("请求参数：rootId={}, newActivated={}", rootId, newActivated);

        log.info("更新类别子树");
        Long userId = StpUtil.getLoginIdAsLong();
        categoryService.changeActivated(userId, rootId, newActivated);

        return new Res().success();
    }

    /**
     * 上/下移动目标记账类别一行
     *
     * @param id   目标记账类别ID
     * @param mode 移动模式：up上移，down下移
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_EDIT)
    @GetMapping("/moveOneRow")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res moveOneRow(@RequestParam("id") Long id, @RequestParam("mode") String mode) {
        Assert.notNull(id, "记账类别ID为空");
        Assert.hasText(mode, "移动模式为空");
        log.info("请求参数: id={}, mode={}", id, mode);
        int modeInt = -1;
        if (mode.equals("up")) {
            modeInt = 0;
        } else if (mode.equals("down")) {
            modeInt = 1;
        } else {
            throw new OnceException("移动模式参数输入错误");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        categoryService.moveOneRow(userId, id, modeInt);
        return new Res().success();
    }


    /**
     * 获取记账类别树（森林）
     *
     * @return {categoryTree 记账类别树（森林）}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_QUERY)
    @GetMapping("/getCategoryTree")
    public Res getCategoryTree() {
        Long userId = StpUtil.getLoginIdAsLong();
        return new Res()
                .success()
                .addData("categoryTree", categoryService.getCategoryTree(userId));
    }

    /**
     * 获取所有记账类别列表
     *
     * @return {categories 所有记账类别列表}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CATEGORY_QUERY)
    @GetMapping("/getCategories")
    public Res getCategories() {
        Long userId = StpUtil.getLoginIdAsLong();
        return new Res()
                .success()
                .addData("categories", categoryService.getCategories(userId));
    }
}
