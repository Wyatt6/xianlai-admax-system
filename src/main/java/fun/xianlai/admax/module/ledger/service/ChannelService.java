package fun.xianlai.admax.module.ledger.service;


import fun.xianlai.admax.module.ledger.model.entity.Channel;

import java.util.List;

/**
 * @author Wyatt
 * @date 2023/10/7
 */
public interface ChannelService {
    /**
     * 创建新动账渠道
     *
     * @param userId  用户ID
     * @param channel 新动账渠道数据
     * @return 新动账渠道对象
     */
    Channel createChannel(Long userId, Channel channel);

    /**
     * 删除动账渠道
     *
     * @param userId    用户ID
     * @param channelId 要删除的动账渠道ID
     */
    void deleteChannel(Long userId, Long channelId);

    /**
     * 更新动账渠道数据
     *
     * @param userId  用户ID
     * @param channel 动账渠道
     * @return 新的动账渠道对象
     */
    Channel updateChannel(Long userId, Channel channel);

    /**
     * 查询用户的所有动账渠道
     *
     * @param userId 用户ID
     * @return 动账渠道列表
     */
    List<Channel> getChannels(Long userId);

    /**
     * 根据ID查找动账渠道
     *
     * @param userId    用户ID
     * @param channelId 动账渠道ID
     * @return 动账渠道对象
     */
    Channel findChannelById(Long userId, Long channelId);
}
