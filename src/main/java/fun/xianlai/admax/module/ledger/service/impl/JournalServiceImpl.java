package fun.xianlai.admax.module.ledger.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.ledger.model.entity.Category;
import fun.xianlai.admax.module.ledger.model.entity.Journal;
import fun.xianlai.admax.module.ledger.model.enums.AccountType;
import fun.xianlai.admax.module.ledger.repository.CategoryRepository;
import fun.xianlai.admax.module.ledger.repository.JournalRepository;
import fun.xianlai.admax.module.ledger.service.JournalService;
import fun.xianlai.admax.util.DateFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 * @date 2023/10/7
 */
@Slf4j
@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Journal createJournal(Long userId, Journal journal) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(journal, "新流水数据为空");
        log.info("输入参数: userId={}, journal={}", userId, journal);

        log.info("插入记录");
        journal.setId(null);
        journal.setUserId(userId);
        journal = journalRepository.save(journal);
        log.info("成功插入记录: id={}", journal.getId());

        return journal;
    }

    @Override
    public void deleteJournal(Long userId, Long journalId) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(journalId, "记账流水ID为空");
        log.info("输入参数: userId={}, journalId={}", userId, journalId);

        log.info("删除记账流水数据库记录");
        journalRepository.deleteByIdAndUserId(journalId, userId);
    }

    @Override
    public Journal updateJournal(Long userId, Journal journal) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(journal, "新流水数据为空");
        Assert.notNull(journal.getId(), "记账流水ID为空");
        log.info("输入参数: userId={}, journal={}", userId, journal);

        log.info("查询是否存在该流水");
        Optional<Journal> oldJournal = journalRepository.findByIdAndUserId(journal.getId(), userId);
        if (oldJournal.isPresent()) {
            Date acctDate = journal.getAcctDate();
            AccountType type = journal.getType();
            BigDecimal amount = journal.getAmount();
            Long categoryId = journal.getCategoryId();
            Long channelId = journal.getChannelId();
            String description = journal.getDescription();

            log.info("流水存在，组装用来更新的对象");
            Journal newJournal = oldJournal.get();
            if (acctDate != null) newJournal.setAcctDate(acctDate);
            if (type != null) newJournal.setType(type);
            if (amount != null) newJournal.setAmount(amount);
            if (categoryId != null) newJournal.setCategoryId(categoryId);
            if (channelId != null) newJournal.setChannelId(channelId);
            if (description != null) newJournal.setDescription(description);

            log.info("更新数据库记录");
            newJournal = journalRepository.save(newJournal);

            return newJournal;
        } else {
            throw new OnceException("记账流水不存在");
        }
    }

    @Override
    public Page<Journal> getJournalsByPageConditionally(
            int pageNum,
            int pageSize,
            Long userId,
            Date stDate,
            Date edDate,
            AccountType type,
            Long categoryId,
            Long channelId,
            String description) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: pageNum={}, pageSize={}, userId={}, stDate={}, edDate={}, type={}, categoryId={}, channelId={}, description={}",
                pageNum, pageSize, userId, DateFormatter.commonFormat(stDate), DateFormatter.commonFormat(edDate), type, categoryId, channelId, description);

        Sort sort = Sort.by(
                Sort.Order.desc("acctDate"),
                Sort.Order.desc("id")
        );
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        if (stDate != null) {
            Calendar st = Calendar.getInstance();
            st.setTime(stDate);
            st.set(Calendar.HOUR, 0);
            st.set(Calendar.MINUTE, 0);
            st.set(Calendar.SECOND, 0);
            st.set(Calendar.MILLISECOND, 0);
            stDate = st.getTime();
            log.info("修整后的起始日期: stDate={}", stDate);
        }
        if (edDate != null) {
            Calendar ed = Calendar.getInstance();
            ed.setTime(edDate);
            ed.set(Calendar.HOUR, 0);
            ed.set(Calendar.MINUTE, 0);
            ed.set(Calendar.SECOND, 0);
            ed.set(Calendar.MILLISECOND, 0);
            edDate = ed.getTime();
            log.info("修整后的结束日期: edDate={}", edDate);
            edDate.setTime(edDate.getTime() + 24 * 3600 * 1000);   // 单位：ms
            log.info("后移一日后的edDate={}", edDate);
        }

        List<Long> categoryTreeIds = new ArrayList<>();
        if (categoryId != null) {
            log.info("查询以id={}为根的记账分类子树的id列表", categoryId);
            List<Category> categoryTreeNodes = categoryRepository.findSubTreeNodes(categoryId);
            for (Category node : categoryTreeNodes) {
                categoryTreeIds.add(node.getId());
            }
            log.info("记账分类子树的id列表: {}", categoryTreeIds);
        }

        Page<Journal> journalPage = journalRepository.findConditionally(
                userId,
                stDate,
                edDate,
                type,
                categoryTreeIds,
                channelId,
                description,
                pageable);
        log.info("查询结果: {}", journalPage);

        return journalPage;
    }

    @Override
    public BigDecimal getDebitSumConditionally(
            Long userId,
            Date stDate,
            Date edDate,
            AccountType type,
            Long categoryId,
            Long channelId,
            String description) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}, stDate={}, edDate={}, type={}, categoryId={}, channelId={}, description={}",
                userId, DateFormatter.commonFormat(stDate), DateFormatter.commonFormat(edDate), type, categoryId, channelId, description);

        if (stDate != null) {
            Calendar st = Calendar.getInstance();
            st.setTime(stDate);
            st.set(Calendar.HOUR, 0);
            st.set(Calendar.MINUTE, 0);
            st.set(Calendar.SECOND, 0);
            st.set(Calendar.MILLISECOND, 0);
            stDate = st.getTime();
            log.info("修整后的起始日期: stDate={}", stDate);
        }
        if (edDate != null) {
            Calendar ed = Calendar.getInstance();
            ed.setTime(edDate);
            ed.set(Calendar.HOUR, 0);
            ed.set(Calendar.MINUTE, 0);
            ed.set(Calendar.SECOND, 0);
            ed.set(Calendar.MILLISECOND, 0);
            edDate = ed.getTime();
            log.info("修整后的结束日期: edDate={}", edDate);
            edDate.setTime(edDate.getTime() + 24 * 3600 * 1000);   // 单位：ms
            log.info("后移一日后的edDate={}", edDate);
        }

        List<Long> categoryTreeIds = new ArrayList<>();
        if (categoryId != null) {
            log.info("查询以id={}为根的记账分类子树的id列表", categoryId);
            List<Category> categoryTreeNodes = categoryRepository.findSubTreeNodes(categoryId);
            for (Category node : categoryTreeNodes) {
                categoryTreeIds.add(node.getId());
            }
            log.info("记账分类子树的id列表: {}", categoryTreeIds);
        }

        BigDecimal debitSum = journalRepository.sumDebitOrCreditAmountConditionally(
                userId,
                stDate,
                edDate,
                type,
                categoryTreeIds,
                channelId,
                description,
                AccountType.debit);
        log.info("debitSum={}", debitSum);

        return debitSum;
    }

    @Override
    public BigDecimal getCreditSumConditionally(
            Long userId,
            Date stDate,
            Date edDate,
            AccountType type,
            Long categoryId,
            Long channelId,
            String description) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}, stDate={}, edDate={}, type={}, categoryId={}, channelId={}, description={}",
                userId, DateFormatter.commonFormat(stDate), DateFormatter.commonFormat(edDate), type, categoryId, channelId, description);

        if (stDate != null) {
            Calendar st = Calendar.getInstance();
            st.setTime(stDate);
            st.set(Calendar.HOUR, 0);
            st.set(Calendar.MINUTE, 0);
            st.set(Calendar.SECOND, 0);
            st.set(Calendar.MILLISECOND, 0);
            stDate = st.getTime();
            log.info("修整后的起始日期: stDate={}", stDate);
        }
        if (edDate != null) {
            Calendar ed = Calendar.getInstance();
            ed.setTime(edDate);
            ed.set(Calendar.HOUR, 0);
            ed.set(Calendar.MINUTE, 0);
            ed.set(Calendar.SECOND, 0);
            ed.set(Calendar.MILLISECOND, 0);
            edDate = ed.getTime();
            log.info("修整后的结束日期: edDate={}", edDate);
            edDate.setTime(edDate.getTime() + 24 * 3600 * 1000);   // 单位：ms
            log.info("后移一日后的edDate={}", edDate);
        }

        List<Long> categoryTreeIds = new ArrayList<>();
        if (categoryId != null) {
            log.info("查询以id={}为根的记账分类子树的id列表", categoryId);
            List<Category> categoryTreeNodes = categoryRepository.findSubTreeNodes(categoryId);
            for (Category node : categoryTreeNodes) {
                categoryTreeIds.add(node.getId());
            }
            log.info("记账分类子树的id列表: {}", categoryTreeIds);
        }

        BigDecimal creditSum = journalRepository.sumDebitOrCreditAmountConditionally(
                userId,
                stDate,
                edDate,
                type,
                categoryTreeIds,
                channelId,
                description,
                AccountType.credit);
        log.info("creditSum={}", creditSum);

        return creditSum;
    }
}
