package fun.xianlai.admax.module.ledger.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.ledger.model.constant.AuthorityConst;
import fun.xianlai.admax.module.ledger.model.entity.Journal;
import fun.xianlai.admax.module.ledger.model.form.JournalConditionForm;
import fun.xianlai.admax.module.ledger.model.form.JournalForm;
import fun.xianlai.admax.module.ledger.service.JournalService;

import java.math.BigDecimal;

/**
 * @author Wyatt
 * @date 2024/4/23
 */
@Slf4j
@RestController
@RequestMapping("/api/ledger/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;

    /**
     * 新增记账流水
     *
     * @param input 新流水表单数据
     * @return {journal 新流水对象}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.JOURNAL_ADD)
    @PostMapping("/addJournal")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res addJournal(@RequestBody JournalForm input) {
        Assert.notNull(input, "输入表单为空");
        Assert.notNull(input.getAcctDate(), "记账日期为空");
        Assert.notNull(input.getType(), "收支类型为空");
        Assert.notNull(input.getAmount(), "金额为空");
        Assert.notNull(input.getCategoryId(), "记账类别为空");
        Assert.notNull(input.getChannelId(), "动账渠道为空");
        log.info("请求参数: {}", input);

        log.info("JournalForm转换为Journal");
        Journal journalInfo = input.convert();

        log.info("创建记账流水");
        Long userId = StpUtil.getLoginIdAsLong();
        Journal journal = journalService.createJournal(userId, journalInfo);

        return new Res().success().addData("journal", journal);
    }

    /**
     * 删除记账流水
     *
     * @param journalId 记账流水ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.JOURNAL_DELETE)
    @GetMapping("/deleteJournal")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res deleteJournal(@RequestParam("journalId") Long journalId) {
        Assert.notNull(journalId, "记账流水ID为空");
        log.info("请求参数: journalId={}", journalId);

        log.info("删除账务流水");
        Long userId = StpUtil.getLoginIdAsLong();
        journalService.deleteJournal(userId, journalId);

        return new Res().success();
    }


    /**
     * 编辑记账流水
     *
     * @param input 新流水表单数据
     * @return {journal 新流水对象}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.JOURNAL_EDIT)
    @PostMapping("/editJournal")
    public Res editJournal(@RequestBody JournalForm input) {
        Assert.notNull(input, "输入表单为空");
        Assert.notNull(input.getId(), "记账流水ID为空");
        log.info("请求参数：{}", input);

        log.info("JournalForm转换为Journal");
        Journal journalInfo = input.convert();

        log.info("更新记账流水");
        Long userId = StpUtil.getLoginIdAsLong();
        Journal journal = journalService.updateJournal(userId, journalInfo);

        return new Res().success().addData("journal", journal);
    }

    /**
     * 条件查询记账流水分页数据
     *
     * @param form 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 总页数, totalElements 总条数, journals 本页列表}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.JOURNAL_QUERY)
    @PostMapping("/getJournalsByPageConditionally")
    public Res getJournalsByPageConditionally(@RequestBody JournalConditionForm form) {
        Assert.notNull(form, "查询条件为空");
        log.info("请求参数：{}", form);

        log.info("调用条件查询记账流水分页服务");
        Long userId = StpUtil.getLoginIdAsLong();
        Page<Journal> journals = journalService.getJournalsByPageConditionally(
                form.getPageNum(),
                form.getPageSize(),
                userId,
                form.getStDate(),
                form.getEdDate(),
                form.getType(),
                form.getCategoryId(),
                form.getChannelId(),
                form.getDescription()
        );

        return new Res().success()
                .addData("pageNum", journals.getPageable().getPageNumber())
                .addData("pageSize", journals.getPageable().getPageSize())
                .addData("totalPages", journals.getTotalPages())
                .addData("totalElements", journals.getTotalElements())
                .addData("journals", journals.getContent());
    }

    /**
     * 条件查询总收入和总支出
     *
     * @param form 查询条件
     * @return {debitSum 总收入, creditSum 总支出}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.GET_SUM_CONDITIONALLY)
    @PostMapping("/getSumConditionally")
    public Res getSumConditionally(@RequestBody JournalConditionForm form) {
        Assert.notNull(form, "查询条件为空");
        log.info("请求参数：{}", form);

        Long userId = StpUtil.getLoginIdAsLong();
        BigDecimal debitSum = journalService.getDebitSumConditionally(
                userId,
                form.getStDate(),
                form.getEdDate(),
                form.getType(),
                form.getCategoryId(),
                form.getChannelId(),
                form.getDescription());
        BigDecimal creditSum = journalService.getCreditSumConditionally(
                userId,
                form.getStDate(),
                form.getEdDate(),
                form.getType(),
                form.getCategoryId(),
                form.getChannelId(),
                form.getDescription());

        return new Res().success()
                .addData("debitSum", debitSum)
                .addData("creditSum", creditSum);
    }
}
