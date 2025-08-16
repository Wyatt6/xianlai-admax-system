package fun.xianlai.admax.module.content.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.module.content.model.entity.Profile;
import fun.xianlai.admax.module.content.repository.ProfileRepository;
import fun.xianlai.admax.module.content.service.ProfileService;

import java.util.Optional;

/**
 * @author WyattLau
 * @date 2024/3/23
 */
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Profile createProfile(Long userId, String avatar, String nickname, String motto) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}, avatar={}, nickname={}, motto={}", userId, avatar, nickname, motto);

        if (avatar != null && avatar.isBlank()) avatar = null;
        if (nickname != null && nickname.isBlank()) nickname = null;
        if (motto != null && motto.isBlank()) motto = null;

        Profile record = new Profile();
        record.setId(null);
        record.setUserId(userId);
        record.setAvatar(avatar);
        record.setNickname(nickname);
        record.setMotto(motto);
        Profile profile = profileRepository.save(record);
        log.info("新个人信息数据成功保存到数据库: id={}", profile.getId());

        return profile;
    }

    @Override
    public Profile updateProfile(Long userId, Profile input) {
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(input, "新用户个人信息为空");
        log.info("输入参数: userId={}, newProfile={}", userId, input);

        log.info("查询是否存在该Profile");
        Optional<Profile> oldProfile = Optional.ofNullable(profileRepository.findByUserId(userId));
        if (oldProfile.isPresent()) {
            log.info("Profile存在，组装用来更新的对象");
            Profile newProfile = oldProfile.get();
            if (input.getNickname() != null) newProfile.setNickname(input.getNickname());
            if (input.getMotto() != null) newProfile.setMotto(input.getMotto());

            log.info("更新数据库");
            newProfile = profileRepository.save(newProfile);
            return newProfile;
        } else {
            throw new OnceException("要更新的用户个人信息不存在");
        }
    }

    @Override
    public Profile getProfile(Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);

        return profileRepository.findByUserId(userId);
    }
}
