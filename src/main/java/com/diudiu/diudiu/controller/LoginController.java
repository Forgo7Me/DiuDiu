package com.diudiu.diudiu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.Admin;
import com.diudiu.diudiu.entity.Token;
import com.diudiu.diudiu.entity.User;
import com.diudiu.diudiu.service.AdminService;
import com.diudiu.diudiu.service.TokenService;
import com.diudiu.diudiu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/*
 * 给世界一点小小的二次元震撼
 *
 *  ██   ██         ███████   ████     ██
 * ░██  ██         ██░░░░░██ ░██░██   ░██
 * ░██ ██         ██     ░░██░██░░██  ░██
 * ░████    █████░██      ░██░██ ░░██ ░██
 * ░██░██  ░░░░░ ░██      ░██░██  ░░██░██
 * ░██░░██       ░░██     ██ ░██   ░░████
 * ░██ ░░██       ░░███████  ░██    ░░███
 * ░░   ░░         ░░░░░░░   ░░      ░░░
 *
 * @Author : Drme
 * @Date : 2024/02/22
 */
@RestController
@Slf4j
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private TokenService tokenService;

    @PostMapping("/")
    public R login(@RequestBody Map<String, String> map) {
        String identity = map.get("identity");
        String username = map.get("username");
        String password = map.get("password");
        String token = UUID.randomUUID().toString();
        Token tokenEntity = new Token();
        Integer id = Math.toIntExact(tokenService.count() + 1);
        if (identity.equals("管理员")) {
            if (!(adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username).eq(Admin::getPassword, password)) == null)) {
                // 将tokenEntity的key和value都设为token,time设为当前时间的13位时间戳加上2小时
                tokenEntity.setTokenKey(token);
                tokenEntity.setTokenValue(token);
                Long time = System.currentTimeMillis() + 7200000L;
                tokenEntity.setTime(time);
                tokenEntity.setId(id);
                tokenService.save(tokenEntity);
                return R.ok(token);
            }
        } else {
            if (!(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getPassword, password)) == null)) {
                tokenEntity.setTokenKey(token);
                tokenEntity.setTokenValue(token);
                Long time = System.currentTimeMillis() + 7200000L;
                tokenEntity.setTime(time);
                tokenEntity.setId(id);
                tokenService.save(tokenEntity);
                return R.ok(token);
            }
        }
        return R.error("用户名或密码错误");
    }
}

















