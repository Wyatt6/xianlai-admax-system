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
import fun.xianlai.admax.module.ledger.model.constant.ChannelInitConst;
import fun.xianlai.admax.module.ledger.model.entity.Channel;
import fun.xianlai.admax.module.ledger.model.form.ChannelForm;
import fun.xianlai.admax.module.ledger.service.ChannelService;

import java.util.List;

/**
 * @author Wyatt
 * @date 2024/4/17
 */
@Slf4j
@RestController
@RequestMapping("/api/ledger/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    /**
     * 当前用户添加动账渠道
     *
     * @param input 新动账渠道数据
     * @return {channel 新动账渠道对象}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CHANNEL_ADD)
    @PostMapping("/addChannel")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res addChannel(@RequestBody ChannelForm input) {
        Assert.notNull(input, "输入数据为空");
        Assert.hasText(input.getName(), "动账渠道名称为空");
        log.info("请求参数: {}", input);

        log.info("ChannelForm转换为Channel");
        Channel channelInfo = input.convert();

        log.info("创建动账渠道");
        Long userId = StpUtil.getLoginIdAsLong();
        Channel channel = channelService.createChannel(userId, channelInfo);

        return new Res().success().addData("channel", channel);
    }

    /**
     * 删除用户的某条动账渠道
     *
     * @param channelId 要删除的渠道ID
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CHANNEL_DELETE)
    @GetMapping("/deleteChannel")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Res deleteChannel(@RequestParam("channelId") Long channelId) {
        Assert.notNull(channelId, "动账渠道ID为空");
        log.info("请求参数：channelId={}", channelId);

        log.info("调用删除动账渠道服务");
        Long userId = StpUtil.getLoginIdAsLong();
        channelService.deleteChannel(userId, channelId);

        return new Res().success();
    }

    /**
     * 编辑动账渠道
     *
     * @param input 新动账渠道数据
     * @return {channel 新动账渠道对象}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CHANNEL_EDIT)
    @PostMapping("/editChannel")
    public Res editChannel(@RequestBody ChannelForm input) {
        Assert.notNull(input, "输入数据为空");
        Assert.notNull(input.getId(), "动账渠道ID为空");
        log.info("请求参数：{}", input);

        log.info("ChannelForm转换为Channel");
        Channel channelInfo = input.convert();

        log.info("更新渠道");
        Long userId = StpUtil.getLoginIdAsLong();
        Channel channel = channelService.updateChannel(userId, channelInfo);

        return new Res().success().addData("channel", channel);
    }

    /**
     * 交换两个动账渠道位置
     *
     * @param id1 渠道1的ID
     * @param id2 渠道2的ID
     * @return {channel1 新渠道1, channel2 新渠道2}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CHANNEL_EDIT)
    @GetMapping("/swapPosition")
    public Res swapPosition(@RequestParam("id1") Long id1, @RequestParam("id2") Long id2) {
        Assert.notNull(id1, "id1为空");
        Assert.notNull(id2, "id2为空");
        log.info("请求参数: id1={}, id2={}", id1, id2);

        log.info("获取两个动账渠道数据");
        Long userId = StpUtil.getLoginIdAsLong();
        Channel channel1 = channelService.findChannelById(userId, id1);
        Channel channel2 = channelService.findChannelById(userId, id2);
        if (channel1 != null && channel2 != null) {
            log.info("(id1, sortId1)=({}, {})", id1, channel1.getSortId());
            log.info("(id2, sortId2)=({}, {})", id2, channel2.getSortId());

            // 自动生成的<未选择>渠道不支持
            if (channel1.getName().equals(ChannelInitConst.AUTO_GEN_NAME) || channel2.getName().equals(ChannelInitConst.AUTO_GEN_NAME)) {
                throw new OnceException("无法改变" + ChannelInitConst.AUTO_GEN_NAME + "动账渠道位置");
            }

            log.info("交换sortId");
            Long tempSortId = channel1.getSortId();
            channel1.setSortId(channel2.getSortId());
            channel2.setSortId(tempSortId);

            Channel newChannel1 = channelService.updateChannel(userId, channel1);
            Channel newChannel2 = channelService.updateChannel(userId, channel2);
            log.info("交换sortId完成");

            return new Res().success()
                    .addData("channel1", newChannel1)
                    .addData("channel2", newChannel2);
        } else {
            throw new OnceException("无法找到动账渠道数据");
        }
    }

    /**
     * 获取本用户的动账渠道列表
     *
     * @return {channels 当前用户的动账渠道列表}
     */
    @SaCheckLogin
    @SaCheckPermission(AuthorityConst.CHANNEL_QUERY)
    @GetMapping("/getChannels")
    public Res getChannels() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Channel> channels = channelService.getChannels(userId);
        return new Res().success().addData("channels", channels);
    }
}
