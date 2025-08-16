package fun.xianlai.admax.module.ledger.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.iam.service.UserService;
import fun.xianlai.admax.module.ledger.model.constant.CategoryInitConst;
import fun.xianlai.admax.module.ledger.model.constant.ChannelInitConst;
import fun.xianlai.admax.module.ledger.model.constant.SubscriberInitConst;
import fun.xianlai.admax.module.ledger.model.entity.Category;
import fun.xianlai.admax.module.ledger.model.entity.Channel;
import fun.xianlai.admax.module.ledger.repository.CategoryRepository;
import fun.xianlai.admax.module.ledger.repository.ChannelRepository;
import fun.xianlai.admax.module.ledger.service.LedgerService;
import fun.xianlai.admax.util.FakeSnowflakeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 * @date 2024/4/30
 */
@Slf4j
@Service
public class LedgerServiceImpl implements LedgerService {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public void subscribe(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);

        try {
            log.info("为用户绑定“记账本订阅者”角色");
            List<Long> subscriberId = new ArrayList<>();
            subscriberId.add(SubscriberInitConst.ID);
            userService.bind(userId, subscriberId);

            log.info("为用户加载订阅者模块的初始数据");
            // 记账类别
            Optional<Category> cat = categoryRepository.findByUserIdAndName(userId, CategoryInitConst.AUTO_GEN_NAME);
            if (cat.isPresent()) {
                log.info("已存在{}记账类别", CategoryInitConst.AUTO_GEN_NAME);
            } else {
                Long id = new FakeSnowflakeUtil().nextId();
                categoryRepository.insert(id, userId, CategoryInitConst.AUTO_GEN_NAME, -1L, 1, 1L);
                log.info("已加载记账类别{}", CategoryInitConst.AUTO_GEN_NAME);
            }
            // 动账渠道
            Optional<Channel> chl = channelRepository.findByUserIdAndName(userId, ChannelInitConst.AUTO_GEN_NAME);
            if (chl.isPresent()) {
                log.info("已存在{}动账渠道", ChannelInitConst.AUTO_GEN_NAME);
            } else {
                Long id = new FakeSnowflakeUtil().nextId();
                channelRepository.insert(id, userId, ChannelInitConst.AUTO_GEN_NAME, 1, 1L);
                log.info("已加载记账类别{}", CategoryInitConst.AUTO_GEN_NAME);
            }
        } catch (Exception e) {
            throw new OnceException("记账本服务订阅失败");
        }
    }

    @Override
    public void unsubscribe(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);

        try {
            List<Long> subscriberId = new ArrayList<>();
            subscriberId.add(SubscriberInitConst.ID);
            userService.cancelBind(userId, subscriberId);
        } catch (Exception e) {
            throw new OnceException("记账本服务退订失败");
        }
    }
}
