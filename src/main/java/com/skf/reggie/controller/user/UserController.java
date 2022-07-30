package com.skf.reggie.controller.user;

import com.skf.reggie.common.R;
import com.skf.reggie.entity.User;
import com.skf.reggie.service.UserService;
import com.skf.reggie.utils.EmailUtil;
import com.skf.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/30
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    public static final String LOGIN_VERIFY_CODE_PREFIX = "login:verify:code:";
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailUtil emailUtil;

    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user) {
        // 获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            // 生成6为验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            log.info("code: {}", code);
            // 发送邮箱验证码
            emailUtil.sendSimpleMail("shenkf111@163.com", "【验证码】", "您的验证码为" + code + ",请妥善保管, 不要告诉他人!");
            // 将验证码保存在redis中
            String codeKey = LOGIN_VERIFY_CODE_PREFIX + phone;

            stringRedisTemplate.opsForValue().set(codeKey, String.valueOf(code), 300, TimeUnit.SECONDS);
            return R.success("发送成功");
        } else {
            return R.error("发送失败");
        }
    }

    @PostMapping("/login")
    public R login(HttpServletRequest httpServletRequest, @RequestBody Map<String, String> loginInfo) {
        if (!CollectionUtils.isEmpty(loginInfo)) {
            String phone = loginInfo.get("phone");
            String code = loginInfo.get("code");
            // 从redis中获取code
            String codeFromRedis = stringRedisTemplate.opsForValue().get(LOGIN_VERIFY_CODE_PREFIX + phone);
            if (StringUtils.equals(code, codeFromRedis)) {
                User user = new User();
                user.setPhone(phone);
                userService.save(user);
                Long id = user.getId();
                httpServletRequest.getSession().setAttribute("user", id);
                return R.success("登录成功");
            }
        }
        return R.error("登录失败");
    }
}
