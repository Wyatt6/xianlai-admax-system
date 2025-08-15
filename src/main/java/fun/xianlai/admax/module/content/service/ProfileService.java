package fun.xianlai.admax.module.content.service;


import fun.xianlai.admax.module.content.model.entity.Profile;

/**
 * @author Wyatt
 * @date 2024/3/23
 */
public interface ProfileService {
    /**
     * 创建个人信息记录
     *
     * @param userId   用户ID
     * @param avatar   头像路径
     * @param nickname 昵称
     * @param motto    个性签名（座右铭）
     * @return 个人信息对象
     */
    Profile createProfile(Long userId, String avatar, String nickname, String motto);

    /**
     * 更新个人信息（不含头像）
     *
     * @param userId 用户ID
     * @param input  新的个人信息数据
     * @return 个人信息对象
     */
    Profile updateProfile(Long userId, Profile input);

    /**
     * 根据用户ID获取个人信息
     *
     * @param userId 用户ID
     * @return 个人信息
     */
    Profile getProfile(Long userId);
}
