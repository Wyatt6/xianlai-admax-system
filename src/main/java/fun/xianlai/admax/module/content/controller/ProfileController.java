package fun.xianlai.admax.module.content.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.xianlai.admax.definition.response.Res;
import fun.xianlai.admax.module.content.model.entity.Profile;
import fun.xianlai.admax.module.content.model.form.ProfileForm;
import fun.xianlai.admax.module.content.service.ProfileService;

/**
 * @author Wyatt
 * @date 2024/3/23
 */
@Slf4j
@RestController
@RequestMapping("/api/content/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    /**
     * 修改个人信息
     *
     * @param input
     * @return
     */
    @SaCheckLogin
    @PostMapping("/editProfile")
    public Res editProfile(@RequestBody ProfileForm input) {
        Assert.notNull(input, "新个人信息为空");
        Assert.notNull(input.getUserId(), "用户ID为空");

        log.info("ProfileForm转换为Profile");
        Profile profileInfo = input.convert();

        log.info("调用个人信息更新服务");
        Profile profile = profileService.updateProfile(profileInfo.getUserId(), profileInfo);

        return new Res().success().addData("profile", profile);
    }

    /**
     * 获取用户个人信息
     *
     * @param userId 用户ID
     * @return profile 个人信息
     */
    @SaCheckLogin
    @GetMapping("/getProfile")
    public Res getProfile(@RequestParam("userId") Long userId) {
        Assert.notNull(userId, "用户ID为空");
        log.info("输入参数: userId={}", userId);

        Profile profile = profileService.getProfile(userId);
        return new Res().success().addData("profile", profile);
    }
}
