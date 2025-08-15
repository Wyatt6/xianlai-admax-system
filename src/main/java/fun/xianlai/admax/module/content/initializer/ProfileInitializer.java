package fun.xianlai.admax.module.content.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.module.content.model.entity.Profile;
import fun.xianlai.admax.module.content.repository.ProfileRepository;
import fun.xianlai.admax.module.iam.model.constant.UserInitConst;

/**
 * @author Wyatt
 * @date 2024/4/10
 */
@Slf4j
@Component
public class ProfileInitializer implements CommandLineRunner {
    @Autowired
    ProfileRepository profileRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>> 内容中心：加载超级管理员用户初始profile数据 >>>>>");
        try {
            Profile record = new Profile();
            record.setId(null);
            record.setUserId(UserInitConst.SUPER_ADMIN_USER_ID);
            record.setAvatar(null);
            record.setNickname("超级管理员");
            record.setMotto("我是一个无所不能的管理者");
            profileRepository.save(record);
        } catch (DataIntegrityViolationException ignored) {
            log.info("已存在: 超级管理员用户的个人信息记录");
        }
        log.info(">>>>> FINISHED: 内容中心：加载超级管理员用户初始profile数据 >>>>>");
    }
}
