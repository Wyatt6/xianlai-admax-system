package fun.xianlai.admax.module.ledger.model.form;

import lombok.Data;
import fun.xianlai.admax.module.ledger.model.entity.Channel;

/**
 * @author WyattLau
 * @date 2023/10/7
 */
@Data
public class ChannelForm {
    private Long id;            // 主键
    private Long userId;        // 用户ID
    private String name;        // 渠道名称
    private Boolean activated;  // 激活标志
    private Long sortId;        // 排序ID

    public Channel convert() {
        Channel channel = new Channel();

        channel.setId(id);
        channel.setUserId(userId);
        channel.setName(name != null ? name.trim() : null);
        channel.setActivated(activated);
        channel.setSortId(sortId);

        return channel;
    }
}
