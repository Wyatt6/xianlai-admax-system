package fun.xianlai.admax.module.ledger.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.ledger.model.constant.ChannelInitConst;
import fun.xianlai.admax.module.ledger.model.entity.Channel;
import fun.xianlai.admax.module.ledger.repository.ChannelRepository;
import fun.xianlai.admax.module.ledger.repository.JournalRepository;
import fun.xianlai.admax.module.ledger.service.ChannelService;
import fun.xianlai.admax.util.PrimaryKeyGenerator;

import java.util.List;
import java.util.Optional;

/**
 * @author Wyatt
 * @date 2024/4/17
 */
@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private JournalRepository journalRepository;

    @Override
    public Channel createChannel(Long userId, Channel channel) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(channel, "新动账渠道数据为空");
        log.info("输入参数: userId={}, channel={}", userId, channel);

        if (channel.getName() != null && ChannelInitConst.AUTO_GEN_NAME.equals(channel.getName().trim())) {
            log.info("不能与自动生成的{}渠道重复", ChannelInitConst.AUTO_GEN_NAME);
            Optional<Channel> c = channelRepository.findByUserIdAndName(userId, channel.getName().trim());
            if (c.isPresent()) {
                throw new OnceException("无法新增名为“" + channel.getName().trim() + "”的动账渠道");
            }
        }

        if (channel.getName() != null &&
                ChannelInitConst.AUTO_GEN_NAME.equals(channel.getName().trim())) {
            log.info("准备插入的渠道名为: {}，需要先判断是否已存在", channel.getName());
            Optional<Channel> c = channelRepository.findByUserIdAndName(userId, channel.getName().trim());
            if (c.isPresent()) {
                throw new OnceException("无法新增名为“" + channel.getName().trim() + "”的动账渠道");
            }
        }

        log.info("插入记录");
        channel.setId(null);
        channel.setUserId(userId);
        if (channel.getActivated() == null) channel.setActivated(false);
        channel.setSortId(new PrimaryKeyGenerator().next());
        channel = channelRepository.save(channel);
        log.info("成功插入记录: id={}", channel.getId());

        return channel;
    }

    @Override
    public void deleteChannel(Long userId, Long channelId) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(channelId, "动账渠道ID为空");
        log.info("输入参数: userId={}, channelId={}", userId, channelId);

        // 自动生成的<未选择>渠道不支持
        Optional<Channel> chl = channelRepository.findByUserIdAndName(userId, ChannelInitConst.AUTO_GEN_NAME);
        if (chl.isPresent() && chl.get().getId().equals(channelId)) {
            throw new OnceException("此动账渠道无法删除");
        }

        log.info("将相关联的记账明细的动账渠道字段设为{}或null", ChannelInitConst.AUTO_GEN_NAME);
        journalRepository.setChannelIdByUserIdAndChannelId(userId, channelId, chl.isPresent() ? chl.get().getId() : null);

        log.info("删除动账渠道记录");
        channelRepository.deleteByIdAndUserId(channelId, userId);
    }

    @Override
    public Channel updateChannel(Long userId, Channel channel) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(channel, "新动账渠道数据为空");
        Assert.notNull(channel.getId(), "新动账渠道ID为空");
        log.info("输入参数: userId={}, channel={}", userId, channel);

        log.info("查询是否存在该渠道");
        Optional<Channel> oldChannel = channelRepository.findByIdAndUserId(channel.getId(), userId);
        if (oldChannel.isPresent()) {
            // 自动生成的<未选择>渠道不支持
            if (ChannelInitConst.AUTO_GEN_NAME.equals(oldChannel.get().getName().trim())) {
                throw new OnceException("此动账渠道无法更新");
            }

            String name = channel.getName();
            Boolean activated = channel.getActivated();
            Long sortId = channel.getSortId();

            log.info("渠道存在，组装用来更新的对象");
            Channel newChannel = oldChannel.get();
            if (name != null) newChannel.setName(name);
            if (activated != null) newChannel.setActivated(activated);
            if (sortId != null) newChannel.setSortId(sortId);

            log.info("更新数据库记录");
            newChannel = channelRepository.save(newChannel);

            return newChannel;
        } else {
            throw new OnceException("动账渠道不存在");
        }
    }

    @Override
    public List<Channel> getChannels(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);

        Sort sort = Sort.by("sortId");
        List<Channel> channels = channelRepository.findByUserId(userId, sort);
        log.info("查询结果: {}", channels);

        return channels;
    }

    @Override
    public Channel findChannelById(Long userId, Long channelId) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(channelId, "动账渠道ID为空");
        log.info("输入参数: userId={}, channelId={}", userId, channelId);

        return channelRepository.findByIdAndUserId(channelId, userId).orElse(null);
    }
}
