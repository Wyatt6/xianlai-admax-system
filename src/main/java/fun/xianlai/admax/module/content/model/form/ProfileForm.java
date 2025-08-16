package fun.xianlai.admax.module.content.model.form;

import lombok.Data;
import fun.xianlai.admax.module.content.model.entity.Profile;

/**
 * @author WyattLau
 * @date 2024/4/7
 */
@Data
public class ProfileForm {
    private Long id;            // 主键
    private Long userId;        // 所属用户
    private String avatar;      // 头像URL
    private String nickname;    // 昵称
    private String motto;       // 座右铭

    public Profile convert() {
        Profile result = new Profile();

        result.setId(id);
        result.setUserId(userId);
        result.setAvatar(avatar != null ? avatar.trim() : null);
        result.setNickname(nickname != null ? nickname.trim() : null);
        result.setMotto(motto != null ? motto.trim() : null);

        return result;
    }
}
